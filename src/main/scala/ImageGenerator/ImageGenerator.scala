/**
 * This class allow to generate image.
 * It used Java2D (Graphic2D)
 * */
package ImageGenerator

import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage
import com.jhlabs.image.GaussianFilter

/**
 *
 * */
object ImageGenerator {

  var width = 400
  var height = 300

  var primaryColor: Color = new Color(125, 125, 0, 255)
  var secondaryColor: Color = new Color(0, 125, 125, 255)
  var thirdColor: Color = new Color(125, 0, 125, 255)
  val classColor = new Color(255, 255, 255, 70)

  var pattern: ImagePattern.ImagePattern = ImagePattern.Round


  def generate(): BufferedImage = {

    // Create an image with ARGB color space. A stands for alpha, and allow to control transparency
    val bufferedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
    val g2d = bufferedImage.createGraphics

    // Background
    g2d.setColor(this.primaryColor)
    g2d.fillRect(0, 0, this.width, this.height)

    // Round
    g2d.setColor(this.secondaryColor)
    g2d.fillOval(rpw(15), rph(30), rpw(30), rpw(30))

    g2d.setColor(this.thirdColor)
    g2d.fillOval(rpw(65), rph(0), rpw(10), rpw(10))

    // Glass filter
    g2d.setColor(classColor)
    g2d.fillRect(0, 0, this.width, this.height)

    // Call dispose without argument
    g2d.dispose

    val blurFilter = new GaussianFilter(30)
    blurFilter.filter(bufferedImage, null)

  }

  /**
   * Set dimension for image
   * @param dim Target dimension with (width, height) format.
   * */
  def setDim(dim: Tuple2[Int, Int]): Unit = {
    this.width = dim(0)
    this.height = dim(1)
  }

  /**
   * Set the pattern of the image. I.e. the form, background, blur, etc.
   * @param pattern The wanted pattern, define in ImagePattern enumeration
   * */
  def setPattern(pattern: ImagePattern.ImagePattern): Unit = {
    this.pattern = pattern
  }

  def setColor(primary: Color, secondary: Color, third: Color): Unit = {
    this.primaryColor = primary
    this.secondaryColor = secondary
    this.thirdColor = third
  }

  /**
   * Helping function to allow relative positionning. To not care about image dimension
   * @param percent Relative position value
   * @param dimSize Value of the size of the targeted dimension
   * */
  def relativePosition(percent: Float, dimSize: Int): Int = {
    val position: Float = (percent * dimSize) / 100
    position.toInt
  }
  /**
   * alias for [[relativePosition]]
   * */
  def rp = relativePosition

  /**
   * alias for [[relativePosition]] with set dimension to image width
   * */
  def rpw = relativePosition(_, this.width)

  /**
   * alias for [[relativePosition]] with set dimension to image height
   * */
  def rph = relativePosition(_, this.height)
}
