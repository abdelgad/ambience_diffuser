package station.audio

import akka.actor.Actor
import station.Snapshot

import message.PlaySound

class SoundGenerator extends Actor {

  override def receive: Receive = {
    case PlaySound(snap) => generateSound(snap)
  }

  private def generateSound(snapshot: Snapshot): Unit = {
    val category = SoundDecisionTree.classifySnapshot(snapshot)
    val playlist = SoundDecisionTree.getPlaylist(category)

    println(s"Generated Playlist for $category:")
    playlist.foreach { song =>
      println(s"- Playing: $song")
      AudioPlayer.play(s"/home/student/Music/ambience/$song.mp3")
    }
  }
}

