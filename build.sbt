import Dependencies._

ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "etl-pipeline",
    libraryDependencies += munit % Test
  )

libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.17.2"

val circeVersion = "0.14.1"
libraryDependencies += "io.circe" %% "circe-parser" % circeVersion

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
