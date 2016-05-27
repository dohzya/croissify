package common

import javax.inject._

import play.api._

class Config @Inject() (conf: Configuration) {
  object Mail {
    lazy val contact = conf.getString("mail.contact").getOrElse(sys.error("Missing contact email"))
    lazy val all = conf.getString("mail.all").getOrElse(sys.error("Missing all email"))
  }

  object Api {
    lazy val secret = conf.getString("api.secret").getOrElse(sys.error("Missing api.secret"))
  }

  object Ui {
    lazy val host = conf.getString("ui.host").getOrElse(sys.error("Missing ui host url"))
  }

  object Croissants {
    lazy val excludedEmails = {
      val str = conf.getString("croissants.excluded_emails").getOrElse(sys.error("Missing excluded Emails"))
      str.split(",").map(_.trim).toList
    }
    lazy val includedDomains = {
      val str = conf.getString("croissants.included_domains").getOrElse(sys.error("Missing included domains"))
      str.split(",").map(_.trim).toList
    }
  }
}
