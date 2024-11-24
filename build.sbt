ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "ambience_diffuser"
  )

// akka
resolvers += "Akka library repository".at("https://repo.akka.io/maven")
val AkkaVersion = "2.10.0"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
  "org.scalatest" %% "scalatest" % "3.2.19" % "test",
  "com.jhlabs" % "filters" % "2.0.235"
)

// logger
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.5.6" // Compatible with SLF4J