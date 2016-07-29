package controllers

import javax.inject.{Inject, Singleton}

import common.Config
import jobs.GmailJob
import models.Croissant
import modules.mail.Mail
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{Action, ActionBuilder, Controller, Request, Result, WrappedRequest}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Croissants @Inject()(
  val messagesApi: MessagesApi,
  val gmailJob: GmailJob
)(implicit
  val reactiveMongoApi: ReactiveMongoApi,
  val config: Config,
  val mailer: Mail,
  ec: ExecutionContext) extends Controller with I18nSupport {

  gmailJob.schedule(None)

  case class AuthenticatedRequest[A](email: String, request: Request[A]) extends WrappedRequest[A](request) {
    def trigram = email.slice(0, email.indexOf('@'))
  }

  object AuthenticatedAction extends ActionBuilder[AuthenticatedRequest] {
    def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]) = {
      request.session.get("email") match {
        case Some(email) => block(AuthenticatedRequest(email, request))
        case None => Future.successful(Redirect(routes.Oauth.login(request.uri)))
      }
    }
  }

  val newCroissantsForm = Form(tuple("from" -> email, "subject" -> text, "secret" -> nonEmptyText))
  def newCroissant = Action.async(parse.tolerantJson) { implicit request =>
    newCroissantsForm.bindFromRequest().fold(
      err => Future.successful(BadRequest(err.errorsAsJson)),
      {
        case (from, subject, config.Api.secret) =>
          val email = from.trim
          Croissant.addCroissant(email, subject).map(_ => Ok)
        case _ => Future.successful(Forbidden)
      }
    )
  }

  def index = AuthenticatedAction.async {
    Croissant.list.map { list =>
      Ok(views.html.index(list))
    }
  }

  def confirm(id: String) = AuthenticatedAction.async { request =>
    Croissant.findById(id).map {
      case Some(croissant) =>
        println(s"Confirm $id by ${request.trigram}")
        // Croissant.vote(id)
        Ok(Json.obj("success" -> "Croissant confirmed"))
      case None => NotFound(Json.obj("error" -> "Croissant not found :-("))
    }
  }

  def pression(id: String) = AuthenticatedAction.async { request =>
    Croissant.findById(id).map {
      case Some(croissant) =>
        println(s"Make pression on $id by ${request.trigram}")
        // Croissant.pression(id)
        Ok(Json.obj("success" -> "Pression on croissant FIRED"))
      case None => NotFound(Json.obj("error" -> "Croissant not found :-("))
    }
  }

}
