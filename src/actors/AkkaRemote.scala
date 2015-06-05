
package actors

import akka.actor._

object Test1 {
  def send = {
    val system = ActorSystem("test1")
    val actor1 = system.actorOf(Props[AkkaRemote],"Test1")
    Test2.actor2 ! 1
  }


}
object Test2{
  val system = ActorSystem("test2")
  val actor2 = system.actorOf(Props[AkkaRemote],"Test2")
  def send={

    actor2!4
  }

}
object AkkaRemote extends App{
  Test1.send
  Thread.sleep(2000)
  Test2.send
}
class AkkaRemote  extends Actor {

  var i:Int = 10
  def receive = {
    case s:Int => println(s)
       i=s+i
      println(i)
    case _=>println("ops")

  }
  /*val system = ActorSystem()

  override def receive = {
    case msg:State =>
      msg match {
        case state:State => println("")
      }
    case _ =>print("")
  }
  startWith(WaitForRequests,new StateData(0))

  //val timeout = scala.concurrent.duration.FiniteDuration
  when(WaitForRequests){
    case Event()
  }

  val config = ConfigFactory.load()

 // val backend = ActorSystem("",config)


  val conf = config.getString("akka.actor.provider")

  println(conf)

  val confInt = config.getInt("myapp.versionint")

  println(confInt)*/

}

/*
class RemoteLookup(path:String) extends Actor with ActorLogging{

  context.setReceiveTimeout(3 seconds)

  sendIdentifyRequest()

  def sendIdentifyRequest():Unit = {

    val selection = context.actorSelection(path)
    selection!Identify(path)
  }

  val uri = ""

  val backendAddress = AddressFromURIString(uri)

  val props = Props[String].withDeploy(Deploy(scope = RemoteScope(backendAddress)))

  context.actorOf(props,"")
  def identify:Receive = {
    case Ac
  }

  def receive = {

  }

}*/
sealed trait State

case object WaitForRequests extends State
case object ProcessRequest extends  State
case object SoldOut extends State

case class StateData(nrBooksInStore:Int)