package actors;

import akka.actor.Actor
import akka.actor.Props

object Greeter {
  case object Greet
  case object Done
}

class Greeter extends Actor {
  def receive = {
    case Greeter.Greet =>
      println("Hello World!")
      sender ! Greeter.Done
  }
}
