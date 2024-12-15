ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "ambience_diffuser",
	  assembly / mainClass := Some("AmbienceDiffuser")
  )

import sbtassembly.AssemblyPlugin.autoImport._

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "native-image", _ @ _*) => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case x => MergeStrategy.first
}

// akka
resolvers += "Akka library repository".at("https://repo.akka.io/maven")
val AkkaVersion = "2.10.0"
val AkkaHttpVersion = "10.7.0"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "org.scalatest" %% "scalatest" % "3.2.19" % "test",
  "com.jhlabs" % "filters" % "2.0.235",
  "io.spray" %%  "spray-json" % "1.3.6",
  "net.jockx" % "test-jssc" % "2.9.3",
  "javazoom" % "jlayer" % "1.0.1"
)

// logger
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.5.7" // Compatible with SLF4J

// Processing for video generation
libraryDependencies += "org.processing" % "core" % "3.3.7"
