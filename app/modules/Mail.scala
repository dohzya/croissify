package modules.mail

import javax.inject.Inject

import play.api.libs.mailer._
import java.io.File

import common.Config
import org.apache.commons.mail.EmailAttachment
import modules.mail.templates._

class Mail @Inject() (config: Config, mailerClient: MailerClient) {

  def confirm(victimName: String, userName: String, to: String = config.Mail.all, toName: String = "All", subject : String = "Croissants") = {
    val fromField = "Zencroissants <"+ config.Mail.contact +">"

    val toField = Seq(toName + "<"+ to +">")
    val body = Confirm.template(victimName, userName)

    send(Email(
      subject = subject,
      from = fromField,
      to = toField,
      bodyHtml = Some(body)
    ))
  }

  def all(victimName: String, zencroissantURL: String) = {
    val fromField = "Zencroissants <"+ config.Mail.contact +">"

    val toField = Seq("All <"+ config.Mail.all +">")
    val body = All.template(victimName, zencroissantURL: String)

    send(Email(
      subject = "Zencroissant a désigné sa nouvelle victime !",
      from = fromField,
      to = toField,
      bodyHtml = Some(body)
    ))
  }

  def pression(victimName: String, userName: String, to: String) = {
    val fromField = "Zencroissants <"+ config.Mail.contact +">"

    val toField = Seq(victimName + " <"+ to +">")
    val body = Pression.template(victimName, userName)

    send(Email(
      subject = userName + " vient de te relancer. Tu vas devoir payer tes croissants rapidos.",
      from = fromField,
      to = toField,
      bodyHtml = Some(body)
    ))
  }

  def victim(victimName: String, to: String) = {
    val fromField = "Zencroissants <"+ config.Mail.contact +">"

    val toField = Seq(victimName + " <"+ to +">")
    val body = Victim.template(victimName)

    send(Email(
      subject = "Croissify !",
      from = fromField,
      to = toField,
      bodyHtml = Some(body)
    ))
  }

  def send(email: Email) = {
    mailerClient.send(email)
  }
}
