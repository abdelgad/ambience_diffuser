package station.algorithme

import station.Snapshot

case class classified_snapshot(
  colors: List[Tuple3[Int, Int, Int]], humidity: Humidity.Humidity, noise: Noise.Noise,
  illuminance: Illuminance.Illuminance, temperature: Temperature.Temperature,
  heartrate: HeartRate.HeartRate
)

object SensorClassifier {

  def classifyHumidity(humidityValue: Double): Humidity.Humidity = {
    if (humidityValue < 40) Humidity.Sec
    else if (humidityValue <= 70) Humidity.Moyen
    else Humidity.Humide
  }

  def classifyNoise(noiseLevel: Double): Noise.Noise = {
    if (noiseLevel < 60) Noise.Normale
    else Noise.Bruyant
  }

  def classifyLight(illuminance: Double): Illuminance.Illuminance = {
    if (illuminance < 15) Illuminance.Sombre
    else Illuminance.Claire
  }

  def classifyTemperature(temperature: Double): Temperature.Temperature = {
    if (temperature < 15) Temperature.Froid
    else if (temperature <= 25) Temperature.Tempere
    else Temperature.Chaud
  }

  def classifyHeartRate(heartRate: Double): HeartRate.HeartRate = {
    if (heartRate < 80) HeartRate.Faible
    else HeartRate.Fort
  }

  /**
   * Traduit Hex couleur vers RGB format.
   * @param hex_color Couleur en format hex. Doit commencer par un # suivi de 6 caractère appartenent à [0-9A-F]
   * @return Tuple de trois valeurs respectivements, Rouge, Vert et Bleu.
   * @Note Generate by duck.ai
   * */
  private def hexColorToRGB(hex_color: String): (Int, Int, Int) = {
    if (hex_color.startsWith("#") && (hex_color.length == 7 || hex_color.length == 4)) {
      val expandedHex = if (hex_color.length == 4) {
        "#" + hex_color.charAt(1) + hex_color.charAt(1) +
          hex_color.charAt(2) + hex_color.charAt(2) +
          hex_color.charAt(3) + hex_color.charAt(3)
      } else {
        hex_color
      }

      // Convertit les valeurs hexadécimales en entiers
      val r = Integer.parseInt(expandedHex.substring(1, 3), 16)
      val g = Integer.parseInt(expandedHex.substring(3, 5), 16)
      val b = Integer.parseInt(expandedHex.substring(5, 7), 16)

      (r, g, b)
    } else {
      throw new IllegalArgumentException("Invalid hex color format")
    }
  }


  def classify_snapshot(snap: Snapshot): classified_snapshot = classified_snapshot(snap.colors.map(hexColorToRGB), classifyHumidity(snap.humidity), classifyNoise(snap.dbs), classifyLight(snap.illuminance), classifyTemperature(snap.temperature), classifyHeartRate(snap.bpm))
}
