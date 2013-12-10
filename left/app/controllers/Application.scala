package controllers

import actors._;
import akka.actor.Actor
import akka.actor.Props
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

  def invokeActorFuture(id: Long) = Action {
    AsyncResult {
      implicit val timeout = Timeout(5.seconds)
      val myActor = Akka.system.actorOf(Props[HelloWorldFuture], name = "helloWorldFuture")
      //val future: Future[String] = ask(myActor, id).mapTo[String]
      val scalaFuture = (myActor ? id).mapTo[String]
      scalaFuture.map { test =>
        Ok(test)
      }
    }
  }
}
