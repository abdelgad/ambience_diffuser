package station.algorithme

import station.algorithme.Temperature.Temperature

object Humidity extends Enumeration {
  type Humidity = Value
  val Sec, Moyen, Humide = Value
  def norm(h:Humidity) = {
    if (h == Sec) "Sec"
    else if (h == Moyen) "Moyen"
    else "Humide"
  }
}

object Noise extends Enumeration {
  type Noise = Value
  val Normale, Bruyant = Value
  def norm(n: Noise) = {
    if (n == Normale) "Normale"
    else "Bruyant"
  }
}

object Illuminance extends Enumeration {
  type Illuminance = Value
  val Sombre, Claire = Value
  def norm(i: Illuminance) = {
    if (i == Sombre) "Sombre"
    else "Claire"
  }
}

object Temperature extends Enumeration {
  type Temperature = Value
  val Froid, Tempere, Chaud = Value
  def norm(t: Temperature) = {
    if (t == Froid) "Froid"
    else if (t == Tempere) "Tempere"
    else "Chaud"
  }
}

object HeartRate extends Enumeration {
  type HeartRate = Value
  val Faible, Fort = Value
  def norm(h: HeartRate) = {
    if (h == Faible) "Faible"
    else "Fort"
  }
}