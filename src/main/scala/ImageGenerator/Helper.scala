package ImageGenerator

object Helper {
  /**
   * Helping function to allow relative positionning. To not care about image dimension
   *
   * @param percent Relative position value
   * @param dimSize Value of the size of the targeted dimension
   * */
  def relativePosition(percent: Float, dimSize: Int): Int = {
    val position: Float = (percent * dimSize) / 100
    position.toInt
  }
}
