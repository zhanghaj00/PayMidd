package main.yiqirun.route


import akka.actor.{Props, ActorRef, Actor, ActorLogging}
import main.yiqirun.domain.WebSocket

import spray.can.websocket.WebSocketServerWorker
import spray.can.websocket.frame.{StatusCode, CloseFrame, TextFrame}
import spray.http.{ HttpRequest}
import spray.routing._
import akka.util.Timeout
import scala.concurrent.duration._
/**
 * Created by zhanghao on 2015/6/12.
 */
class WsInterface(sender:ActorRef)  extends Actor with  ActorLogging  with HttpService with WebSocketServerWorker with WebSocket{

  import context.dispatcher


  def actorRefFactory = context

  lazy val serverConnection = sender

  implicit  val timeout = Timeout(5 seconds)

  override  def receive = runRoute(routes) orElse handshaking orElse closeLogic

  def runRoute(route:Route):Receive = {


    case request : HttpRequest =>
      val ctx = RequestContext(request, self, request.uri.path)
      log.debug("HTTP request for uri {}", request.uri.path)
        route(ctx.withResponder(self))
      handshaking(request)
    case WebSocket.Register(request, actor, ping) =>
     // if (ping) pinger = Some(context.system.scheduler.scheduleOnce(110.seconds, self, WebSocket.Ping))
      val handler = actor
      uripath = request.uri.path.toString
      handler ! WebSocket.Open(this)
  }


  def routes:Route = {
    pathPrefix("find") {
      path("ws") {
        implicit ctx =>
          ctx.responder ! WebSocket.Register(ctx.request, serverConnection, true)
      }
    }
  }



  // this is the actor's behavior after the WebSocket handshaking resulted in an upgraded request
  override def businessLogic = {
    case TextFrame(message) =>
      println(message.utf8String)
  }
  def send(message : String) = send(TextFrame(message))
  def close() = send(CloseFrame(StatusCode.NormalClose))

  def path() = uripath
  private var uripath = "/"
}



class MyResponder(requestContext:RequestContext,actorRef:ActorRef) extends Actor with ActorLogging {

  import spray.httpx.SprayJsonSupport._

  def receive = {
    case _ =>
  }
}
