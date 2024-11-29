ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "generate_sound",
    libraryDependencies ++= Seq(
      // spray-json pour convertir Snapshot en JSON (si nécessaire)
      "io.spray" %% "spray-json" % "1.3.6",
      // Logger
      "ch.qos.logback" % "logback-classic" % "1.2.11"
    )
  )
