package controllers

import javax.inject.{Inject, Singleton}

import common.Config
import models.Croissant
import modules.mail.Mail
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Croissants @Inject()(
  val messagesApi: MessagesApi,
  val config: Config,
  val mailer : Mail,
  val ws: WSClient)
  (implicit reactiveMongoApi: ReactiveMongoApi, ec: ExecutionContext) extends Controller with I18nSupport {

  val newCroissantsForm = Form(tuple("from" -> email, "subject" -> text, "secret" -> nonEmptyText))
  def newCroissant =  Action.async(parse.tolerantJson) { implicit request =>
    newCroissantsForm.bindFromRequest().fold(
      err => Future.successful(BadRequest(err.errorsAsJson)),
      {
        case (from, subject, config.Api.secret) =>
          val email = from.trim
          getUserIdFromEmail(email) match {
            case Some(victimId  )  =>
              Logger.debug(s"New croissants for : $email")
              Croissant.add(victimId).map { _ =>
                mailer.victim(victimId, email)
                mailer.all(victimId, subject, config.Ui.host)
                Ok
              }
            case None =>
              Logger.debug(s"Mail ignored from : $email")
              Future.successful(Ok)
          }
        case _ => Future.successful(Forbidden)
      }
    )
  }

  def index = Action.async {
    Croissant.list.map { list =>
      Ok(views.html.index(list))
    }
  }

  def gmailMails = Action.async { implicit request =>
    val idListFut = ws.url("https://www.googleapis.com/gmail/v1/users/me/messages")
    .withQueryString("q" -> "is:unread")
    .withHeaders(
      "Authorization" -> "Bearer ya29.Ci8SA2625pcE-3TrHfrfaeMyUOBiPwjNPx1n9TRbGsBS-DspSxy3yZ1f5zQnIGfekw"
    )
    .get()
    .map(resp =>
      (resp.json \ "messages" \\ "id" )
    )

    def msg(id: String) = ws.url(s"https://www.googleapis.com/gmail/v1/users/me/messages/$id")
    .withHeaders(
      "Authorization" -> "Bearer ya29.Ci8SA2625pcE-3TrHfrfaeMyUOBiPwjNPx1n9TRbGsBS-DspSxy3yZ1f5zQnIGfekw"
    ).get()
    .map(_.json)

    def update(id: String) = ws.url(s"https://www.googleapis.com/gmail/v1/users/me/messages/$id/modify")
    .withHeaders(
      "Authorization" -> "Bearer ya29.Ci8SA2625pcE-3TrHfrfaeMyUOBiPwjNPx1n9TRbGsBS-DspSxy3yZ1f5zQnIGfekw"
    ).setContentType("application/json")
    .post(
      """{
        "removeLabelId" : "UNREAD"
      }"""
    )

    (
      for {
        idList <- idListFut
        msgList <- Future.traverse(idList){
          id => msg(id.asOpt[String].getOrElse(""))
            .flatMap{ _ =>
              update(id)
              id
            }
        }
      } yield msgList
    ).map{
      msgList => Ok("New Messages: " + msgList)
    }
  }

  private def getUserIdFromEmail(emviews.txt.email.victim(victimName)ail: String): Option[String] = {
    val domains = config.Croissants.includedDomains
    val excludedEmails = config.Croissants.excludedEmails

    if (domains.exists(domain => email.endsWith(domain)) && !excludedEmails.contains(email)) {
      Some(email.split("@")(0))
    } else {
      None
    }
  }

}
