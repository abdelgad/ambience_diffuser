/**
 * This class allow to generate image.
 * It used Java2D (Graphic2D)
 * */
package ImageGenerator

import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage
import com.jhlabs.image.GaussianFilter

/**
 * Helping class to generate image. **Warning**: It's not allow to resize image.
 * @param width image width, should be equal to screen width
 * @param height image height, should be equal to screen height
 * */
class ImageGenerator(width: Int, height: Int) {

  // Create an image with ARGB color space. A stands for alpha, and allow to control transparency
  val bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
  val g2d = bufferedImage.createGraphics()

  def generate_image(blur: Boolean = true): BufferedImage = {

    // Call dispose without argument
    g2d.dispose
    if (blur) {
      val blurFilter = new GaussianFilter(30)
      blurFilter.filter(bufferedImage, null)
    } else {
      bufferedImage
    }
  }

  /**
   * Create new image, erase all drawing safely
   * @param background_color Background color (RGBA)
   * */
  def new_image(background_color: Color) = {
    // Background
    g2d.setColor(background_color)
    g2d.fillRect(0, 0, this.width, this.height)
  }

  /**
   * Add circle in the image
   * @param position_x: The absolute position of x the cercle in the image. unit: pixel
   * @param position_y: The absolute position of y the cercle in the image. unit: pixel
   * @param rayon: The *rayon* of the cercle. Unit: pixel
   * @param color: Color of the circle.
   * */
  def addCercle(position_x: Int, position_y: Int, rayon: Int, color: Color): Unit = {
    g2d.setColor(color)
    g2d.fillOval(position_x, position_y, rayon, rayon)
  }
}
