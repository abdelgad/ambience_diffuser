package be.grouph

import javax.swing._
import java.awt._
import java.awt.image.BufferedImage

object BasicDisplay {

  // Method to display the generated image
  def displayImage(image: BufferedImage): Unit = {

    // Create windows that fit the image
    val frame = new JFrame("Image Display")
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setSize(image.getWidth, image.getHeight)

    // Create the component that contains the image
    val panel = new JPanel {
      override def paintComponent(g: Graphics): Unit = {
        super.paintComponent(g)
        g.drawImage(image, 0, 0, null)
      }
    }

    frame.add(panel)
    frame.setVisible(true)
  }
}