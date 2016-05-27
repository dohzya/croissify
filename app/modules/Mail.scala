package modules.mail

import javax.inject.Inject
import play.api.libs.mailer._
import java.io.File
import org.apache.commons.mail.EmailAttachment
import core.common.Global
import modules.mail.templates._

class Mail @Inject() (global: Global, mailerClient: MailerClient) {

  def confirm(victimName: String, userName: String, to: String = Mail.All.value, toName: String = "All", subject : String = "Croissants") = {
    val fromField = "Zencroissants <"+ global.Mail.contact +">"

    val toField = Seq(toName + "<"+ to +">")
    val body = Confirm.template(victimName, userName)

    send(Email(
      subject = subject,
      from = fromField,
      to = toField,
      bodyHtml = Some(body)
    ))
  }

  def all(victimName: String, userName: String, zencroissantURL: String) = {
    val fromField = "Zencroissants <"+ global.Mail.contact +">"

    val toField = Seq("All <"+ Mail.All.value +">")
    val body = All.template(victimName, userName, zencroissantURL: String)

    send(Email(
      subject = "Zencroissant a désigné sa nouvelle victime !",
      from = fromField,
      to = toField,
      bodyHtml = Some(body)
    ))
  }

  def pression(victimName: String, userName: String, to: String) = {
    val fromField = "Zencroissants <"+ global.Mail.contact +">"

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
    val fromField = "Zencroissants <"+ global.Mail.contact +">"

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

object Mail {
  case class Destination(value: String)
  object All extends Destination("all@zengularity.com")
}
