package ImageGenerator

import java.awt.Color
import java.awt.image.BufferedImage

class VideoGenerator(width: Int, height: Int) {
  
  val IG: ImageGenerator = new ImageGenerator(width, height)
  
  def next(pattern: Pattern): Option[BufferedImage] = {
    pattern match
      case orbite: Orbite => Some(next_orbite(orbite))
      case _ => None
  }
  
  private def next_orbite(orbite: Orbite): BufferedImage = {

    val bg_color: Color = new Color(0, 0, 0, 255)
    IG new_image bg_color

    IG addCercle(this rpw 25, this rph 25, this rpw 20, orbite.primary)

    val orbite_x = (this rpw 25) - orbite.frame

    IG addCercle(orbite_x, this rph 35, this rpw 5, orbite.secondary)

    IG.generate_image()
  }

  private def rpw = Helper.relativePosition(_, width)
  private def rph = Helper.relativePosition(_, height)

}
