package main.socket


import akka.actor._
import spray.http.StatusCodes
import spray.routing.{Directives, HttpService}


/**
 * Created by zhanghao on 2015/6/4.
 */

trait ReactiveApi extends StaticRoute with AbstractSystem{

  val system:ActorSystem

  val socketService = system.actorOf(Props[SocketService], "tcp")
}
trait StaticRoute extends Directives {
  this : AbstractSystem =>

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
      } ~ complete(StatusCodes.NotFound)
}
trait AbstractSystem {
  implicit def system : ActorSystem
}