package station.algorithme
import station.AmbienceDiffuser
import station.Snapshot
import station.displayEngine.{fire_params, orbite_params, rain_params}
// import station.audio.SoundGenerator.generateSound
import station.displayEngine.{Color, OrbitePattern, RainPattern, FirePattern, display_video}
import message.Ledmessage

object AnimationController {
  // recevoir une snapshot
  val portName = "/dev/ttyACM0"

  // function to stop annimation
  var stop_annimation = () => {}

  // Fonction principale
  def triggerAnimations(factors: Snapshot): Unit = {
    // Déterminer le mode d'animation
    val cs = SensorClassifier.classify_snapshot(factors)
    val animationMode = (cs.humidity, cs.temperature) match {
      case (Humidity.Sec | Humidity.Moyen, Temperature.Froid) => "space"  // Espace
      case (Humidity.Sec | Humidity.Moyen, Temperature.Tempere | Temperature.Chaud) => "fire"  // Feu
      case (Humidity.Humide, Temperature.Froid) => "rain"          // Pluie
      case (Humidity.Humide, Temperature.Tempere | Temperature.Chaud) => "jungle" // Jungle
      case _ => "default" // Cas par défaut pour éviter les erreurs
    }

    // Déterminer la vitesse en fonction du BPM
    val speed = cs.heartrate match {
      case HeartRate.Faible => "SLOW"
      case HeartRate.Fort => "FAST"
      case _ => "MEDIUM" // Valeur par défaut
    }

    // Animation sur écran
    println(s"Launching screen animation: Mode=$animationMode, Colors=(${cs.colors(0)}, ${cs.colors(1)}, ${cs.colors(2)}), Brightness=${cs.illuminance}, Speed=$speed")
    if (animationMode == "space") {
      val pattern = new OrbitePattern(
        background_color = (30, 30, 30),
        blur = 5,
        relative_orbite_width = 15,
        relative_orbite_height = 5,
        //if bpm = "lent", angular_speed = 1e-3, else 1e-2
        angular_speed = if (cs.heartrate == HeartRate.Faible) 1e-3 else 1e-2,
        relative_planet_radius = 10,
        relative_sattelite_radius = 2,
        relative_faraway_planet_radius = 6,
        //if luminous, nbr_stars = 100, else 40
        nbr_stars = if (cs.illuminance == Illuminance.Claire) 100 else 40,
        planet_color = cs.colors(0),
        sattelite_color = cs.colors(1),
        faraway_planet_color = cs.colors(2)
      )
      orbite_params.running = true
      display_video.display_pattern(pattern)
      this.stop_annimation = () => {orbite_params.running = false}

    } else if (animationMode == "rain") {
      val pattern = new RainPattern(
        background_color = cs.colors(1),
        blur_building = 8,
        blur_cloud = 8,
        relative_building_max_height = 70, 
        building_color = cs.colors(0),
        nbr_building = 8,
        relative_cloud_max_width = 20, 
        cloud_color = cs.colors(2),
        //if luminous, nbr_cloud = 20, else 10
        nbr_cloud = if (cs.illuminance == Illuminance.Claire) 20 else 10,
        //if bpm = "lent", base_speed_drop = 1, else 3
        base_speed_drop = if (cs.heartrate == HeartRate.Faible) 3 else 5,
        nbr_drops = if (cs.illuminance == Illuminance.Claire) 30 else 20,
      )
      rain_params.running = true
      display_video.display_pattern(pattern)
      this.stop_annimation = () => {rain_params.running = false}
    } else {
      val pattern = new FirePattern(
        background_color = cs.colors(1),
        flame_color = cs.colors(0),
        spark_color = cs.colors(2),
        flame_speed = if (cs.heartrate == HeartRate.Faible) 2 else 3,
        spark_speed = if (cs.heartrate == HeartRate.Faible) 4 else 6,
        flame_intensity = if (cs.illuminance == Illuminance.Claire) 50 else 100,
        spark_intensity = if (cs.illuminance == Illuminance.Claire) 25 else 50,
        flame_shrink_speed = 0.5,
        spark_shrink_speed = 0.1
      )
      fire_params.running = true
      display_video.display_pattern(pattern)
      this.stop_annimation = () => {fire_params.running = false}
    }

    // Jouer un son
    // println(s"Playing sound for mode: $animationMode, Intensity=${cs.noise}")
    // generateSound(cs)

    // Animation LED
    println(s"Launching LED animation: Mode=$animationMode, Color=${cs.colors(0)}, Speed=${cs.heartrate}")
    AmbienceDiffuser.arduinoActor ! Ledmessage(animationMode, speed, cs.colors(0))
    }
}
