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
      log.info(s"Getting a room ${userRegister.user.email} for the date  .")
      if(context.child(userRegister.user.email).isEmpty){
        log.info("new user:"+userRegister.user.email)
        val userActor = context.actorOf(Props[UserActor],userRegister.user.email)
        userActor ! KeepConnection(userRegister.user.email)
        sender ! userRegister.user
      }else{
        log.info("not a new user"+userRegister.user.email)
        context.child(userRegister.user.email).get ! KeepConnection(userRegister.user.email)
        sender ! userRegister.user
      }
    case keeyConnection:KeepConnection => println("keep connection"+keeyConnection.userId)
    case user:User =>
    case _ => log.info("no thing ")
  }
}
case class UserRegister(user:User)
case class KeepConnection(userId:String)