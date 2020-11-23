import sbt._
import Keys._

object Dependencies {
  val doobieVersion = "0.9.2"
   val logbackVersion = "1.2.3"
  val doobie = Seq(
    libraryDependencies ++= Seq(
      // Start with this one
      "org.tpolecat" %% "doobie-core"      % doobieVersion,
      // And add any of these as neede
      "org.tpolecat" %% "doobie-h2"        % doobieVersion,          // H2 driver 1.4.197 + type mappings.
      "org.tpolecat" %% "doobie-hikari"    % doobieVersion,          // HikariCP transactor.
      "org.tpolecat" %% "doobie-postgres"  % doobieVersion,          // Postgres driver 42.2.5 + type mappings.
      "org.tpolecat" %% "doobie-scalatest" % doobieVersion % "test"  // ScalaTest support for typechecking statements.
    )
  )
  val logging = Seq(
      libraryDependencies ++= Seq(
        "ch.qos.logback"        % "logback-classic" % logbackVersion
      )
    )
}