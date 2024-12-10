package station.audio

import akka.actor.Actor
import javazoom.jl.player.Player
import station.Snapshot
import message.{PlaySound, StopSound}

import java.io.FileInputStream

class SoundGenerator extends Actor {

  // private var t : Thread = _
  private var player: Option[Player] = None

  override def receive: Receive = {
    case PlaySound(snap) => generateSound(snap)
    case StopSound => // Stop the music
      stopMusic()
      println("Music stopped.")
  }

  private def generateSound(snapshot: Snapshot): Unit = {
    val category = SoundDecisionTree.classifySnapshot(snapshot)
    val playlist = SoundDecisionTree.getPlaylist(category)
    val filePath = s"/home/student/Music/ambience/${playlist.head}.mp3"
    try {
      val inputStream = new FileInputStream(filePath)
      val newPlayer = new Player(inputStream)
      player = Some(newPlayer)

      // Run the player in a separate thread to avoid blocking
      val thread = new Thread(() => newPlayer.play())
      thread.start()

      println(s"Playing: $filePath")
    } catch {
      case e: Exception =>
        println(s"Error playing file: $e")
    }
    // play(s"/home/student/Music/ambience/${playlist.head}.mp3")
  }

  private def stopMusic(): Unit = {
    player.foreach(_.close())
    player = None
  }

//  private def stopGeneration(): Unit = {
//    if(t.isAlive) t.interrupt()
//  }

//  private def play(filePath: String): Unit = {
//    try {
//      val fileStream = new FileInputStream(filePath)
//      player = Some(new Player(fileStream))
//      println(s"Playing audio: $filePath")
//      player.play()
//      println("Noé")
//    } catch {
//      case e: Exception =>
//        println(s"Error playing audio file: $e")
//    }
//  }

//  private def stop() = {
//    println("haaaaaaaa")
//    player.close()
//  }

}

