package main.rest

import akka.actor.Actor
import com.sun.xml.internal.bind.v2.TODO
import spray.http.StatusCodes
import spray.routing.{Route, HttpService}
import akka.util.Timeout
import scala.concurrent.duration._

/**
 * Created by zhanghao on 2015/6/8.
 */
class RestInterface extends Actor with RestApi{

  def actorRefFactory = context

  def receive =runRoute(routes)
}

trait RestApi extends HttpService{

    implicit def executionContext = actorRefFactory.dispatcher

    implicit val timeout = Timeout(10 seconds)

    def routes:Route = {
      pathPrefix("/register" / Segment){
          path("firstSigin"){
            get{
              requestContext=>
                  requestContext.complete(StatusCodes.OK,"hello world")
            }
          }

      }
    }

}