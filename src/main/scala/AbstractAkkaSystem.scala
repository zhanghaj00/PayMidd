package main.scala

import akka.actor.ActorSystem

/**
 * Created by zhanghao on 2015/6/3.
 */
trait AbstractAkkaSystem {
  implicit def system: ActorSystem
}
