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
    val noise = SensorClassifier.classifyNoise(snapshot.noiseLevel)
    val light = SensorClassifier.classifyLight(snapshot.illuminance)
    val temperature = SensorClassifier.classifyTemperature(snapshot.temperature)
    val heartRate = SensorClassifier.classifyHeartRate(snapshot.heartRate)

    (humidity, noise, light, temperature, heartRate) match {
      case ("Sec", "Normale", "Sombre", "Froid", "Faible") =>
        "Catégorie 1: Triangle, Clignotement rapide, Mélodie tonique"
      case ("Sec", "Normale", "Sombre", "Froid", "Fort") =>
        "Catégorie 2: Cercle, Transition douce, Son relaxant" //Faire ça jusqu'à la catégorie 72
      case ("Humide", "Bruyant", "Claire", "Chaud", "Fort") =>
        "Catégorie 72: Triangle, Transition rapide, Son énergique"
      case _ =>                                             // Si finalement aucuns critères ne corresponds
        "Catégorie Inconnue: Aucun affichage associé"
    }
  }

  // Fonction pour mapper une catégorie à une playlist
  def getPlaylist(category: String): List[String] = {
    category match {
      case cat if cat.startsWith("Catégorie 1") =>
        List("High Energy.mp3", "Tropical Beats.mp3")
      case cat if cat.startsWith("Catégorie 2") =>
        List("Relaxing Piano.mp3", "Soft Guitar.mp3")  //Prévoir pour toutes les catégories ??
      case _ =>
        List("DefaultSound.mp3")
    }
  }
}
