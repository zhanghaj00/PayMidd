package main.websocket

import akka.actor.{Actor, ActorLogging}

/**
 * Created by zhanghao on 2015/6/3.
 */
class IndexActor extends Actor with ActorLogging {

  import WSocketServer._

  override def receive = {
    case Open(ws, hs) =>
      ws.send("Hello")
      log.debug("registered monitor for url {}", hs.getResourceDescriptor)
    case Message(ws, msg) =>
     // log.debug("url {} received msg '{}'", ws.getResourceDescriptor, msg)
      ws.send("[echo]"+msg)
  }
}
