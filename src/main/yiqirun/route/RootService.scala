package main.yiqirun.route

import akka.actor.{ActorRef, Props, ActorLogging, Actor}
import akka.routing.Route
import main.yiqirun.domain.WebSocket
import spray.can.Http
import scala.concurrent.duration._
import scala.reflect.ClassTag

/**
 * Created by zhanghao on 2015/6/12.
 */
class RootService[RA <: Actor](implicit tag : ClassTag[RA]) extends Actor with ActorLogging {
  implicit  val timeout = spray.http.SetRequestTimeout(5 seconds)

  override def receive = {
    case connected : Http.Connected =>
      // implement the "per-request actor" pattern
      println("ok connect")
      sender ! Http.Register(context.actorOf(Props(tag.runtimeClass)))
    case whatever => log.debug("RootService got some {}", whatever)
  }
}
