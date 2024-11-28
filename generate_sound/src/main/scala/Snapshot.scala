import spray.json._

// Classe pour représenter un Snapshot
case class Snapshot(
  datetime: String,
  humidity: Double,
  temperature: Double,
  illuminance: Double,
  noiseLevel: Double,
  heartRate: Int
)

// JSON format pour Snapshot (si nécessaire pour tests ou stockage)
object SnapshotJsonProtocol extends DefaultJsonProtocol {
  implicit val snapshotFormat: RootJsonFormat[Snapshot] = jsonFormat6(Snapshot.apply)
}
