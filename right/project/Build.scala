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
    "com.typesafe.akka" % "akka-remote_2.10" % "2.2.3"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
