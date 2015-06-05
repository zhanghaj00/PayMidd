package main.camel

import akka.actor.{ActorRef, ActorSystem}
import akka.camel.{CamelMessage, Consumer, CamelExtension}

import scala.xml.XML

/**
 * Created by zhanghao on 2015/6/4.
 */


class OrderConsumer(uri:String,next:ActorRef) extends Consumer{

  def endpointUri = uri

  def receive = {
    case message:CamelMessage =>
      val content = message.bodyAs[String]
      val xml = XML.load(content)
      val order = xml \\ "order"
      val customer = (order \\ "customerId").text
      println(customer)
      //next ! new Order(customer)
  }
}

case class Order(customer:String)