package station.algorithme

object Humidity extends Enumeration {
  type Humidity = Value
  val Sec, Moyen, Humide = Value
}

object Noise extends Enumeration {
  type Noise = Value
  val Normale, Bruyant = Value
}

object Illuminance extends Enumeration {
  type Illuminance = Value
  val Sombre, Claire = Value
}

object Temperature extends Enumeration {
  type Temperature = Value
  val Froid, Tempere, Chaud = Value
}

object HeartRate extends Enumeration {
  type HeartRate = Value
  val Faible, Fort = Value
}