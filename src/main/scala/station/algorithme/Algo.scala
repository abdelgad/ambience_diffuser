package station.algorithme

//import station.LedController.sendLedCommandOnce
import station.Snapshot
import station.audio.SoundGenerator.generateSound
import station.displayEngine.{Color, OrbitePattern, RainPattern, FirePattern, display_video}

object AnimationController {
  // recevoir une snapshot
  val portName = "/dev/ttyACM0"
  
  val sc = SensorClassifier

  val primaryColor = (255, 0, 0)
  val secondaryColor = (0, 255, 0)
  val tertiaryColor = (0, 0, 255)
  val datetime = "2021-10-01T12:00:00"
  val humidity = 50.0
  val temperature = 20.0
  val illuminance = 1000.0
  val noiseLevel = 50.0
  val heartRate = 60
  
  val snapshot = Snapshot(primaryColor, secondaryColor, tertiaryColor, datetime, humidity, temperature, illuminance, noiseLevel, heartRate)

  // Fonction principale
  def triggerAnimations(factors: Snapshot): Unit = {
    // Déterminer le mode d'animation
    val animationMode = (sc.classifyHumidity(factors.humidity), sc.classifyTemperature(factors.temperature)) match {
      case ("sec" | "moyen", "froid") => "space"  // Espace
      case ("sec" | "moyen", "tempéré" | "chaud") => "fire"  // Feu
      case ("humide", "froid") => "rain"          // Pluie
      case ("humide", "tempéré" | "chaud") => "jungle" // Jungle
      case _ => "default" // Cas par défaut pour éviter les erreurs
    }

    // Déterminer la vitesse en fonction du BPM
    val speed = sc.classifyHeartRate(factors.heartRate) match {
      case "lent" => "SLOW"
      case "rapide" => "FAST"
      case _ => "MEDIUM" // Valeur par défaut
    }

    // Animation sur écran
    println(s"Launching screen animation: Mode=$animationMode, Colors=(${factors.primaryColor}, ${factors.secondaryColor}, ${factors.tertiaryColor}), Brightness=${factors.illuminance}, Speed=$speed")
    if (animationMode == "space") {
      val pattern = new OrbitePattern(
        background_color = (30, 30, 30),
        blur = 5,
        relative_orbite_width = 15,
        relative_orbite_height = 5,
        //if bpm = "lent", angular_speed = 1e-3, else 1e-2
        angular_speed = if (sc.classifyHeartRate(factors.heartRate) == "lent") 1e-3 else 1e-2,
        relative_planet_radius = 10,
        relative_sattelite_radius = 2,
        relative_faraway_planet_radius = 6,
        //if luminous, nbr_stars = 100, else 40
        nbr_stars = if (sc.classifyLight(factors.illuminance) == "lumineux") 100 else 40,
        planet_color = factors.primaryColor,
        sattelite_color = factors.secondaryColor,
        faraway_planet_color = factors.tertiaryColor
      )
      display_video.display_pattern(pattern)

    } else if (animationMode == "rain") {
      val pattern = new RainPattern(
        background_color = factors.secondaryColor,
        blur_building = 8,
        blur_cloud = 8,
        relative_building_max_height = 70, 
        building_color = factors.primaryColor,
        nbr_building = 8,
        relative_cloud_max_width = 20, 
        cloud_color = factors.tertiaryColor,
        //if luminous, nbr_cloud = 20, else 10
        nbr_cloud = if (sc.classifyLight(factors.illuminance) == "lumineux") 20 else 10,
        //if bpm = "lent", base_speed_drop = 1, else 3
        base_speed_drop = if (sc.classifyHeartRate(factors.heartRate) == "lent") 1 else 3,
        nbr_drops = if (sc.classifyLight(factors.illuminance) == "lumineux") 30 else 20,
      )
      display_video.display_pattern(pattern)
    } else {
      val pattern = new FirePattern(
        background_color = factors.secondaryColor,
        flame_color = factors.primaryColor,
        spark_color = factors.tertiaryColor,
        flame_speed = if (sc.classifyHeartRate(factors.heartRate) == "lent") 2 else 3,
        spark_speed = if (sc.classifyHeartRate(factors.heartRate) == "lent") 4 else 6,
        flame_intensity = if (sc.classifyLight(factors.illuminance) == "lumineux") 50 else 100,
        spark_intensity = if (sc.classifyLight(factors.illuminance) == "lumineux") 25 else 50,
        flame_shrink_speed = 0.5,
        spark_shrink_speed = 0.1
      )
      display_video.display_pattern(pattern)
    }

    // Jouer un son
    println(s"Playing sound for mode: $animationMode, Intensity=${factors.noiseLevel}")
    generateSound(snapshot)

    // Animation LED
    println(s"Launching LED animation: Mode=$animationMode, Color=${factors.primaryColor}, Speed=${factors.heartRate}")
    //sendLedCommandOnce(portName, animationMode, sc.classifyHeartRate(factors.heartRate), factors.primaryColor)
  }
}
