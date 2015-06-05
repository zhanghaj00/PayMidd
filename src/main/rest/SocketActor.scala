package main.rest

import akka.actor.{Props, Actor, ActorLogging}
import akka.io.Tcp

/**
 * Created by zhanghao on 2015/6/5.
 */
class SocketActor  extends Actor with ActorLogging{

  val maker = context.actorOf(Props[OtherActor],"hello1")

  def receive = {

    case Tcp.Received(data) =>
      val da = data.toString()

      println(da)
      //ask(maker,Order(1,"ok"))
      maker ! Order(1,"ok")

    case Tcp.Closed =>
      context stop self

    case _ =>
      println("nothing......")
  }

}
