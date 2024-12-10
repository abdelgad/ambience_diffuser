package station

import akka.actor.{Actor, ActorSystem, Props}
import message.InitializeUI
import station.UI.Interface
import station.audio.SoundGenerator

import javax.swing.*
import java.awt.*
import java.awt.event.*

object AmbienceDiffuser extends App {

  val snapshotFolderName = "snapshots"
  val arduinoPort = "/dev/ttyACM0"

  val system = ActorSystem("AmbienceDiffuser")

  // Set up the list model to hold file names
  val listModel = new DefaultListModel[SnapshotFile]()
  
  // Create the actors
  val soundActor =  system.actorOf(Props(new SoundGenerator()), "sound")
  val fileListActor = system.actorOf(Props(new FileListActor(snapshotFolderName, listModel)), "fileListActor")
  val UIActor = system.actorOf(Props(new Interface(fileListActor)), "UI")
  val fileClient = system.actorOf(Props(new FileClient(snapshotFolderName, fileListActor)), "fileClient")
  
  val arduinoActor = system.actorOf(Props(new Arduino(arduinoPort, UIActor)), "arduino")

  UIActor ! InitializeUI(listModel)
  fileListActor ! RefreshList
}
