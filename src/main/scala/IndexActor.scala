package main.scala

import akka.actor.{ActorSystem, ActorRef, ActorLogging, Actor}
import org.json4s.{DefaultFormats, Formats}
import spray.httpx.Json4sSupport
import spray.routing.Directives

/**
 * Created by zhanghao on 2015/6/3.
 */
class IndexActor extends Actor with ActorLogging {
  override def receive = {
    case None =>
  }
}

class IndexService(index: ActorRef)(implicit system: ActorSystem) extends Directives with Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats

  lazy val route =
    pathPrefix("page") {
      val dir = "page/"
      pathEndOrSingleSlash {
        getFromResource(dir + "index.html")
      } ~
        getFromResourceDirectory(dir)
    } ~
      path("echo" / Segment) {
        message => get {
          complete {
            s"${message}"
          }
        }
      } ~
      path("person") {
        get {
          complete {
            val person = new Person("Feng Jiang", 26)
            person
          }
        }

      }
}

case class Person(name:String,age:Int)