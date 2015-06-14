package main.yiqirun.actor


import akka.actor.{Actor, ActorLogging}
import main.yiqirun.domain.WebSocket

/**
 * Created by zhanghao on 2015/6/12.
 */
/*class RunActor extends Actor with ActorLogging{


  def receive = {
    case WebSocket.Open(ws) =>
      if (null != ws) {
            clients += ws
        for (markerEntry <- markers if None != markerEntry._2)
          ws.send(message(markerEntry._2.get))
        log.debug("registered monitor for url {}", ws.path)
      }
  }
}*/
