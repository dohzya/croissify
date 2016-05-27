package modules.mail

import javax.inject.Inject
import play.api.libs.mailer._
import java.io.File
import org.apache.commons.mail.EmailAttachment
import core.common.Zencroissants
import modules.mail.templates._

class Mail @Inject() (mailerClient: MailerClient) {

  def confirm(victimName: String, userName: String, to: String = Mail.All.value, toName: String = "All", subject : String = "Croissants") = {
    val fromField = "Zencroissants <"+ Zencroissants.Mail.contact +">"

    val toField = Seq(toName + "<"+ to +">")
    val body = Confirm.template(victimName, userName)

    send(Email(
      subject = subject,
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
