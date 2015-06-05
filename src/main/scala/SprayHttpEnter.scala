package main.scala

import akka.actor.{Props, ActorSystem}
import spray.routing.SimpleRoutingApp

/**
 * Created by zhanghao on 2015/6/2.
 */
object SprayHttpEnter extends App with SimpleRoutingApp{

  implicit val system = ActorSystem("my-system")

 // val actor = system.actorOf(Props[SprayRouteHttpEnter])
  startServer(interface="localhost",port=9999){
    path("hello" / Segment){ x=>
      println("")
      get{
        complete{
          <h1>say hello</h1>
        }
      }
    }
  }
}
