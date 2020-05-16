import Dependencies._
import scala.collection.immutable.Seq

lazy val lwjglVersion = "3.2.1"

lazy val os = Option(System.getProperty("os.name", ""))
  .map(_.substring(0, 3).toLowerCase) match {
  case Some("win") => "windows"
  case Some("mac") => "macos"
  case _           => "linux"
  }

name := "scala-lwjgl"

ThisBuild / scalaVersion     := "2.13.2"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.nloyola"
ThisBuild / organizationName := "scala-lwjgl"

lazy val root = (project in file("."))
  .settings(
    name := "scala-jwjgl",
    libraryDependencies ++= Seq(
        scalaTest % Test,
        "org.lwjgl" % "lwjgl"        % lwjglVersion,
        "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,
        "org.lwjgl" % "lwjgl-glfw"   % lwjglVersion,
        "org.lwjgl" % "lwjgl-stb"    % lwjglVersion,
        "org.lwjgl" % "lwjgl-assimp" % lwjglVersion,
        "org.lwjgl" % "lwjgl-nanovg" % lwjglVersion,
        "org.lwjgl" % "lwjgl"        % lwjglVersion classifier s"natives-$os",
        "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier s"natives-$os",
        "org.lwjgl" % "lwjgl-glfw"   % lwjglVersion classifier s"natives-$os",
        "org.lwjgl" % "lwjgl-stb"    % lwjglVersion classifier s"natives-$os",
        "org.lwjgl" % "lwjgl-assimp" % lwjglVersion classifier s"natives-$os",
        "org.lwjgl" % "lwjgl-nanovg" % lwjglVersion classifier s"natives-$os"
      ),

    scalacOptions ++= Seq(
        "-deprecation",
        "-encoding", "UTF-8",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-unchecked",
        "-Xfatal-warnings",
        "-Xlint:_,-missing-interpolator",
        "-Ywarn-dead-code",
        "-Ywarn-numeric-widen",
        "-Ywarn-value-discard",
        "-Yrangepos",
        "-target:jvm-1.8"
      ),

    javaOptions ++= {
      if (os == "macos")
        Seq("-XstartOnFirstThread")
      else
        Nil
    },

    fork in run := true
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
