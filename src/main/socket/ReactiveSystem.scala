package main.socket

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.io.IO
import spray.can.Http
import akka.io.Tcp

/**
 * Created by zhanghao on 2015/6/5.
 */
object ReactiveSystem  extends App with ReactiveApi{
      implicit  lazy val system = ActorSystem("hello-Service")

      IO(Tcp) ! Tcp.Bind(socketService, new InetSocketAddress("localhost", 9999))
}
