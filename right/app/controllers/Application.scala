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
import org.fusesource.scalate._
import org.fusesource.scalate.util._
import java.io._

import collection.JavaConverters._
import de.neuland.jade4j._




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

  def actorInit():ActorSelection ={
    val config = ConfigFactory.load()
    val system = ActorSystem("right", config.getConfig("rightConfig"))
    //val system = ActorSystem("MySystem")
    system.actorSelection("akka.tcp://left@127.0.0.1:12552/user/helloWorldFuture")
  }

  //val theActor: Option[ActorRef] = None;
  val theActor = actorInit()

  // RIGHT
  def invokeActorFuture(id: Long) = Action {
    AsyncResult {
      implicit val timeout = Timeout(60.seconds)
      //val future: Future[String] = ask(myActor, id).mapTo[String]
      val localActor = theActor;
      val scalaFuture = (localActor ? id).mapTo[String]
      scalaFuture.map { test =>
        Ok(test)
      }
    }
  }


  ////////////////////
  // SCALATRA Stuff //
  ////////////////////
  val engine = new TemplateEngine
  engine.resourceLoader = new FileResourceLoader(List(new File("/tmp/jade")))
  engine.classpath = "tmp/classes"
  //engine.workingDirectory = new File("/tmp/jade")
  engine.workingDirectory = Play.getFile("tmp")
  //engine.combinedClassPath = true
  engine.classLoader = Play.classloader
  def testDynamicCompile() = Action {
    val sw = new StringWriter;
    val pw:PrintWriter = new PrintWriter(sw)
    engine.layout(uri="/index.jade", out=pw, attributes=Map(("title","Hello world!")))
    Ok(pw.toString)
  }

  def jade4j() = Action {
    val html = Jade4J.render("/tmp/jade/jade4j.jade":String, Map(("title","Hello world!":Object)).asJava:java.util.Map[String, Object]);
    Ok(html)
  }



}
