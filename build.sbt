name := "http-crawler"

version := "0.1"

scalaVersion := "2.13.15"

triggeredMessage := Watched.clearWhenTriggered

autoStartServer := false

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-language:implicitConversions",
  "-language:higherKinds"
)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

libraryDependencies += "dev.zio" %% "zio" % "2.1.13"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.1"