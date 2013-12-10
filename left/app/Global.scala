
import play.api._
import actors._;
import com.typesafe.config._
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import models.Task
import play.api.Play.current
import play.api._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.concurrent._
import play.api.libs.iteratee._
import play.api.mvc._
import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._

// This file is named Global.scala in the root namespace
object Global extends GlobalSettings {
 override def onStart(app: Application) {
    Logger.info("[+] Initializing database sessions...")
    Logger.info("[+] Initializing Other Stuff like the actors here...")

    implicit val timeout = Timeout(5.seconds)
    val config = ConfigFactory.load()
    val system = ActorSystem("left", config.getConfig("leftConfig"))
    val myActor = system.actorOf(Props[HelloWorldFuture], name = "helloWorldFuture")

    //val system2 = ActorSystem("MySystem")
    //val greeter = system2.actorOf(Props[GreetingActor], name = "greeter")
  }
}

//case class Greeting(who: String) extends Serializable

//class GreetingActor extends Actor with ActorLogging {
  //def receive = {
    //case Greeting(who) ⇒ log.info("Hello " + who)
  //}
//}
