import jssc.{SerialPort, SerialPortException}

object LedController {
  var serialPort: Option[SerialPort] = None

  // Ouvrir le port série
  def openPort(portName: String): Unit = {
    try {
      val port = new SerialPort(portName)
      port.openPort()
      port.setParams(
        SerialPort.BAUDRATE_9600,
        SerialPort.DATABITS_8,
        SerialPort.STOPBITS_1,
        SerialPort.PARITY_NONE
      )
      serialPort = Some(port)
      println(s"Port série $portName ouvert.")
    } catch {
      case ex: SerialPortException =>
        println(s"Erreur d'ouverture du port série : ${ex.getMessage}")
    }
  }

  // Envoyer une commande
  def sendLedCommand(animationType: String, speed: String, color: (Int, Int, Int)): Boolean = {
    serialPort match {
      case Some(port) =>
        try {
          val command = s"TYPE:$animationType|SPEED:$speed|COLOR:${color._1},${color._2},${color._3}%"
          port.writeString(command)
          println(s"Commande envoyée : $command")
          true
        } catch {
          case ex: SerialPortException =>
            println(s"Erreur d'envoi : ${ex.getMessage}")
            false
        }
      case None =>
        println("Port série non ouvert.")
        false
    }
  }

  // Fermer le port série
  def closePort(): Unit = {
    serialPort.foreach { port =>
      try {
        port.closePort()
        println("Port série fermé.")
      } catch {
        case ex: SerialPortException =>
          println(s"Erreur lors de la fermeture du port : ${ex.getMessage}")
      }
    }
    serialPort = None
  }

  def sendLedCommandOnce(portName: String, animationType: String, speed: String, color: (Int, Int, Int)): Unit = {
    // Ouvrir le port
    openPort(portName)
    Thread.sleep(500) // Attendre que le port soit prêt

    // Envoyer la commande
    if (serialPort.isDefined) {
      sendLedCommand(animationType, speed, color)
    }

    // Fermer le port
    closePort()
  }

  // Programme principal
  def main(args: Array[String]): Unit = {
    val portName = "COM5"
    openPort(portName)

    if (serialPort.isDefined) {
      while (true) {
        sendLedCommand("CASCADE", "FAST", (255, 0, 0))
        Thread.sleep(5000)
        sendLedCommand("CASCADE", "FAST", (0, 0, 255))
        Thread.sleep(5000)
      }
    }

    closePort()
  }
}
