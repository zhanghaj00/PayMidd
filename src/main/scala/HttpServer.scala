package main.scala

import akka.actor.{Actor, ActorLogging}

import spray.routing.{Route, HttpService}

/**
 * Created by zhanghao on 2015/6/3.
 */
class HttpServer(route: Route)  extends Actor with HttpService with ActorLogging {

  implicit def actorRefFactory = context

  override def receive = runRoute(route)
}
