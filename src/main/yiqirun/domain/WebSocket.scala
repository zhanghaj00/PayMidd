package main.yiqirun.domain

import akka.actor.ActorRef
import spray.http.HttpRequest

/**
 * Created by zhanghao on 2015/6/12.
 */
object WebSocket {
  sealed trait WebSocketMessage
  case class Open(ws : WebSocket) extends WebSocketMessage
  case class Message(ws : WebSocket, msg : String) extends WebSocketMessage
  case class Close(ws : WebSocket, code : Int, reason : String) extends WebSocketMessage
  case class Error(ws : WebSocket, reason : String) extends WebSocketMessage
  case class Connect(host : String, port : Int, resource : String, withSsl : Boolean = false) extends WebSocketMessage
  case class Send(msg : String) extends WebSocketMessage
  case object Release extends WebSocketMessage
  case class Register(request : HttpRequest, handler : ActorRef, userID:String)
  private[main] object Ping extends WebSocketMessage
}
trait WebSocket {
  def send(message : String) : Unit
  def close() : Unit
  def path() : String
}
