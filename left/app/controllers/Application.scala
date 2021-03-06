package controllers

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



object Application extends Controller {

  val taskForm = Form(
    "label" -> nonEmptyText
  )

  def index = Action {
    //Ok(views.html.index("Your new application is ready."))
    //Ok("hello world")
    Redirect( routes.Application.tasks() )
    //Ok(views.html.index(Task.all(), taskForm))
  }

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.create(label)
        Redirect(routes.Application.tasks)
      }
      )
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  def invokeActor(id: Long) = Action {
    val myActor = Akka.system.actorOf(Props[HelloWorld], name = "helloWorld")
    Ok("hi");
  }

  def invokeActorFutureLocal(id: Long) = Action {
    AsyncResult {
      implicit val timeout = Timeout(5.seconds)
      val config = ConfigFactory.load()
      //val app1 = ActorSystem("MyApp1", config.getConfig("myapp1").withFallback(config))
      val system = ActorSystem("MySystem", config.getConfig("MySystem"))
    //println(system.settings);
    val myActor = Akka.system.actorOf(Props[HelloWorldFuture], name = "helloWorldFuture")
    //val future: Future[String] = ask(myActor, id).mapTo[String]
    val scalaFuture = (myActor ? id).mapTo[String]
    myActor.tell(PoisonPill, myActor);
    scalaFuture.map { test =>
      Ok(test)
    }
    }
  }

  def actorInit():ActorSelection ={
      val config = ConfigFactory.load()
      val system = ActorSystem("left")
      //DOESNT WORK - ADDRESS BIND val system = ActorSystem("left", config.getConfig("leftConfig"))
      //DOESNT WORK val system = ActorSystem("MySystem")
      //DOESNT WORK DEAD LETTER val myActor = system.actorSelection("akka://left@127.0.0.1:12552/user/helloWorldFuture")
      system.actorSelection("/user/helloWorldFuture")
  }

  //val theActor: Option[ActorRef] = None;
  val theActor = actorInit()

  // LEFT
  def invokeActorFuture(id: Long) = Action {
    AsyncResult {
      implicit val timeout = Timeout(60.seconds)
      //val future: Future[String] = ask(myActor, id).mapTo[String]
      val scalaFuture = (myActor ? id).mapTo[String]
      scalaFuture.map { test =>
        Ok(test)
      }
    }
  }
}
