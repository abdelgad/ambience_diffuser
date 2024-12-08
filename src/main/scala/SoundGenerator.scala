import SoundDecisionTree._

object SoundGenerator {

  def generateSound(snapshot: Snapshot): Unit = {
    val category = SoundDecisionTree.classifySnapshot(snapshot)
    val playlist = SoundDecisionTree.getPlaylist(category)

    println(s"Generated Playlist for $category:")
    playlist.foreach { song =>
      println(s"- Playing: $song")
      AudioPlayer.play(s"src/main/resources/playlist/$song.mp3")
    }
  }
}

