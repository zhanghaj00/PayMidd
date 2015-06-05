package main.rest


import akka.actor.{Props, ActorLogging, Actor}
import akka.io.Tcp

/**
 * Created by zhanghao on 2015/6/5.
 */
class SocketService extends Actor with ActorLogging{

  val sender1 = context.actorOf(Props[SocketActor])
  override def receive = {
    case Tcp.CommandFailed(_ : Tcp.Bind) =>
      context stop self
    case Tcp.Connected(remote, local) =>
      // implement the "per-request actor" pattern
      println(remote)
      sender1 ! Tcp.Register(context.actorOf(Props(classOf[SocketActor])))
    case Tcp.Received(data)=>
      println(data)
  }

}
