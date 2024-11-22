package ImageGenerator

object ImagePattern extends Enumeration {
  type ImagePattern = Value
  /**
   * Round => Colored round
   * Building => Rect bottom up
   * */
  val Round, Building = Value
}
