package station

import akka.actor.{Actor, ActorRef}
import jssc.{SerialPort, SerialPortEvent, SerialPortEventListener, SerialPortException}
import message.{SelectDown, SelectUp, Selected, Ledmessage}


class Arduino(portName: String, UI: ActorRef) extends Actor {
  var serialPort: Option[SerialPort] = None
  
  openPort(portName)

  override def receive: Receive = {
    case Ledmessage(animationType,speed,color) => sendLedCommand(animationType,speed,color)
  }
  
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

      //Listener pour lire les données du port
      port.addEventListener(new SerialPortEventListener {
        override def serialEvent(event: SerialPortEvent): Unit = {
          if (event.isRXCHAR && event.getEventValue > 0) {
            try {
              val receivedData = port.readString(event.getEventValue)
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
      case "U"  => UI ! SelectUp
      case "D"  => UI ! SelectDown
      case "C"  => UI ! Selected
      case _    => println(s"Commande inconnue : $input")
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
}
