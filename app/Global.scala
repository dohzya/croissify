package core.common

import javax.inject._
import play.api._
import scala.collection.JavaConversions._
import play.api.Play.current
import play.api._

class Global @Inject() (conf: Configuration) {
  object Mail {
    val contact = conf.getString("mail.contact").getOrElse(sys.error("Missing contact email"))
  }
}
