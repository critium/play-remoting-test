import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "todolist"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "com.typesafe.akka" % "akka-remote_2.10" % "2.2.3",
    "org.fusesource.scalate" % "scalate-core_2.10" % "1.6.1",
    "de.neuland-bfi" % "jade4j" % "0.4.0"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += "usesource.m2" at "http://repo.fusesource.com/nexus/content/repositories/public"
  )

}
