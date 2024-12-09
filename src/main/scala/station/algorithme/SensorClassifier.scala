package station.algorithme

object SensorClassifier {

  def classifyHumidity(humidityValue: Double): String = {
    if (humidityValue < 40) "Sec"
    else if (humidityValue <= 70) "Moyen"
    else "Humide"
  }

  def classifyNoise(noiseLevel: Double): String = {
    if (noiseLevel < 60) "Normale"
    else "Bruyant"
  }

  def classifyLight(illuminance: Double): String = {
    if (illuminance < 15) "Sombre"
    else "Claire"
  }

  def classifyTemperature(temperature: Double): String = {
    if (temperature < 15) "Froid"
    else if (temperature <= 25) "Tempéré"
    else "Chaud"
  }

  def classifyHeartRate(heartRate: Int): String = {
    if (heartRate < 80) "Faible"
    else "Fort"
  }
}
