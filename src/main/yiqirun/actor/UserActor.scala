package main.yiqirun.actor

import akka.actor.{Actor, Props, ActorRef}
import main.yiqirun.domain.MessageUser

/**
 * Created by zhanghao on 2015/6/23.
 */
object UserActor {
  def props(connection: ActorRef) = Props(new UserActor(connection))
}

class UserActor(connection: ActorRef) extends Actor {
  override def receive = {
    case msg: MessageUser => receivedMessageUser(msg)
  }

  def receivedMessageUser(messageUser: MessageUser) = {
    connection ! messageUser
  }
}
