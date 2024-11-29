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
    (snapshot.humidity, snapshot.noise, snapshot.light, snapshot.temperature, snapshot.heartRate) match {
      case ("Sec", "Normale", "Sombre", "Froid", "Faible") => "Catégorie 1"
      case ("Sec", "Normale", "Sombre", "Froid", "Fort") => "Catégorie 2"
      case ("Sec", "Normale", "Claire", "Froid", "Faible") => "Catégorie 3"
      case ("Sec", "Normale", "Claire", "Froid", "Fort") => "Catégorie 4"
      case ("Sec", "Bruyant", "Sombre", "Tempéré", "Faible") => "Catégorie 5"
      case ("Sec", "Bruyant", "Sombre", "Tempéré", "Fort") => "Catégorie 6"
      // Les autres catégories du fichier...
      case _ => "Catégorie Inconnue"
    }
  }

  // Fonction pour mapper une catégorie à une playlist
  def getPlaylist(category: String): List[String] = {
    category match {
      case "Catégorie 1" => List("Triangle.mp3", "ClignotementRapide.mp3", "MelodieTonique.mp3")
      case "Catégorie 2" => List("Cercle.mp3", "TransitionDouce.mp3", "SonRelaxant.mp3")
      case "Catégorie 3" => List("Carre.mp3", "OndulationLente.mp3", "SonApaisant.mp3")
      case "Catégorie 4" => List("Hexagone.mp3", "ClignotementMoyen.mp3", "SonNeutre.mp3")
      case "Catégorie 5" => List("Etoile.mp3", "TransitionRapide.mp3", "MelodieHarmonieuse.mp3")
      // Playlists pour les autres catégories...
      case _ => List("DefaultSound.mp3")
    }
  }
}
