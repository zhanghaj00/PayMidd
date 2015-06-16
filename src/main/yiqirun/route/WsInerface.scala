package main.yiqirun.route


import akka.actor.{Props, ActorRef, Actor, ActorLogging}
import main.yiqirun.actor.RunActor
import main.yiqirun.domain.WebSocket
import spray.can.server.UHttp
import spray.can.websocket


import spray.can.websocket.{UpgradedToWebSocket, WebSocketServerWorker}
import spray.can.websocket.frame.{PingFrame, StatusCode, CloseFrame, TextFrame}
import spray.http.HttpRequest
import spray.routing._
import akka.util.Timeout
import scala.concurrent.duration._

/**
 * Created by zhanghao on 2015/6/12.
 */
class WsInterface  extends Actor with  ActorLogging  with HttpService with WebSocketServerWorker with WebSocket{

  import context.dispatcher


  def actorRefFactory = context

  val runActor = context.actorOf(Props[RunActor],"Run-Actor")


  lazy val serverConnection = sender

  implicit  val timeout = Timeout(1000 seconds)

    override  def receive = runRoute(routes) orElse handshaking orElse closeLogic

  def runRoute(route:Route):Receive = {
    case request : HttpRequest =>
     // log.info("get request ")
      val ctx = RequestContext(request, self, request.uri.path)
      log.info("get request ")
      log.info("HTTP request for uri {}", request.uri.path)
      route(ctx.withResponder(self))
      handshaking(request)
    case WebSocket.Register(request, actor, ping) =>
        println("websocket register......................")
     // if (ping) pinger = Some(context.system.scheduler.scheduleOnce(110.seconds, self, WebSocket.Ping))
      uripath = request.uri.path.toString
      val handler = actor
      handler ! WebSocket.Open(this)


  }
  def routes:Route = {
        implicit ctx =>
          println("ok get ou "+ctx)
          ctx.responder ! WebSocket.Register(ctx.request, serverConnection, true)

  }
  override  def handshaking: Receive = {
    // when a client request for upgrading to websocket comes in, we send
    // UHttp.Upgrade to upgrade to websocket pipelines with an accepting response.
    case websocket.HandshakeRequest(state) =>
      state match {
        case wsFailure: websocket.HandshakeFailure => sender ! wsFailure.response
        case wsContext: websocket.HandshakeContext => sender ! UHttp.UpgradeServer(websocket.pipelineStage(self, wsContext), wsContext.response)
      }

    // upgraded successfully
    case UHttp.Upgraded =>
      context.become(businessLogic orElse closeLogic)
      self ! websocket.UpgradedToWebSocket // notify Upgraded to WebSocket protocol


  }

  // this is the actor's behavior after the WebSocket handshaking resulted in an upgraded request
  override def businessLogic = {
    case TextFrame(message) =>
      println(message.utf8String)
      send(TextFrame("messaage ======get"))
    case UpgradedToWebSocket =>
      println("upgrade websoeket.......................")
      send(TextFrame("messaage ======upgrade "))

    case _ => println("11111111111111111111")
  }
  def send(message : String) = {
    println("send message"+message)
    send(TextFrame(message))
  }
  def close() = send(CloseFrame(StatusCode.NormalClose) )

  def path() = uripath
  private var uripath = "/"
}



/*class MyResponder(requestContext:RequestContext,actorRef:ActorRef) extends Actor with ActorLogging {

  import spray.httpx.SprayJsonSupport._

  def receive = {
    case _ =>
  }
}*/
