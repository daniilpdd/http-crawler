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

libraryDependencies += "dev.zio" %% "zio-http" % "3.0.1"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test" % "2.1.13" % Test,
  "dev.zio" %% "zio-test-sbt" % "2.1.13" % Test,
  "dev.zio" %% "zio-test-magnolia" % "2.1.13" % Test
)

libraryDependencies += "dev.zio" %% "zio-json" % "0.7.3"

scalacOptions += "-Ymacro-annotations"