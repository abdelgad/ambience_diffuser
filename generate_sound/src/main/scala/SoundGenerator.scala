import SoundDecisionTree._

object SoundGenerator {

  def generateSound(snapshot: Snapshot): Unit = {
    val category = SoundDecisionTree.classifySnapshot(snapshot)
    val playlist = SoundDecisionTree.getPlaylist(category)

    println(s"Generated Playlist for Category ($category):")
    playlist.foreach(song => println(s"- $song"))
  }
}

