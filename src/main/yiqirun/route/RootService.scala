package main.yiqirun.route

import akka.actor.{ActorRef, Props, ActorLogging, Actor}
import akka.routing.Route
import main.yiqirun.domain.WebSocket
import spray.can.Http
import spray.can.Http.Register
import scala.concurrent.duration._
import scala.reflect.ClassTag

/**
 * Created by zhanghao on 2015/6/12.
 */
class RootService extends Actor with ActorLogging {
  implicit  val timeout = spray.http.SetRequestTimeout(5 seconds)

  override def receive = {
    case connected : Http.Connected =>
      // implement the "per-request actor" pattern
      println("ok connect+"+connected.remoteAddress+"dfsdfsdf"+connected.localAddress)
      val handler = context.actorOf(WsInerface.props(sender))
      sender ! Register(handler)
    case whatever => log.debug("RootService got some {}", whatever)
  }
}
