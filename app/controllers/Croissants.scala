package controllers

import javax.inject.{Inject, Singleton}

import models.Croissant
import play.api.{Configuration, Logger}
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Croissants @Inject()(
  val messagesApi: MessagesApi,
  val conf: Configuration)
  (implicit reactiveMongoApi: ReactiveMongoApi, ec: ExecutionContext) extends Controller with I18nSupport {

  val API_SECRET = conf.getString("api.secret").getOrElse(throw new RuntimeException("Missing api.secret configuration"))

  val newCroissantsForm = Form(tuple("from" -> email, "subject" -> text, "secret" -> nonEmptyText))
  def newCroissant =  Action.async(parse.tolerantJson) { implicit request =>
    newCroissantsForm.bindFromRequest().fold(
      err => Future.successful(BadRequest(err.errorsAsJson)),
      {
        case (from, subject, API_SECRET) =>
          if (isZenEmail(from)) {
            Logger.debug(s"New croissants for : $from")
            Croissant.add(from).map(_ => Ok)
          } else {
            Logger.debug(s"Mail ignored from : $from")
            Future.successful(Ok)
          }
        case _ => Future.successful(Forbidden)
      }
    )
  }

  def list = Action.async {
    Croissant.list.map(croissants => Ok(Json.toJson(croissants)))
  }

  private def isZenEmail(email: String): Boolean = {
    val domains = Set("zengularity.com", "zenexity.fr", "zenexity.com")

    domains.exists(domain => email.endsWith(domain))
  }

}
