package main.rest

import akka.actor.{Props, Actor, ActorLogging}
import main.rest.Domain.User
import scala.concurrent.duration._
import akka.util.Timeout
/**
 * Created by zhanghao on 2015/6/9.
 */

class UserActor extends Actor with ActorLogging{

  implicit val timeout = Timeout(5 seconds)

  def receive = {

    case userRegister:UserRegister =>
      log.info(s"Getting a room ${userRegister.userId} for the date  .")
      if(context.child(userRegister.userId).isEmpty){
        log.info("new user:"+userRegister.userId)
        val userActor = context.actorOf(Props[UserActor],userRegister.userId)
        userActor ! KeepConnection
        sender ! User("414.80606",userRegister.userId,"pasworld")
      }else{
        context.child(userRegister.userId).get ! KeepConnection
        sender ! User("414.80608",userRegister.userId,"pasworld")
      }
    case keeyConnection:KeepConnection => println("keep connection")

  }
}
case class UserRegister(userId:String)
case class KeepConnection(userId:String)