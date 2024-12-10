package station.audio

import station.Snapshot
import station.algorithme.{Humidity, Noise, Temperature, Illuminance, HeartRate, SensorClassifier}

// Représentation de l'arbre de décision pour mapper les conditions du Snapshot à une playlist
sealed trait DecisionTree
case class Condition(
  predicate: Snapshot => Boolean,
  trueBranch: DecisionTree,
  falseBranch: DecisionTree
) extends DecisionTree
case class Leaf(playlist: List[String]) extends DecisionTree

// Arbre de décision simplifié
object SoundDecisionTree {
   // Fonction pour classer les données du Snapshot
  def classifySnapshot(snapshot: Snapshot): String = {
    val humidity = SensorClassifier.classifyHumidity(snapshot.humidity)
    val noise = SensorClassifier.classifyNoise(snapshot.bpm)
    val light = SensorClassifier.classifyLight(snapshot.illuminance)
    val temperature = SensorClassifier.classifyTemperature(snapshot.temperature)
    val heartRate = SensorClassifier.classifyHeartRate(snapshot.bpm)

    (Humidity.norm(humidity), Noise.norm(noise), Illuminance.norm(light), Temperature.norm(temperature), HeartRate.norm(heartRate)) match {
      case ("Sec", "Normale", "Sombre", "Froid", "Faible") => "Catégorie 1: Triangle, Clignotement rapide, Mélodie tonique"
      case ("Sec", "Normale", "Sombre", "Froid", "Fort") => "Catégorie 2: Cercle, Transition douce, Son relaxant"
      case ("Sec", "Normale", "Claire", "Froid", "Faible") => "Catégorie 3: Carré, Ondulation lente, Son apaisant"
      case ("Sec", "Normale", "Claire", "Froid", "Fort") => "Catégorie 4: Hexagone, Clignotement moyen, Son neutre"
      case ("Sec", "Bruyant", "Sombre", "Tempéré", "Faible") => "Catégorie 5: Étoile, Transition rapide, Mélodie harmonieuse"
      case ("Sec", "Bruyant", "Sombre", "Tempéré", "Fort") => "Catégorie 6: Losange, Clignotement rapide, Son énergique"
      case ("Sec", "Bruyant", "Claire", "Chaud", "Faible") => "Catégorie 7: Triangle, Transition lente, Son relaxant"
      case ("Sec", "Bruyant", "Claire", "Chaud", "Fort") => "Catégorie 8: Cercle, Transition douce, Mélodie harmonieuse"

      // Catégories pour Humidité = Moyen
      case ("Moyen", "Normale", "Sombre", "Froid", "Faible") => "Catégorie 9: Carré, Transition rapide, Mélodie douce"
      case ("Moyen", "Normale", "Sombre", "Froid", "Fort") => "Catégorie 10: Hexagone, Transition lente, Son neutre"
      case ("Moyen", "Normale", "Claire", "Tempéré", "Faible") => "Catégorie 11: Losange, Clignotement moyen, Mélodie tonique"
      case ("Moyen", "Normale", "Claire", "Tempéré", "Fort") => "Catégorie 12: Triangle, Ondulation lente, Son relaxant"
      case ("Moyen", "Bruyant", "Sombre", "Chaud", "Faible") => "Catégorie 13: Cercle, Transition rapide, Son énergique"
      case ("Moyen", "Bruyant", "Sombre", "Chaud", "Fort") => "Catégorie 14: Carré, Transition lente, Mélodie apaisante"
      case ("Moyen", "Bruyant", "Claire", "Froid", "Faible") => "Catégorie 15: Hexagone, Transition douce, Mélodie neutre"
      case ("Moyen", "Bruyant", "Claire", "Froid", "Fort") => "Catégorie 16: Étoile, Transition rapide, Son harmonieux"

      // Catégories pour Humidité = Humide
      case ("Humide", "Normale", "Sombre", "Tempéré", "Faible") => "Catégorie 17: Cercle, Clignotement moyen, Mélodie tonique"
      case ("Humide", "Normale", "Sombre", "Tempéré", "Fort") => "Catégorie 18: Triangle, Ondulation rapide, Mélodie apaisante"
      case ("Humide", "Normale", "Claire", "Chaud", "Faible") => "Catégorie 19: Hexagone, Transition douce, Son relaxant"
      case ("Humide", "Normale", "Claire", "Chaud", "Fort") => "Catégorie 20: Carré, Clignotement rapide, Mélodie énergique"
      case ("Humide", "Bruyant", "Sombre", "Froid", "Faible") => "Catégorie 21: Triangle, Transition lente, Son apaisant"
      case ("Humide", "Bruyant", "Sombre", "Froid", "Fort") => "Catégorie 22: Cercle, Transition rapide, Mélodie harmonieuse"
      case ("Humide", "Bruyant", "Claire", "Tempéré", "Faible") => "Catégorie 23: Carré, Ondulation douce, Mélodie tonique"
      case ("Humide", "Bruyant", "Claire", "Tempéré", "Fort") => "Catégorie 24: Hexagone, Transition lente, Mélodie relaxante"
      case ("Sec", "Normale", "Claire", "Tempéré", "Faible") => "Catégorie 25: Étoile, Transition lente, Mélodie douce"
      case ("Sec", "Normale", "Claire", "Tempéré", "Fort") => "Catégorie 26: Cercle, Transition rapide, Son harmonieux"
      case ("Sec", "Bruyant", "Sombre", "Chaud", "Faible") => "Catégorie 27: Triangle, Clignotement moyen, Mélodie relaxante"
      case ("Sec", "Bruyant", "Sombre", "Chaud", "Fort") => "Catégorie 28: Hexagone, Transition douce, Son neutre"

      // Catégories pour Humidité = Moyen (Suite)
      case ("Moyen", "Normale", "Claire", "Chaud", "Faible") => "Catégorie 29: Carré, Clignotement rapide, Mélodie tonique"
      case ("Moyen", "Normale", "Claire", "Chaud", "Fort") => "Catégorie 30: Étoile, Transition lente, Son relaxant"
      case ("Moyen", "Bruyant", "Claire", "Tempéré", "Faible") => "Catégorie 31: Cercle, Ondulation rapide, Mélodie harmonieuse"
      case ("Moyen", "Bruyant", "Claire", "Tempéré", "Fort") => "Catégorie 32: Triangle, Transition moyenne, Mélodie apaisante"
      case ("Moyen", "Normale", "Sombre", "Tempéré", "Faible") => "Catégorie 33: Hexagone, Transition douce, Mélodie énergétique"
      case ("Moyen", "Normale", "Sombre", "Tempéré", "Fort") => "Catégorie 34: Losange, Ondulation lente, Son apaisant"
      case ("Moyen", "Bruyant", "Sombre", "Froid", "Faible") => "Catégorie 35: Carré, Transition rapide, Mélodie douce"
      case ("Moyen", "Bruyant", "Sombre", "Froid", "Fort") => "Catégorie 36: Hexagone, Transition lente, Son harmonieux"

      // Catégories pour Humidité = Humide (Suite)
      case ("Humide", "Normale", "Sombre", "Froid", "Faible") => "Catégorie 37: Étoile, Clignotement moyen, Mélodie tonique"
      case ("Humide", "Normale", "Sombre", "Froid", "Fort") => "Catégorie 38: Cercle, Transition douce, Mélodie apaisante"
      case ("Humide", "Normale", "Claire", "Tempéré", "Faible") => "Catégorie 39: Hexagone, Transition lente, Mélodie relaxante"
      case ("Humide", "Normale", "Claire", "Tempéré", "Fort") => "Catégorie 40: Triangle, Ondulation rapide, Son énergétique"
      case ("Humide", "Bruyant", "Claire", "Chaud", "Faible") => "Catégorie 41: Losange, Transition douce, Mélodie harmonieuse"
      case ("Humide", "Bruyant", "Claire", "Chaud", "Fort") => "Catégorie 42: Carré, Clignotement rapide, Mélodie douce"
      case ("Humide", "Bruyant", "Sombre", "Tempéré", "Faible") => "Catégorie 43: Étoile, Ondulation lente, Son relaxant"
      case ("Humide", "Bruyant", "Sombre", "Tempéré", "Fort") => "Catégorie 44: Cercle, Transition moyenne, Mélodie tonique"

      // Suite des catégories restantes
      case ("Sec", "Bruyant", "Claire", "Froid", "Faible") => "Catégorie 45: Triangle, Transition douce, Mélodie énergétique"
      case ("Sec", "Bruyant", "Claire", "Froid", "Fort") => "Catégorie 46: Hexagone, Clignotement rapide, Mélodie relaxante"
      case ("Moyen", "Normale", "Sombre", "Chaud", "Faible") => "Catégorie 47: Carré, Ondulation rapide, Mélodie harmonieuse"
      case ("Moyen", "Normale", "Sombre", "Chaud", "Fort") => "Catégorie 48: Étoile, Transition lente, Son apaisant"
      case ("Humide", "Bruyant", "Sombre", "Froid", "Faible") => "Catégorie 49: Cercle, Ondulation lente, Mélodie douce"
      case ("Humide", "Bruyant", "Sombre", "Froid", "Fort") => "Catégorie 50: Triangle, Transition moyenne, Mélodie relaxante"
      case ("Sec", "Bruyant", "Claire", "Tempéré", "Faible") => "Catégorie 51: Hexagone, Clignotement moyen, Mélodie douce"
      case ("Sec", "Bruyant", "Claire", "Tempéré", "Fort") => "Catégorie 52: Triangle, Transition rapide, Son harmonieux"
      case ("Sec", "Normale", "Sombre", "Chaud", "Faible") => "Catégorie 53: Carré, Ondulation lente, Mélodie relaxante"
      case ("Sec", "Normale", "Sombre", "Chaud", "Fort") => "Catégorie 54: Étoile, Transition douce, Son neutre"

      // Suite des catégories pour Humidité = Moyen
      case ("Moyen", "Bruyant", "Sombre", "Tempéré", "Faible") => "Catégorie 55: Cercle, Clignotement rapide, Mélodie tonique"
      case ("Moyen", "Bruyant", "Sombre", "Tempéré", "Fort") => "Catégorie 56: Losange, Ondulation lente, Son apaisant"
      case ("Moyen", "Normale", "Claire", "Froid", "Faible") => "Catégorie 57: Triangle, Transition moyenne, Mélodie énergétique"
      case ("Moyen", "Normale", "Claire", "Froid", "Fort") => "Catégorie 58: Hexagone, Transition rapide, Mélodie harmonieuse"
      case ("Moyen", "Bruyant", "Claire", "Chaud", "Faible") => "Catégorie 59: Carré, Ondulation douce, Mélodie relaxante"
      case ("Moyen", "Bruyant", "Claire", "Chaud", "Fort") => "Catégorie 60: Étoile, Transition lente, Son harmonieux"

      // Suite des catégories pour Humidité = Humide
      case ("Humide", "Normale", "Sombre", "Tempéré", "Faible") => "Catégorie 61: Cercle, Clignotement moyen, Son relaxant"
      case ("Humide", "Normale", "Sombre", "Tempéré", "Fort") => "Catégorie 62: Triangle, Transition lente, Mélodie douce"
      case ("Humide", "Normale", "Claire", "Froid", "Faible") => "Catégorie 63: Hexagone, Transition rapide, Son énergique"
      case ("Humide", "Normale", "Claire", "Froid", "Fort") => "Catégorie 64: Carré, Ondulation rapide, Mélodie harmonieuse"
      case ("Humide", "Bruyant", "Claire", "Tempéré", "Faible") => "Catégorie 65: Étoile, Transition douce, Son neutre"
      case ("Humide", "Bruyant", "Claire", "Tempéré", "Fort") => "Catégorie 66: Cercle, Clignotement rapide, Son énergétique"
      case ("Humide", "Bruyant", "Sombre", "Chaud", "Faible") => "Catégorie 67: Triangle, Transition lente, Mélodie apaisante"
      case ("Humide", "Bruyant", "Sombre", "Chaud", "Fort") => "Catégorie 68: Hexagone, Transition moyenne, Son harmonieux"

      // Dernières catégories pour combinaisons restantes
      case ("Sec", "Normale", "Claire", "Chaud", "Faible") => "Catégorie 69: Carré, Ondulation lente, Mélodie relaxante"
      case ("Sec", "Normale", "Claire", "Chaud", "Fort") => "Catégorie 70: Étoile, Transition douce, Son neutre"
      case ("Humide", "Bruyant", "Claire", "Froid", "Faible") => "Catégorie 71: Cercle, Clignotement moyen, Mélodie douce"
      case ("Humide", "Bruyant", "Claire", "Froid", "Fort") => "Catégorie 72: Triangle, Transition rapide, Son énergique"

      case _ =>                                                 "Catégorie Inconnue: Aucun affichage associé" // Si finalement aucuns critères ne corresponds
    }
  }

  // Fonction pour mapper une catégorie à une playlist
def getPlaylist(category: String): List[String] = {
  category match {
    // Catégories avec Mélodie tonique, Son tonique
    case cat if cat.contains("Mélodie tonique") || cat.contains("Son tonique") =>
      List(
        "ekisa-kyo-tekitegerekeka-by-tonic-melodies-choir-128-ytshorts.savetube.me",
        "bullijo-tutendereza-by-tonic-melodies-choir-128-ytshorts.savetube.me",
        "yesu-byonna-abimanyi-by-tonic-melodies-choir-128-ytshorts.savetube.me"
      )

    // Catégories avec Son relaxant, Mélodie relaxante
    case cat if cat.contains("Son relaxant") || cat.contains("Mélodie relaxante") =>
      List(
        "nature-sounds-for-deep-sleep-128-ytshorts.savetube.me",
        "relaxation-128-ytshorts.savetube.me",
        "5-minute-meditation-music-with-earth-resonance-frequency-for-deeper-relaxation-128-ytshorts.savetube.me"
      )

    // Catégories avec Son apaisant, Mélodie apaisante
    case cat if cat.contains("Son apaisant") || cat.contains("Mélodie apaisante") =>
      List(
        "4-minute-timer-relaxing-music-lofi-fish-background-128-ytshorts.savetube.me",
        "short-meditation-music-3-minute-relaxation-calming-128-ytshorts.savetube.me",
        "musique-zen-5-minutes-128-ytshorts.savetube.me"
      )

    // Catégories avec Son neutre, Mélodie neutre
    case cat if cat.contains("Son neutre") || cat.contains("Mélodie neutre") =>
      List(
        "yesu-leero-nkukowoola-by-tonic-melodies-choir-128-ytshorts.savetube.me",
        "5-minute-timer-relaxing-music-with-ocean-waves-128-ytshorts.savetube.me"
      )

    // Catégories avec Son énergique, Mélodie énergique
    case cat if cat.contains("Son énergique") || cat.contains("Mélodie énergique") =>
      List(
        "clean-up-song-for-children-by-elf-learning-128-ytshorts.savetube.me",
        "",
      )

    // Catégories avec Mélodie harmonieuse, Son harmonieux
    case cat if cat.contains("Mélodie harmonieuse") || cat.contains("Son harmonieux") =>
      List(
        "bernard-lavilliers-jimmy-cliff-melody-tempo-harmony-128-ytshorts.savetube.me",
        "melodie-und-harmonie-siegfried-rundel-128-ytshorts.savetube.me",
        "harmonize-feat-bruce-melodie-zanzibar-official-music-video-128-ytshorts.savetube.me"
      )

    // Catégories avec Mélodie douce, Son doux
    case cat if cat.contains("Mélodie douce") || cat.contains("Son doux") =>
      List(
        "",
        ""
      )

    // Catégories inconnues ou par défaut
    case _ =>
      List("default-sound.mp3") // Fichier par défaut si aucune correspondance trouvée
  }
}

}
