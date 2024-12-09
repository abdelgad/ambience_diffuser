package station

import akka.actor.Actor

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.swing.{DefaultListModel, SwingUtilities}


case object RefreshList
case object SelectDown
case object SelectUp
case object Selected


// Define the Actor that handles the file list and UI updates
class FileListActor(snapshotsFolderName: String, listModel: DefaultListModel[String]) extends Actor {

  // Handle messages
  override def receive: Receive = {
    case RefreshList =>
      loadFiles()  // Refresh the list when the message is received
      println("File list refreshed")
    case SelectDown =>
      println("Select Down")
    case SelectUp =>
      println("Select Up")
    case Selected =>
      println("Selected")
  }


  // Combined helper function to extract and format the date from the file name
  private def extractAndFormatDate(fileName: String): (Date, String) = {
    val baseName = fileName.replace("snapshot_", "").replace(".json", "")
    val timestampString = baseName.split("_").lastOption.getOrElse("")

    // Truncate the timestamp string to milliseconds
    val truncatedTimestamp = timestampString.take(23) // Keep up to milliseconds (yyyy-MM-dd'T'HH:mm:ss.SSS)

    // Create the SimpleDateFormat object
    val timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss.SSS") // Milliseconds precision

    try {
      val timestamp = timestampFormat.parse(truncatedTimestamp)

      // Format for human-readable display
      val humanReadableFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss")
      val formattedDate = humanReadableFormat.format(timestamp)

      (timestamp, formattedDate) // Return both the Date object and the formatted string
    } catch {
      case _: Exception => (new Date(0), baseName) // Return the base name if there's an error in parsing
    }
  }


  // Load the files and update the list
  private def loadFiles(): Unit = {
    val snapshotsFolder = new File(snapshotsFolderName)
    if (snapshotsFolder.exists && snapshotsFolder.isDirectory) {
      val files = snapshotsFolder.listFiles()
      println(s"Found ${files.length} files.")  // Debugging statement
      val sortedFiles = files
        .filter(_.isFile)
        .sortBy(file => extractAndFormatDate(file.getName)._1) // Sort by Date object

      SwingUtilities.invokeLater(() => {
        listModel.clear() // Clear the existing items
        sortedFiles.foreach { file =>
          val formattedName = extractAndFormatDate(file.getName)._2 // Get the formatted name
          listModel.addElement(formattedName)
        }
      })
    } else {
      println("Snapshots folder does not exist or is not a directory.") // Debugging statement
    }
  }
  
  private def moveCursorUp = {
    listModel.get(0)
  }
}