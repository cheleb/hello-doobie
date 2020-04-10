addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.3")
addSbtPlugin("com.dwijnand"      % "sbt-dynver"      % "4.0.0")
addSbtPlugin("de.heikoseeberger" % "sbt-header"      % "5.4.0")

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.30" // Needed by sbt-git
