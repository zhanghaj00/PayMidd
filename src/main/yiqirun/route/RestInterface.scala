package main.yiqirun.route

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import akka.util.Timeout
import com.sun.xml.internal.bind.v2.TODO

import spray.routing._
import scala.concurrent.duration._
/**
 * Created by zhanghao on 2015/6/11.
 */
class RestInterface extends Actor with ActorLogging with RestApi{

  def actorRefFactory = context

  def receive =runRoute(routes)
}

trait RestApi extends HttpService{

  implicit def executionContext = actorRefFactory.dispatcher

  implicit val timeout = Timeout(10 seconds)


  def routes:Route = {
    case _ =>
  }
  def createResponder(requestContext: RequestContext, actorRef: ActorRef) = {
    actorRefFactory.actorOf(Props(classOf[MyResponder],requestContext, actorRef))
  }
}

class MyResponder(requestContext:RequestContext,actorRef:ActorRef) extends Actor with ActorLogging {
  import spray.httpx.SprayJsonSupport._
  def receive = {
    case _ =>
  }
}