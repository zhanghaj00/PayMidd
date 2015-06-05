package main.spraywebsocket

import akka.actor._
import akka.io.Tcp.{Connected, Register, Connect, Bound}
import akka.io.{Tcp, IO}
import spray.can.websocket.FrameCommandFailed
import spray.can.websocket.frame.{TextFrame, BinaryFrame}
import spray.can.{Http, websocket}
import spray.http.HttpRequest
import spray.routing.HttpServiceActor

/**
 * Created by zhanghao on 2015/6/3.
 */
object SimpleServer extends App with MySslConfiguration {

  final case class Push(msg: String)

  object WebSocketServer {
    def props() = Props(classOf[WebSocketServer])
  }
  class WebSocketServer extends Actor with ActorLogging {

    def receive = {
     /* // when a new connection comes in we register a WebSocketConnection actor as the per connection handler
      case Http.Connected(remoteAddress, localAddress) =>
        println(remoteAddress)
        println(localAddress)
        val serverConnection = sender
        val conn = context.actorOf(WebSocketWorker.props(serverConnection))
        serverConnection ! Http.Register(conn)

*/    case b @ Bound(localAddress)=>
        println("ok get some thing first")

      case c @ Connected(remote,local) =>
        println("ok connect some thing escond")
        val handler = context.actorOf(Props[SimplisticHandler])
        val connection = sender
        connection ! Register(handler)

    }
  }
  class SimplisticHandler extends Actor {
    import Tcp._
    def receive = {
      case Received(data) =>
        println(data)
        sender ! Write(data)
      case PeerClosed     => context stop self
    }
  }
  object WebSocketWorker {
    def props(serverConnection: ActorRef) = Props(classOf[WebSocketWorker], serverConnection)
  }
  class WebSocketWorker(val serverConnection: ActorRef) extends HttpServiceActor with websocket.WebSocketServerWorker {
    override def receive = handshaking orElse businessLogicNoUpgrade orElse closeLogic

    def businessLogic: Receive = {
      // just bounce frames back for Autobahn testsuite
      case x @ (_: BinaryFrame | _: TextFrame) =>
        sender ! x

      case Push(msg) => send(TextFrame(msg))

      case x: FrameCommandFailed =>
        log.error("frame command failed", x)

      case x: HttpRequest => println(x.message)
     // case x:
    }

    def businessLogicNoUpgrade: Receive = {
      implicit val refFactory: ActorRefFactory = context
      runRoute {
        getFromResourceDirectory("webapp")
      }
    }
  }

  def doMain() {
    implicit val system = ActorSystem()
    import system.dispatcher

    val server = system.actorOf(WebSocketServer.props(), "websocket")

    IO(Http)  ! Http.Bind(server, "localhost", 8080)

    readLine("Hit ENTER to exit ...\n")
    system.shutdown()
    system.awaitTermination()
  }

  // because otherwise we get an ambiguous implicit if doMain is inlined
  doMain()
}
