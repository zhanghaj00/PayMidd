package main.scala

import akka.actor.Props


import spray.http.HttpRequest
import spray.routing.directives.LogEntry
import spray.routing.{Route, RouteConcatenation, Directives}



/**
 * Created by zhanghao on 2015/6/3.
 */
trait Routes extends RouteConcatenation with StaticRoute with AbstractAkkaSystem {

  val httpServer = system.actorOf(Props(classOf[HttpServer], allRoutes))

  implicit def routes: Route


  lazy val allRoutes = logRequest(showReq _) {
    routes ~ staticRoute
  }

  private def showReq(req: HttpRequest) = LogEntry(req.uri)
}


trait StaticRoute extends Directives {
  this: AbstractAkkaSystem =>

  lazy val staticRoute =
    path("favicon.ico") {
      getFromResource("favicon.ico")
    } ~
      pathPrefix("markers") {
        getFromResourceDirectory("markers/")
      } ~
      pathPrefix("css") {
        getFromResourceDirectory("css/")
      } ~
      pathEndOrSingleSlash {
        getFromResource("index.html")
      }
}