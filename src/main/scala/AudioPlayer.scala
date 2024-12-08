import javazoom.jl.player.Player
import java.io.FileInputStream

object AudioPlayer {
  def play(filePath: String): Unit = {
    try {
      val fileStream = new FileInputStream(filePath)
      val player = new Player(fileStream)
      println(s"Playing audio: $filePath")
      player.play()
    } catch {
      case e: Exception =>
        println(s"Error playing audio file: $e")
    }
  }
}
// Exemple d'appel
//AudioPlayer.play("src/main/resources/playlist/4-minute-breathing-meditation-to-chill-out-128-ytshorts.savetube.me.mp3")