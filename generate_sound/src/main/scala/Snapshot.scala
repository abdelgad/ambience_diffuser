import spray.json._

// Classe pour représenter un Snapshot
case class Snapshot(
  datetime: String,
  humidity: String,    // Sec, Moyen, Humide
  noise: String,       // Normale, Bruyant
  light: String,       // Claire, Sombre
  temperature: String, // Froid, Tempéré, Chaud
  heartRate: String    // Faible, Fort
)

// JSON format pour Snapshot (si nécessaire pour tests ou stockage)
object SnapshotJsonProtocol extends DefaultJsonProtocol {
  implicit val snapshotFormat: RootJsonFormat[Snapshot] = jsonFormat6(Snapshot.apply)
}
