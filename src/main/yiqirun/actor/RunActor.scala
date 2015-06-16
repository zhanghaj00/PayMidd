package main.yiqirun.actor


import akka.actor.{Actor, ActorLogging}
import main.yiqirun.domain.WebSocket

/**
 * Created by zhanghao on 2015/6/12.
 */
class RunActor(userID:String) extends Actor with ActorLogging{


  def receive = {
    case WebSocket.Open(ws) =>
        log.info("registered monitor for url..........{}", ws.path)
         log.info("get user"+userID)
    case WebSocket.Message(ws,msg) =>
       ws.send(msg)

  }

}
