package main.scala

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.typesafe.config.ConfigFactory
import spray.can.Http

/**
 * Created by zhanghao on 2015/6/3.
 */
object Configuration  {

  private val config = ConfigFactory.load("resource/application.conf")

  val host = config.getString("akka.actor.provider")
  val portHttp = config.getInt("akka.actor.portHttp")
  val portTcp = config.getInt("akka.actor.portTcp")
  val portWs = config.getInt("akka.actor.portWs")

}





object Server extends App with Routes {
  implicit lazy val system = ActorSystem("server-system")
  lazy val index = system.actorOf(Props[IndexActor], "index")

  implicit lazy val routes = {
    new IndexService(index)(system).route
  }

  IO(Http) ! Http.Bind(httpServer, Configuration.host,port = Configuration.portHttp)
}
