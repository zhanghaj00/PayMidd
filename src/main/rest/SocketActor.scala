package main.rest

import java.net.InetSocketAddress


import akka.actor.{ActorRef, Props, Actor, ActorLogging}

import akka.io.Tcp

/**
 * Created by zhanghao on 2015/6/5.
 */
class SocketActor(connection:ActorRef,remote: InetSocketAddress)  extends Actor with ActorLogging{

  val maker = context.actorOf(Props[OtherActor],"hello1")

  context watch connection

  //case object Ack extends Event
  def receive = {

    case Tcp.Received(data) =>
      println("not in")
      println(data.utf8String)
      //ask(maker,Order(1,"ok"))
      maker ! Order(1,"ok")
      println("send back data ")
      connection ! Tcp.Write(data)
    case Tcp.Closed =>
      context stop self

    case _ =>
      println("nothing......")
  }

}
