package main.yiqirun.enter

import java.net.InetSocketAddress

import akka.actor.{ActorRefFactory, Props, ActorSystem}
import akka.io.{Tcp, IO}
import com.typesafe.config.ConfigFactory
import main.yiqirun.route.{WsApi, WsInterface, RootService, RestInterface}
import spray.can.Http
import spray.can.server.UHttp

/**
 * Created by zhanghao on 2015/6/11.
 *
 * 这是程序的主入口
 */
class MainEnter extends App {

  implicit val system = ActorSystem("YiQiRun")



    val interface = system.actorOf(Props(new RootService[WsInterface]),"RootActor-RestInterface")


  IO(Http) ! Http.Bind(interface,"",9999)

  IO(Tcp) ! Tcp.Bind(interface,new InetSocketAddress("",9998))

  IO(UHttp) ! Http.Bind(interface,"",9997)
}

/*trait Configuration {

  val configure = ConfigFactory.load("/resource/application.conf")

  val port = configure.getString("")
}*/
