package main.rest

import akka.actor._
import akka.util.Timeout
import main.rest.Domain.User
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport
import spray.httpx.unmarshalling.FormDataUnmarshallers
import scala.concurrent.duration._
import spray.routing._
import spray.json.DefaultJsonProtocol
import akka.pattern.pipe
import akka.pattern.ask


/**
 * Created by zhanghao on 2015/6/8.
 */
class RestInterface extends Actor with RestApi with FormDataUnmarshallers {

  def actorRefFactory = context

  def receive =runRoute(routes)
}

trait RestApi extends HttpService with FormDataUnmarshallers {

    implicit def executionContext = actorRefFactory.dispatcher

    implicit val timeout = Timeout(10 seconds)


    val userManager = actorRefFactory.actorOf(Props(classOf[UserActor]),"userManager")


    import spray.httpx.SprayJsonSupport._
    def routes:Route = {
      path("firstSigin") {
        post {
          entity(as[User]) { user =>
            requestContext =>
              println(user)
              val responder = createResponder(requestContext, userManager)
              userManager.ask(UserRegister(user)).pipeTo(responder)
          }
        }
      }~
        pathPrefix("email" / Segment){
          email=>{
            pathPrefix("userId" / Segment){
              userId=>{
                get{
                  path("passWord" / Segment){
                    passWord=> requestContext=>
                      val user = User(email,userId,passWord)
                      val responder = createResponder(requestContext,userManager)
                      userManager.ask(UserRegister(user)).pipeTo(responder)
                  }
                }
              }
            }
          }
        }


    }


  def createResponder(requestContext: RequestContext, actorRef: ActorRef) = {
    actorRefFactory.actorOf(Props(classOf[MyResponder],requestContext, actorRef))
  }
}

class MyResponder(requestContext:RequestContext,actorRef:ActorRef) extends Actor with ActorLogging {
  import spray.httpx.SprayJsonSupport._
  def receive = {
    case user:User =>
      requestContext.complete(StatusCodes.OK,user)
      self ! PoisonPill
  }
}

/*
object PersonJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val userFormat = jsonFormat3(User)
}
*/

case class Order(name:String,number:Int)