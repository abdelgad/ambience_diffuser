import SoundDecisionTree._

object SoundGenerator {
  def generateSound(snapshot: Snapshot): Unit = {
    val playlist = evaluate(snapshot)
    println(s"Generated Playlist for Snapshot (${snapshot.datetime}):")
    playlist.foreach(song => println(s"- $song"))
  }
}
