import jssc.{SerialPort, SerialPortEvent, SerialPortEventListener, SerialPortException}

object DifuseurArduinoInterface {
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

      // Ajouter un listener pour lire les données du port
      port.addEventListener(new SerialPortEventListener {
        override def serialEvent(event: SerialPortEvent): Unit = {
          if (event.isRXCHAR && event.getEventValue > 0) {
            try {
              val receivedData = port.readString(event.getEventValue)
              println(s"Données reçues : $receivedData")
              handleEncoderInput(receivedData.trim)
            } catch {
              case ex: SerialPortException =>
                println(s"Erreur de lecture : ${ex.getMessage}")
            }
          }
        }
      })

      serialPort = Some(port)
      println(s"Port série $portName ouvert.")
    } catch {
      case ex: SerialPortException =>
        println(s"Erreur d'ouverture du port série : ${ex.getMessage}")
    }
  }

  // Envoyer une commande LED à l'Arduino
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

  // Gérer l'entrée de l'encodeur
  def handleEncoderInput(input: String): Unit = {
    input match {
      case "U"    => sendLedCommand("CASCADE", "FAST", (0, 0, 255)) // Cascade bleue
      case "D"  => sendLedCommand("CASCADE", "FAST", (255, 0, 0)) // Cascade rouge
      case "C" => sendLedCommand("CASCADE", "FAST", (0, 255, 0)) // Cascade verte
      case _       => println(s"Commande inconnue : $input")
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

  // Programme principal
  def main(args: Array[String]): Unit = {
    val portName = "COM5" // Modifiez selon le port utilisé
    openPort(portName)

    println("En attente des entrées du rotary encoder...")
    scala.io.StdIn.readLine() // Bloque le programme pour maintenir l'écoute
    closePort()
  }
}
