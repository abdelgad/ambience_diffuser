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
  val decisionTree: DecisionTree = Condition(
    predicate = snapshot => snapshot.temperature > 25,
    trueBranch = Condition(
      predicate = snapshot => snapshot.noiseLevel > 60,
      trueBranch = Leaf(List("High Energy.mp3", "Tropical Beats.mp3")),
      falseBranch = Leaf(List("Relaxing Tunes.mp3", "Acoustic Chill.mp3"))
    ),
    falseBranch = Condition(
      predicate = snapshot => snapshot.illuminance < 100,
      trueBranch = Leaf(List("Calm Ambience.mp3", "Soft Piano.mp3")),
      falseBranch = Leaf(List("Default Playlist.mp3", "Nature Sounds.mp3"))
    )
  )

  // Fonction pour évaluer l’arbre de décision
  def evaluate(snapshot: Snapshot): List[String] = {
    def traverse(tree: DecisionTree): List[String] = tree match {
      case Condition(predicate, trueBranch, falseBranch) =>
        if (predicate(snapshot)) traverse(trueBranch)
        else traverse(falseBranch)
      case Leaf(playlist) => playlist
    }
    traverse(decisionTree)
  }
}
