package main.rest


import akka.actor.{Props, ActorLogging, Actor}
import akka.io.Tcp
import akka.io.Tcp._
import main.spraywebsocket.SimpleServer.SimplisticHandler

/**
 * Created by zhanghao on 2015/6/5.
 */
class SocketService extends Actor with ActorLogging{

 // val sender1 = context.actorOf(Props[SocketActor])
  override def receive = {
    case Tcp.CommandFailed(_ : Tcp.Bind) =>
      context stop self
    case Tcp.Connected(remote, local) =>
      // implement the "per-request actor" pattern
      println("this is TCP:CONNECTED"+remote)
      sender ! Tcp.Register(context.actorOf(Props(classOf[SocketActor],sender,remote)),keepOpenOnPeerClosed = true)
    case Tcp.Received(data)=>
      println("THIS IS RECEIVEDdata:"+data.toString())


  /* case b @ (localAddress) => println(localAddress)
   // do some logging or setup ...

   case CommandFailed(_: Bind) => context stop self

   case Tcp.Connected(remote, local) =>
     println("hello")
     val handler = context.actorOf(Props(classOf[SocketActor],sender))
     val connection = sender
     connection ! Register(handler)*/
  }

}
