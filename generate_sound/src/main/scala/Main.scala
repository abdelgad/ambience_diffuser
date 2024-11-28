import SnapshotJsonProtocol._
import spray.json._

object Main extends App {
  // Simuler un Snapshot
  val simulatedSnapshot = Snapshot(
    datetime = "2024-11-26T12:00:00",
    humidity = 45.0,
    temperature = 28.5,
    illuminance = 80.0,
    noiseLevel = 65.0,
    heartRate = 75
  )

  // Générer les sons pour le Snapshot simulé
  println("Simulated Snapshot:")
  println(simulatedSnapshot.toJson.prettyPrint)
  SoundGenerator.generateSound(simulatedSnapshot)
}
