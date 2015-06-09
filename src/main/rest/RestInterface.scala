package main.rest

import akka.actor._
import akka.util.Timeout
import main.rest.Domain.User
import spray.http.StatusCodes
import scala.concurrent.duration._
import spray.httpx.SprayJsonSupport
import spray.routing._

import spray.json.DefaultJsonProtocol
import spray.httpx.unmarshalling._
/**
 * Created by zhanghao on 2015/6/8.
 */
class RestInterface extends Actor with RestApi{

  def actorRefFactory = context

  def receive =runRoute(routes)
}

trait RestApi extends HttpService with PersonJsonSupport{

    implicit def executionContext = actorRefFactory.dispatcher

    implicit val timeout = Timeout(10 seconds)


    val userManager = actorRefFactory.actorOf(Props[],"userManager")
    def routes:Route = {
        pathPrefix("userId" / IntNumber){
          userid=> {
            path("firstSigin") {
              get {
                requestContext =>
                  cookie("userId"){nameCookie=>
                    complete(StatusCodes.OK,nameCookie.content)

                  }
                  requestContext.complete(StatusCodes.OK, userid.toString)
              }
            }
          }
        }
    }


  def createResponder(requestContext: RequestContext, actorRef: ActorRef) = {
    actorRefFactory.actorOf(Props(classOf[MyResponder],requestContext, actorRef))
  }
}

class MyResponder(requestContext:RequestContext,actorRef:ActorRef) extends Actor with ActorLogging{

  def receive = {
    case user:User =>
      requestContext.complete(StatusCodes.OK,user)
      self ! PoisonPill
  }
}




trait PersonJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val PersonFormat = jsonFormat2(Order)
}
case class Order(name:String,number:Int)