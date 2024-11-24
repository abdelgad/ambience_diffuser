package ImageGenerator

import org.scalatest.funsuite.AnyFunSuite

import java.awt.{Color, Graphics}
import java.awt.image.BufferedImage
import javax.swing.{JFrame, JPanel, WindowConstants}
import be.grouph.BasicDisplay

import javax.imageio.ImageIO
import java.io.File


class ImageGeneratorTests extends AnyFunSuite {

  val width = 1280
  val height = 720
  val dim = (width, height)

  val pattern = ImagePattern.Round

  // Color(red,green,blue,alpha)
  val primary = new Color(255, 0, 0, 255)
  val secondary = new Color(0, 255, 0, 255)
  val third = new Color(0, 0, 255, 255)

  ImageGenerator.setDim(dim)
  ImageGenerator.setPattern(pattern)
  ImageGenerator.setColor(primary, secondary, third)

  val image: BufferedImage = ImageGenerator.generate()
  BasicDisplay.displayImage(image)

  val outputFile = new File("./src/test/resources/output/test_1.png")
  ImageIO.write(image, "png", outputFile)
}
