import SnapshotJsonProtocol._
import spray.json._

object Main extends App {
  // Simuler un Snapshot
  val simulatedSnapshot = Snapshot(
    datetime = "2024-11-28T14:30:00",
    humidity = "Sec",
    noise = "Normale",
    light = "Sombre",
    temperature = "Froid",
    heartRate = "Faible"
  )

  // Générer les sons pour le Snapshot simulé
  println("Simulated Snapshot:")
  println(simulatedSnapshot.toJson.prettyPrint)
  SoundGenerator.generateSound(simulatedSnapshot)
}
