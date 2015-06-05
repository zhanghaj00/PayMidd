package main.rest

import akka.actor.{Actor, ActorLogging}

/**
 * Created by zhanghao on 2015/6/5.
 */
class OtherActor extends Actor with ActorLogging{

  def receive = {

    case order:Order=>
      println("get number of name:"+order.number+":"+order.name)

  }

}

case class Order(number:Int,name:String )