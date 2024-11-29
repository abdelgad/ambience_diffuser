import SnapshotJsonProtocol._
import spray.json._

object Main extends App {
  // Simuler un Snapshot
  val simulatedSnapshot = Snapshot(
    datetime = "2024-11-28T14:30:00",
    humidity = 40.0,       // En pourcentage
    temperature = 18.0,    // En degrés Celsius
    illuminance = 150.0,   // En lux
    noiseLevel = 55.0,     // En décibels
    heartRate = 85         // En BPM
  )

  // Afficher les données brutes
  println("Raw Snapshot Data:")
  println(simulatedSnapshot.toJson.prettyPrint)
  
  // Générer les sons pour le Snapshot simulé
  println("Simulated Snapshot:")
  println(simulatedSnapshot.toJson.prettyPrint)
  SoundGenerator.generateSound(simulatedSnapshot)
}
