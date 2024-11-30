package ImageGenerator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers._

import java.awt.Color
import java.awt.image.BufferedImage
import com.grouph.BasicDisplay


class ImageGeneratorTests extends AnyFlatSpec {

  val width = 1280
  val height = 720

  // Color(red,green,blue,alpha)
  val primary = new Color(255, 0, 0, 255)
  val secondary = new Color(0, 255, 0, 255)
  val third = new Color(0, 0, 255, 255)

  val IG: ImageGenerator = new ImageGenerator(width, height)

  "Image" should "display" in {
    IG new_image primary

    BasicDisplay displayImage(IG generate_image true)
    Thread.sleep(4000)
  }

  "Generate image" should "not throw an exception" in {
    IG new_image primary
    IG.addCercle(500, 500, 100, secondary)

    noException should be thrownBy {
      val image: BufferedImage = IG generate_image true
    }
  }

}
