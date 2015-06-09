package main.rest

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.typesafe.config.ConfigFactory
import spray.can.Http

/**
 * Created by zhanghao on 2015/6/8.
 */
object MainEnter extends App {

  implicit  val system = ActorSystem("my-rest-server")

  val actorEnter = system.actorOf(Props[RestInterface],"mySparyEnter")


  IO(Http)!Http.Bind(actorEnter,"127.0.0.1",9998)
}

object Configuration{

  val configure = ConfigFactory.load("/resource/application.conf")

  val port = configure.getString("")
}