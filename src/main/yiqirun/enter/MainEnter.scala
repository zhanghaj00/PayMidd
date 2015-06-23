package main.yiqirun.enter


import akka.actor.{PoisonPill, ActorRefFactory, Props, ActorSystem}
import akka.io.{Tcp, IO}
import akka.pattern.ask
import main.yiqirun.actor.UserSupervisor
import main.yiqirun.route.{ WsInterface, RootService, RestInterface}
import spray.can.Http
import spray.can.server.UHttp
import scala.concurrent.duration._
import akka.util.Timeout
/**
 * Created by zhanghao on 2015/6/11.
 *
 *
 */
object MainEnter extends App {

  implicit val system = ActorSystem("YiQiRun")

    val interface = system.actorOf(Props(new RootService()),"RootActor-RestInterface")

  implicit val timeout = Timeout(5.seconds)
  system.actorSelection("/user/IO-HTTP") ! PoisonPill
  IO(UHttp) ? Http.Bind(interface,"",9997)


  private def initializeSupervisors(system: ActorSystem) = {
    system.actorOf(UserSupervisor.props, "UserSupervisor")
   // system.actorOf(RoomSupervisor.props, "RoomSupervisor")
  }
}
