package core.common

import play.api._
import scala.collection.JavaConversions._
import play.api.Play.current
import play.api._

object Zencroissants {

  object Mail {
    val contact = Play.configuration.getString("mail.contact").getOrElse(sys.error("Missing contact email"))
  }
}
