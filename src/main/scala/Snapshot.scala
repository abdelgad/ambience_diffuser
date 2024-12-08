import spray.json._

  case class RGB(r: Int, g: Int, b: Int)
  
// Classe pour représenter un Snapshot
case class Snapshot(
  primaryColor: RGB,   // Couleur principale
  secondaryColor: RGB, // Couleur secondaire
  tertiaryColor: RGB,  // Couleur tertiaire
  datetime: String,
  humidity: Double,       // En pourcentage
  temperature: Double,    // En degrés Celsius
  illuminance: Double,    // En lux
  noiseLevel: Double,     // En décibels
  heartRate: Int          // En BPM
)

// JSON format pour Snapshot (si nécessaire pour tests ou stockage)
object SnapshotJsonProtocol extends DefaultJsonProtocol {
  implicit val snapshotFormat: RootJsonFormat[Snapshot] = jsonFormat6(Snapshot.apply)
}
