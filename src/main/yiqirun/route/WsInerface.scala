package main.yiqirun.route


import java.util.UUID

import akka.actor.{Props, ActorRef, Actor, ActorLogging}
import main.webs.UserActor
import main.yiqirun.actor.RunActor
import main.yiqirun.domain._
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

object WsInerface{
  def props(serverConnection: ActorRef) = Props(classOf[WsInterface],serverConnection)
}
class WsInterface(val serverConnection: ActorRef)  extends Actor with  ActorLogging  with HttpService with WebSocketServerWorker {

  val id = UUID.randomUUID();

  def actorRefFactory = context
  val userSupervisorActor = context.system.actorSelection(Supervisors.userSupervisor)

  override def businessLogic = {
    case websocket.UpgradedToWebSocket =>  userSupervisorActor ! UserConnected(id, self) // this 我们可以用redis来存放这些用户昵称和id
    case msg: TextFrame => {
     println( msg.payload.decodeString("UTF-8") )
    }
    case msg: MessageUser => send(TextFrame(msg.message.toUpperCase))
  }
  def receivedMessageUser(messageUser: MessageUser) = {
    userSupervisorActor ! Message(id, messageUser)
  }

  /*def receivedMessageRoom(messageRoom: MessageRoom) = {
    roomSupervisorActor ! Message(id, messageRoom)
  }*/
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
}
