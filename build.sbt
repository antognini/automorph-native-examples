// Project
val projectName = "automorph-native-examples"
ThisBuild / organization := "ch.produs"
ThisBuild / organizationName := "produs ag"
ThisBuild / organizationHomepage := None
ThisBuild / description := "Automorph examples for GraalVM native image"
ThisBuild / homepage := None
ThisBuild / licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / versionScheme := Some("semver-spec")
ThisBuild / developers := List(
  Developer(
    id = "LA",
    name = "Luigi Antognini",
    email = "",
    url = url("https://github.com/antognini")
  )
)
ThisBuild / scmInfo := Some(
  ScmInfo(
    url(s"https://github.com/antognini/$projectName"),
    s"scm:git@github.com:antognini/$projectName.git"
  )
)
Global / onChangedBuildSource := ReloadOnSourceChanges


// Compile
ThisBuild / scalaVersion := "3.3.1"
ThisBuild / Compile / scalacOptions := Seq(
  "-encoding", "utf8",
  "-deprecation",
  "-language:scala3",
  "-new-syntax",
  "-indent",
  "-language:strictEquality",
  "-java-output-version", "11",
//  "-Yimports:scala,scala.Predef,java.lang,equality"
//  "-Wunused:all"
)


lazy val root = project.in(file(".")).dependsOn(examples).settings(
  name := projectName,
  publish / skip := true
).aggregate(examples, examples)

lazy val examples = project.in(file("examples"))
  .enablePlugins(NativeImagePlugin)
  .settings(
  Compile / mainClass := Some("examples.rpcServer"),
  nativeImageInstalled := true,
  nativeImageGraalHome := java.nio.file.Paths.get(System.getProperty("user.home"), "graalvm"),
  nativeImageOptions ++=
    List(
      "--no-fallback",
      s"--parallelism=${java.lang.Runtime.getRuntime.availableProcessors}",
      s"-H:ConfigurationFileDirectories=${baseDirectory.value / "native-image-config" }"
    ),
  name := projectName,
  libraryDependencies ++= Seq(
    "ch.produs" %% "type-safe-equality" % "0.6.0",
    "org.automorph" %% "automorph-default" % "0.2.3"
  )
)