package modules.mail

import javax.inject.Inject

import play.api.libs.mailer._
import java.io.File

import common.Config
import org.apache.commons.mail.EmailAttachment

class Mail @Inject() (config: Config, mailerClient: MailerClient) {

  def confirm(victimName: String, userName: String, to: String = config.Mail.all, toName: String = "All", subject : String = "Croissants") = {
    val fromField = "Zencroissants <"+ config.Mail.contact +">"

    val toField = Seq(toName + "<"+ to +">")
    val body = views.txt.email.confirmCroissants(victimName, userName).toString

    send(Email(
      subject = subject,
      from = fromField,
      to = toField,
      bodyHtml = Some(body.toString)
    ))
  }

  def all(victimName: String, message: String, zencroissantURL: String) = {

    val body = views.txt.email.zenall(victimName, message, zencroissantURL).toString

    send(Email(
      subject = "Zencroissant a désigné sa nouvelle victime !",
      from = "Zencroissants <"+ config.Mail.contact +">",
      to = Seq("All <"+ config.Mail.all +">"),
      bodyHtml = Some(body.toString)
    ))
  }

  def pression(victimName: String, userName: String, to: String) = {
    val fromField = "Zencroissants <"+ config.Mail.contact +">"

    val toField = Seq(victimName + " <"+ to +">")
    val body = views.txt.email.pression(victimName, userName).toString

    send(Email(
      subject = userName + " vient de te relancer. Tu vas devoir payer tes croissants rapidos.",
      from = fromField,
      to = toField,
      bodyHtml = Some(body.toString)
    ))
  }

  def victim(victimName: String, to: String) = {
    val fromField = "Zencroissants <"+ config.Mail.contact +">"

    val toField = Seq(victimName + " <"+ to +">")
    val body = views.txt.email.victim(victimName)

    send(Email(
      subject = "Croissify !",
      from = fromField,
      to = toField,
      bodyHtml = Some(body.toString)
    ))
  }

  def send(email: Email) = {
    if(config.Mail.mock) {
      println(email)
    }
    mailerClient.send(email)
  }
}
