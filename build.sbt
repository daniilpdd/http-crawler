ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "http-crawler"
  )

val zioVersion = "2.1.11"
libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-macros" % zioVersion
)

val testVersion = "2.1.13"
libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test" % testVersion % Test,
  "dev.zio" %% "zio-test-sbt" % testVersion % Test,
  "dev.zio" %% "zio-test-magnolia" % testVersion % Test
)

libraryDependencies += "dev.zio" %% "zio-json" % "0.7.3"
libraryDependencies += "dev.zio" %% "zio-http" % "3.0.1"

scalacOptions += "-Ymacro-annotations"

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerExposedPorts := Seq(8080)

dockerBaseImage := "openjdk:23-jdk"