package main.websocket

import akka.actor.Props
import main.scala.{HttpServer, IndexService, StaticRoute, AbstractAkkaSystem}
import spray.http.HttpRequest
import spray.routing.RouteConcatenation
import spray.routing.directives.LogEntry

import scala.tools.util.SocketServer


/**
 * Created by zhanghao on 2015/6/3.
 */
trait Routes extends RouteConcatenation with StaticRoute with AbstractAkkaSystem {

  val httpServer = system.actorOf(Props(classOf[HttpServer], allRoutes))

 // val socketServer = system.actorOf(Props[SocketServer])


  lazy val index = system.actorOf(Props[IndexActor], "index")

  lazy val allRoutes = logRequest(showReq _) {
    new IndexService(index).route ~ staticRoute
  }

  implicit val wsocketServer: WSocketServer
  wsocketServer.forResource("/ws", Some(index))


  private def showReq(req: HttpRequest) = LogEntry(req.uri)
}
