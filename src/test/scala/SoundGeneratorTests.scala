import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._
import station.audio.{SoundDecisionTree, SoundGenerator}
import station.{Snapshot}

class SoundGeneratorTests extends AnyFlatSpec {

  // Test 1: Vérifier la classification des snapshots
  "SoundDecisionTree.classifySnapshot" should "return correct category based on snapshot data" in {
    val snapshot = Snapshot(
      datetime = "2024-12-08T12:00:00",
      humidity = 40.0,
      temperature = 18.0,
      illuminance = 150.0,
      noiseLevel = 55.0,
      heartRate = 85
    )

    val category = SoundDecisionTree.classifySnapshot(snapshot)
    category shouldBe "Catégorie 34: Losange, Ondulation lente, Son apaisant" // Vérifie la sortie attendue
  }

  // Test 2: Vérifier la récupération de playlist
  "SoundDecisionTree.getPlaylist" should "return a valid playlist for a given category" in {
    val category = "Catégorie 34: Losange, Ondulation lente, Son apaisant"
    val playlist = SoundDecisionTree.getPlaylist(category)

    playlist should not be empty
    playlist should contain("4-minute-timer-relaxing-music-lofi-fish-background-128-ytshorts.savetube.me")
  }

  // Test 3: Vérifier la lecture d'un fichier audio
  "AudioPlayer.play" should "not throw an exception when playing a valid file" in {
    noException should be thrownBy {
      val filePath = "src/main/resources/playlist/relaxation-128-ytshorts.savetube.me.mp3"
      station.audio.AudioPlayer.play(filePath) // Tester la lecture
    }
  }

  // Test 4: Vérifier la génération complète de sons
  "SoundGenerator.generateSound" should "generate and play all songs from a valid playlist" in {
    val snapshot = Snapshot(
      datetime = "2024-12-08T12:00:00",
      humidity = 40.0,
      temperature = 18.0,
      illuminance = 150.0,
      noiseLevel = 55.0,
      heartRate = 85
    )

    noException should be thrownBy {
      SoundGenerator.generateSound(snapshot) // Tester la génération complète
    }
  }
}
