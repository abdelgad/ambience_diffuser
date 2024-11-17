import akka.actor.{ActorSystem, Props}


object AmbienceDiffuser extends App {
  val system = ActorSystem("AmbienceDiffuser")
  val fileClient = system.actorOf(Props(new FileClient("snapshots")), "fileClient")
}