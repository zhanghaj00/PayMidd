package main.yiqirun.actor

import akka.actor.{Props, Actor}
import main.yiqirun.domain.{MessageUser, Message, UserConnected}

/**
 * Created by zhanghao on 4/9/15.
 */

object UserSupervisor {
  def props = Props(new UserSupervisor)
}

class UserSupervisor extends Actor {

  override def receive = {
    case msg: UserConnected => context.actorOf(UserActor.props(msg.connection), "UserActor" + msg.id)
    case msg: Message => receivedMessage(msg)
  }

  def receivedMessage(message: Message) = {
    val messageUser = message.msg.asInstanceOf[MessageUser]
    context.actorSelection("UserActor*") ! messageUser
  }
}

