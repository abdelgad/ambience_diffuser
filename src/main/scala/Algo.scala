import LedController.sendLedCommandOnce

object AnimationController {

  // Types pour les paramètres
  case class RGB(r: Int, g: Int, b: Int)
  case class Factors(
      primaryColor: RGB,   // Couleur principale
      secondaryColor: RGB, // Couleur secondaire
      tertiaryColor: RGB,  // Couleur tertiaire
      brightness: String,  // "sombre" ou "lumineux"
      bpm: String,         // "lent" ou "rapide"
      sound: String,       // "calme" ou "bruyant"
      humidity: String,    // "sec", "moyen" ou "humide"
      temperature: String  // "froid", "tempéré", "chaud"
  )

  // Fonction principale
  def triggerAnimations(factors: Factors): Unit = {
    // Déterminer le mode d'animation
    val animationMode = (factors.humidity, factors.temperature) match {
      case ("sec" | "moyen", "froid") => "space"  // Espace
      case ("sec" | "moyen", "tempéré" | "chaud") => "fire"  // Feu
      case ("humide", "froid") => "rain"          // Pluie
      case ("humide", "tempéré" | "chaud") => "jungle" // Jungle
      case _ => "default" // Cas par défaut pour éviter les erreurs
    }

    // Déterminer la vitesse en fonction du BPM
    val speed = factors.bpm match {
      case "lent" => "SLOW"
      case "rapide" => "FAST"
      case _ => "MEDIUM" // Valeur par défaut
    }

    // Animation sur écran
    println(s"Launching screen animation: Mode=$animationMode, Colors=(${factors.primaryColor}, ${factors.secondaryColor}, ${factors.tertiaryColor}), Brightness=${factors.brightness}, Speed=$speed")
    // TODO: Lancer l'animation sur écran

    // Jouer un son
    println(s"Playing sound for mode: $animationMode, Intensity=${factors.sound}")
    // TODO: faire apepel a la fonction de lecture de son

    // Animation LED
    println(s"Launching LED animation: Mode=$animationMode, Color=${factors.primaryColor}, Speed=$speed")
    sendLedCommandOnce(portName, animationMode, speed, (factors.primaryColor.r, factors.primaryColor.g, factors.primaryColor.b))
  }
}
