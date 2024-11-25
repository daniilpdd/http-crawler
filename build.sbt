ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "http-crawler"
  )

val zioVersion = "2.1.11"
libraryDependencies += "dev.zio" %% "zio" % zioVersion
libraryDependencies += "dev.zio" %% "zio-macros" % zioVersion

libraryDependencies += "dev.zio" %% "zio-http" % "3.0.1"

libraryDependencies += "dev.zio" %% "zio-json" % "0.7.3"

scalacOptions += "-Ymacro-annotations"