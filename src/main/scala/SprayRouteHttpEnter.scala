package main.scala

import akka.actor.Actor
import spray.routing.{HttpServiceActor, HttpService}

/**
 * Created by zhanghao on 2015/6/2.
 */
object SprayRouteHttpEnter extends HttpServiceActor {


  def receive = runRoute{

    path("hello"){
      get{
        complete{
          <h1>hello</h1>
        }
      }
    }
  }
}
