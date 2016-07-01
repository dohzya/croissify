name := """zencroissants"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  ws,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.11",
  "org.julienrf" %% "enum" % "3.0",
  "com.typesafe.play" %% "play-mailer" % "5.0.0-M1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.google.api-client" % "google-api-client" % "1.22.0",
  "com.google.apis" % "google-api-services-gmail" % "v1-rev45-1.22.0"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator
