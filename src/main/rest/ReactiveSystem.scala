package main.rest

import akka.actor.ActorSystem
import akka.io.IO
import spray.can.Http

/**
 * Created by zhanghao on 2015/6/5.
 */
object ReactiveSystem  extends App with ReactiveApi{
      implicit  lazy val system = ActorSystem("hello-Service")

      IO(Http) ! Http.Bind(socketService, "localhost", 9999)
}
