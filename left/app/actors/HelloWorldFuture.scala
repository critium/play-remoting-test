package actors;

import akka.actor.Actor
import akka.actor.Props

class HelloWorldFuture extends Actor {
  def receive = {
    case i:Long => {
      printf("hi im running: " + i)
      sender ! ( "Hello World from the Future " + i )
    }
  }
}
