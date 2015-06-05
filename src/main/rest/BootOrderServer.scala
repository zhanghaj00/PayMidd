/*
package main.rest

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.io.IO
import main.scala.Configuration
import main.websocket.{WSocketServer, Routes}
import spray.can.Http

/**
 * Created by zhanghao on 2015/6/5.
 */
/*object BootOrderServer extends App with Routes{

  implicit lazy val system = ActorSystem("server-system")


  var service = system.actorOf(Props{new OrderServiceActor(orderSystem)},"myservice")
 /* implicit lazy val wsocketServer = new WSocketServer(Configuration.portWs)

  wsocketServer.start
  sys.addShutdownHook({
    system.shutdown
    wsocketServer.stop
  })
*/
  IO(Http) ! Http.Bind(service, Configuration.host, port = Configuration.portHttp)


}*/
class OrderHttpServer(host:String,portNr:Int,orderSystem:ActorRef) {
  implicit lazy val system = ActorSystem("server-system")
  var service = system.actorOf(Props{new OrderServiceActor(orderSystem)},"myservice")

  IO(Http) ! Http.Bind(service, host, port = portNr)
}*/
