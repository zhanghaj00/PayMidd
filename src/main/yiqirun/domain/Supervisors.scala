package main.yiqirun.domain

import java.util.UUID

import akka.actor.ActorRef

/**
 * Created by zhanghao on 2015/6/23.
 */
object Supervisors {
  private def path(name: String) = "user/" + name

  def userSupervisor = path("UserSupervisor")

  def roomSupervisor = path("RoomSupervisor")
}
case class Message(id: UUID, msg: Any)
case class UserConnected(id: UUID, connection: ActorRef)

/*object MessageUser extends JsonMapper {
  def unapply(json: String): Option[MessageUser] = {
    try{
      val messageUser = objectMapper.readValue(json, classOf[MessageUser])
      Some(messageUser)
    }catch {
      case _ => None
    }
  }
}*/
case class MessageUser(fromUserId: Int, toUserId: Int, message: String)