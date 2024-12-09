package station

import scala.io.Source
import spray.json.*
import station.displayEngine.Color

case class RGB(r: Int, g: Int, b: Int)
  
// Classe pour représenter un Snapshot
//case class Snapshot(
//  primaryColor: Color,   // Couleur principale
//  secondaryColor: Color, // Couleur secondaire
//  tertiaryColor: Color,  // Couleur tertiaire
//  datetime: String,
//  humidity: Double,       // En pourcentage
//  temperature: Double,    // En degrés Celsius
//  illuminance: Double,    // En lux
//  noiseLevel: Double,     // En décibels
//  heartRate: Int          // En BPM
//)

case class Snapshot(
  datetime: String, humidity: Double, temperature: Double,
  illuminance: Double, colors: List[String], bpm: Double, dbs: Double
)

object snapLoader extends DefaultJsonProtocol() {

  implicit val snapshotFormat: RootJsonFormat[Snapshot] = jsonFormat7(Snapshot.apply)

  def load_snap(name: String): Snapshot = {
    val jsonString = Source.fromFile(s"/home/student/snapshots/$name").getLines().mkString
    jsonString.parseJson.convertTo[Snapshot]
  }
}
