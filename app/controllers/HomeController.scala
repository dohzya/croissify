package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import modules.mail.Mail

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val mailer : Mail) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action { implicit request =>
    Logger.debug(request.toString())
    mailer.victim("Arnaud", "alw@zengularity.com")
    Ok(views.html.index("Your new application is ready."))
  }

}