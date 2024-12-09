import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http

import javax.swing.*
import java.awt.*
import java.awt.event.*
import scala.concurrent.ExecutionContextExecutor

object AmbienceDiffuser extends App {

  val snapshotFolderName = "snapshots"

  implicit val system: ActorSystem = ActorSystem("AmbienceDiffuser")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // Initialize the JsonApiService
  val jsonApiService = new APIService(snapshotFolderName)

  // Start HTTP Server
  val bindingFuture = Http().bindAndHandle(jsonApiService.routes, "localhost", 8080)
  println(s"Server running at http://localhost:8080/")



  // Set up the list model to hold file names
  val listModel = new DefaultListModel[String]()
  
  // Create the FileListActor and the FileClient Actor
  val fileListActor = system.actorOf(Props(new FileListActor(snapshotFolderName, listModel)), "fileListActor")
  val fileClient = system.actorOf(Props(new FileClient(snapshotFolderName, fileListActor)), "fileClient")
  
  // Create the JFrame window
  val frame = new JFrame("File List in Full Screen")

  // Set the window to full screen using GraphicsEnvironment
  val graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment
  val graphicsDevice = graphicsEnvironment.getDefaultScreenDevice
  frame.setUndecorated(true)
  graphicsDevice.setFullScreenWindow(frame)

  // Create the JList with the human-readable file names
  val fileList = new JList[String](listModel)

  // Set the background color of the JList to light blue
  fileList.setBackground(new Color(140, 162, 180))

  // Modify the cell renderer to set a background color for each item if needed and center
  fileList.setCellRenderer(new DefaultListCellRenderer() {
    override def getListCellRendererComponent(
                                               list: JList[_], value: Any, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component = {
      val label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
      label match
        case jLabel: JLabel =>
          jLabel.setHorizontalAlignment(SwingConstants.CENTER)
          if (!isSelected) {
            jLabel.setBackground(new Color(140, 162, 180))
            jLabel.setOpaque(true)
          }
        case _ =>
      label
    }
  })

  // Focus the first item in the list
  fileList.setSelectedIndex(0)

  // Ensure the list is focusable and can capture key events
  fileList.setFocusable(true)

  // Add a key listener to handle "Enter" key press
  fileList.addKeyListener(new KeyAdapter() {
    override def keyPressed(e: KeyEvent): Unit = {
      if (e.getKeyCode == KeyEvent.VK_ENTER) {
        val selectedFileName = fileList.getSelectedValue
        println(s"File selected: $selectedFileName")
        // TODO : Add more instructions here
      }
    }
  })

  // Add the list to a scroll pane
  val scrollPane = new JScrollPane(fileList)
  frame.add(scrollPane)

  // Set the default close operation
  //  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  // Make the window visible
  frame.setVisible(true)

  // Explicitly request focus on the file list
  fileList.requestFocusInWindow()

  // Initially load the files
  fileListActor ! RefreshList
}
