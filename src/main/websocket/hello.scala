package main.websocket

import java.net.InetSocketAddress
import java.nio.ByteBuffer

import akka.actor.ActorRef
import org.java_websocket.{WebSocketImpl, WebSocket}
import org.java_websocket.framing.CloseFrame
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

/**
 * Created by zhanghao on 2015/6/3.
 */
object WSocketServer {

  sealed trait WSocketServerMessage

  case class Message(ws: WebSocket, msg: String)
    extends WSocketServerMessage

  case class BufferMessage(ws: WebSocket, buffer: ByteBuffer)
    extends WSocketServerMessage

  case class Open(ws: WebSocket, hs: ClientHandshake)
    extends WSocketServerMessage

  case class Close(ws: WebSocket, code: Int, reason: String, external: Boolean)
    extends WSocketServerMessage

  case class Error(ws: WebSocket, ex: Exception)
    extends WSocketServerMessage

}

class WSocketServer(val port: Int)
  extends WebSocketServer(new InetSocketAddress(port)) {
  private var reactors = Map[String, ActorRef]()

  final def forResource(descriptor: String, reactor: Option[ActorRef]) {
    reactor match {
      case Some(actor) => reactors += ((descriptor, actor))
      case None => reactors -= descriptor
    }
  }

  final override def onMessage(ws: WebSocket, msg: String) {
    if (null != ws) {
      reactors.get(ws.getLocalSocketAddress.getHostName) match {
        case Some(actor) => actor ! WSocketServer.Message(ws, msg)
        case None => ws.close(CloseFrame.REFUSE)
      }
    }
  }

  final  def onMessage(ws: WebSocketImpl, buffer: ByteBuffer) {
    if (null != ws) {
      reactors.get(ws.getLocalSocketAddress.getHostName) match {
        case Some(actor) => actor ! WSocketServer.BufferMessage(ws, buffer)
        case None => ws.close(CloseFrame.REFUSE)
      }
    }
  }

  final override def onOpen(ws: WebSocket, hs: ClientHandshake) {
    if (null != ws) {
      val actor = reactors.get(hs.getResourceDescriptor)
      actor.get ! WSocketServer.Open(ws, hs)
    }
  }

  final override def onClose(ws: WebSocket, code: Int, reason: String, external: Boolean) {
    if (null != ws) {
      reactors.get(ws.getLocalSocketAddress.getHostName) match {
        case Some(actor) => actor ! WSocketServer.Close(ws, code, reason, external)
        case None => ws.close(CloseFrame.REFUSE)
      }
    }
  }

  final override def onError(ws: WebSocket, ex: Exception) {
    if (null != ws) {
      reactors.get(ws.getLocalSocketAddress.getHostName) match {
        case Some(actor) => actor ! WSocketServer.Error(ws, ex)
        case None => ws.close(CloseFrame.REFUSE)
      }
    }
  }
}
