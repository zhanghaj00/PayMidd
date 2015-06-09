package main.rest

import akka.actor.{Props, Actor, ActorLogging}
import main.rest.Domain.User
import scala.concurrent.duration._
import akka.util.Timeout
/**
 * Created by zhanghao on 2015/6/9.
 */
object UserActor{
  def apply(userId:String):Props={
    Props(classOf[UserActor],userId)
  }
}
class UserActor(userId:String ) extends Actor with ActorLogging{

  implicit val timeout = Timeout(5 seconds)

  def receive = {

    case userRegister:UserRegister =>
      log.info(s"Getting a room ${userRegister.userId} for the date  .")
      if(context.child(userRegister.userId).isEmpty){
        val userActor = context.actorOf(UserActor(userRegister.userId))
        userActor ! KeepConnection
      }else{
        context.child(userRegister.userId).get ! KeepConnection
      }
    case keeyConnection:KeepConnection =>

  }
}
case class UserRegister(userId:String)
case class KeepConnection(userId:String)