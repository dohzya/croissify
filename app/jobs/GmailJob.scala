package jobs

import javax.inject.{Inject, Singleton}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import common.Config
import org.joda.time.DateTime
import play.api.libs.json.JsValue
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.util.{Failure, Success}

case class AccessToken(
  value: String,
  refreshAt: DateTime
)

@Singleton
class GmailJob @Inject()(
  val config: Config,
  val ws: WSClient,
  val system: ActorSystem
) {

  val logger = play.api.Logger("GmailJob")

  val refreshtoken = config.Gmail.refreshtoken
  val clientId = config.Gmail.clientId
  val clientSecret = config.Gmail.clientSecret

  def schedule(accessToken: Option[AccessToken]): Unit = {
    system.scheduler.scheduleOnce(5 seconds) {
      val now = DateTime.now

      val accessTokenFtr = if (accessToken.isEmpty || now.isAfter(accessToken.get.refreshAt)) {
        refreshAccessToken()
      } else Future.successful(accessToken.get)

      val result = for {
        accessToken <- accessTokenFtr
        ids <- listUnreadGmailMessages(accessToken)
      } yield (accessToken, ids)

      result.onComplete {
        case Success((accessToken, result)) =>
          println(result)
          schedule(Some(accessToken))
        case Failure(e) =>
          logger.error("Error during gmail job: ", e)
          schedule(accessToken)
      }
    }
    ()
  }

  private def listUnreadGmailMessages(accessToken: AccessToken): Future[Seq[JsValue]] = {
    ws.url("https://www.googleapis.com/gmail/v1/users/me/messages")
      .withQueryString("q" -> "is:unread")
      .withHeaders(
        "Authorization" -> s"Bearer ${accessToken.value}"
      )
      .get()
      .map {
        case response if response.status == 200 =>
          (response.json \ "messages" \\ "id")
        case response => throw new Exception(s"Could not get new messages ids, error: ${response.status} ${response.statusText}")
      }
  }

  private def refreshAccessToken(): Future[AccessToken] = {
    ws.url("https://www.googleapis.com/oauth2/v3/token")
      .post(Map(
        "client_id" -> Seq(clientId),
        "client_secret" -> Seq(clientSecret),
        "grant_type" -> Seq("refresh_token"),
        "refresh_token" -> Seq(refreshtoken)
      ))
      .map {
        case response if response.status == 200 =>
          val accessToken = (response.json \ "access_token").as[String]
          val expiresIn = (response.json \ "expires_in").as[Int]
          AccessToken(accessToken, DateTime.now.plus(expiresIn * 1000 / 2))
        case response => throw new Exception(s"Could not refresh access token, error: ${response.status} ${response.statusText}")
      }
  }

}