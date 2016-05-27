package controllers

import javax.inject._
import play.api.mvc._

import modules.mail.Mail

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val mailer : Mail) extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }
}
