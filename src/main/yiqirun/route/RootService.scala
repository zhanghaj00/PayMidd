package main.yiqirun.route

import akka.actor.{ActorRef, Props, ActorLogging, Actor}
import akka.routing.Route
import spray.can.Http

import scala.reflect.ClassTag

/**
 * Created by zhanghao on 2015/6/12.
 */
class RootService[RA <: ActorRef](implicit tag : ClassTag[RA]) extends Actor with ActorLogging {
  override def receive = {
    case connected : Http.Connected =>
      // implement the "per-request actor" pattern
          sender ! Http.Register(context.actorOf(Props(tag.runtimeClass, sender)))
    case whatever => log.debug("RootService got some {}", whatever)
  }
}
