// *****************************************************************************
// Projects
// *****************************************************************************

//scalafixDependencies in ThisBuild += "org.scalatest" %% "autofix" % "3.1.0.0" 

lazy val `hello-doobie` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(settings)
    .settings(Dependencies.doobie)
    .settings(Dependencies.logging)
    .settings(
      libraryDependencies ++= Seq(
        library.protobuf,
        library.calciteDruid
      )
    )
    .settings(
      libraryDependencies ++= Seq(
        library.scalaCheck % Test,
        library.scalaTest  % Test,
        library.pgEmbedded % Test
      )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val scalaCheck = "1.15.1"
      val scalaTest      = "3.2.3"
      val pgEmbedded = "0.13.3"
    }
    val calciteDruid =  "org.apache.calcite" % "calcite-druid" % "1.26.0"
    val protobuf = "com.google.protobuf" % "protobuf-java" % "3.14.0"
    val scalaCheck = "org.scalacheck" %% "scalacheck" % Version.scalaCheck
    val scalaTest      = "org.scalatest"    %% "scalatest"      % Version.scalaTest
    val pgEmbedded = "com.opentable.components" % "otj-pg-embedded" % Version.pgEmbedded,
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  scalafmtSettings ++
  scalafixSettings

lazy val scalafixSettings = Seq(
  //addCompilerPlugin(scalafixSemanticdb)
)

lazy val commonSettings =
  Seq(
    scalaVersion := "2.13.4",
    organization := "tv.teads",
    organizationName := "Olivier NOUGUIER",
    startYear := Some(2018),
    licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-Ywarn-unused",
      "-feature",
      "-language:higherKinds"
    )
//    Compile / compile / wartremoverWarnings ++= Warts.unsafe
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true
  )
