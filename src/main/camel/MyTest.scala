/*
package main.camel

import java.io.PrintWriter
import java.net.Socket

import akka.actor.{Props, ActorSystem}
import akka.camel.CamelExtension
import akka.util.Timeout

import scala.actors.threadpool.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration


/**
 * Created by zhanghao on 2015/6/4.
 */
object MyTest extends App {

  implicit val timeout1 = Timeout(5)
  val time1 = Duration.create("5")
  val system = ActorSystem("init-system")
  val probe = system.actorOf(Props[MyActor],"hello")

  val camelUri = "mina:tcp://localhost:8888?textline=true"

  val consumer = system.actorOf(Props(new OrderConsumer(camelUri,probe)))

  val activated = CamelExtension(system).activationFutureFor(consumer)(timeout=timeout1,executor = system.dispatcher)
  Await.result(activated,time1)

  val msg = Order("ms")

  val xml = <order>
              <customer>{msg.customer}</customer>
            </order>

  val xmlStr = xml.toString.replace("n","")
  val socket = new Socket("localhost",8888)

  val outputWriter = new PrintWriter(socket.getOutputStream,true)
  outputWriter.println(xmlStr)
  outputWriter.flush()

  outputWriter.close()
  system.stop(consumer)



}
*/
