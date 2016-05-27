name := """zencroissants"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.11.11",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.typesafe.play" %% "play-mailer" % "4.0.0"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
