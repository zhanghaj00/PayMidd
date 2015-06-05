package main.websocket

import akka.actor.ActorSystem
import akka.io.IO
import main.scala.Configuration
import spray.can.Http

/**
 * Created by zhanghao on 2015/6/3.
 */
object Server extends App with Routes {

  implicit lazy val system = ActorSystem("server-system")

  implicit lazy val wsocketServer = new WSocketServer(Configuration.portWs)

  wsocketServer.start
  sys.addShutdownHook({
    system.shutdown
    wsocketServer.stop
  })

  IO(Http) ! Http.Bind(httpServer, Configuration.host, port = Configuration.portHttp)
  // IO(Tcp) ! Tcp.Bind(socketServer, new InetSocketAddress(Configuration.host, Configuration.portTcp))
}
