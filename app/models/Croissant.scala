package models

import common.Config
import org.joda.time.DateTime
import play.api.libs.json.{JsResult, JsValue, _}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import enum.Enum
import modules.mail.Mail
import play.api.Logger
import utils.Repository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

sealed trait Status
object Status {
  case object Pending extends Status
  case object Done extends Status

  val statusEnum: Enum[Status] = Enum.derived[Status]

  implicit val statusJsonFormat: Format[Status] = new Format[Status] {
    override def reads(json: JsValue): JsResult[Status] = json.validate[String].flatMap { str =>
      statusEnum.decode(str) match {
        case Right(status) => JsSuccess(status)
        case Left(err) => JsError(s"Invalid status value. Valid values are : ${err.validValues}")
      }
    }
    override def writes(status: Status): JsValue = JsString(statusEnum.encode(status))
  }
}

case class Croissant(
  id: String,
  victimId: String,
  creationDate: DateTime,
  doneDate: Option[DateTime],
  status: Status,
  voters: Seq[String],
  email: String
) {
  def isDone = doneDate.isDefined
}

object Croissant extends Repository[Croissant] {
  val collectionName: String = "croissants"
  val logger = play.api.Logger("croissant")
  implicit val format: OFormat[Croissant] = Json.format[Croissant] //.asInstanceOf[OFormat[Croissant]]

  val nbVotersToDone = 5

  def genId() = java.util.UUID.randomUUID.toString

  def add(userId: String, email: String)(implicit reactiveMongoApi: ReactiveMongoApi): Future[WriteResult] = {
    val croissant = Croissant(genId(), userId, DateTime.now(), None, Status.Pending, Nil, email)
    logger.info(s"Add croissant ${croissant.id}($userId)")
    Croissant.save(croissant)
  }

  def addCroissant(email: String, subject: Option[String])(implicit config: Config, mailer: Mail, reactiveMongoApi: ReactiveMongoApi): Future[Unit] = {
    getUserIdFromEmail(email) match {
      case Some(victimId) =>
        Logger.debug(s"New croissants for : $email")
        Croissant.add(victimId, email).map { _ =>
          mailer.victim(victimId, email)
          mailer.all(victimId, subject, config.Ui.host)
          ()
        }
      case None =>
        Future.successful(Logger.debug(s"Mail ignored from : $email"))
    }
  }

  def chooseDate(id: String, date: DateTime)(implicit reactiveMongoApi: ReactiveMongoApi): Future[WriteResult] = {
    val query = Json.obj(
      "id" -> id
    )
    update(query, Json.obj(
      "$set" -> Json.obj(
        "doneDate" -> date
      )
    ))
  }

  def findById(id: String)(implicit reactiveMongoApi: ReactiveMongoApi) = findByOpt(Json.obj("id" -> id))

  def vote(croissant: Croissant, from: String)(implicit reactiveMongoApi: ReactiveMongoApi) = {
    logger.info(s"User $from voted for croissant ${croissant.id}(${croissant.victimId})")
    update(
      Json.obj("id" -> croissant.id),
      Json.obj("$addToSet" -> Json.obj("voters" -> from))
    ).flatMap { _ =>
      if (croissant.voters.size >= nbVotersToDone && !croissant.isDone) {
        update(
          Json.obj(
            "id" -> croissant.id,
            "doneDate" -> Json.obj("$exists" -> false)
          ),
          Json.obj("$set" -> Json.obj("doneDate" -> DateTime.now))
        )
      }
      else Future.successful(())
    }
  }

  def listNotDone()(implicit reactiveMongoApi: ReactiveMongoApi) = {
    list(Json.obj(
      "doneDate" -> Json.obj(
        "$exists" -> false
      )
    ))
  }

  def findNotDone(victimId: String)(implicit reactiveMongoApi: ReactiveMongoApi) = {
    findByOpt(Json.obj(
      "victimId" -> victimId,
      "doneDate" -> Json.obj(
        "$exists" -> false
      )
    ))
  }

  def getUserIdFromEmail(email: String)(implicit config: Config): Option[String] = {
    val domains = config.Croissants.includedDomains
    val excludedEmails = config.Croissants.excludedEmails

    if (domains.exists(domain => email.endsWith(domain)) && !excludedEmails.contains(email)) {
      Some(email.split("@")(0))
    } else {
      None
    }
  }

  def findByDate(implicit reactiveMongoApi: ReactiveMongoApi) = {
    val beginDate = DateTime.now.dayOfMonth().withMinimumValue()
                      .hourOfDay().withMinimumValue()
                      .minuteOfHour().withMinimumValue()
                      .secondOfMinute().withMinimumValue()
    val query = Json.obj(
      "doneDate" -> Json.obj(
        "$gte" -> beginDate
      ),
      "doneDate" -> Json.obj(
        "$exists" -> true
      )
    )
    list(query)
  }

}
