package station

import akka.actor.Actor
import akka.actor.{Actor, ActorRef}

import java.io.File
import java.nio.file.{Files, Paths, StandardOpenOption}

case object NotifySynchronization
case class FileList(files: Set[String])
case class RequestFile(fileName: String)
case class FileContent(fileName: String, content: String)

class FileClient(localFolderPath: String) extends Actor{
  override def receive: Receive = {
    case FileList(serverFiles) =>
      println(s"Received file list from server: $serverFiles")

      val folder = new File(localFolderPath)
      if (!folder.exists()) folder.mkdir()

      val localFiles = folder.listFiles().map(_.getName).toSet

      // Request missing files
      serverFiles.diff(localFiles).foreach { missingFile =>
        println(s"Requesting missing file: $missingFile")
        sender() ! RequestFile(missingFile)
      }

      // Delete extra files
      localFiles.diff(serverFiles).foreach { extraFile =>
        println(s"Deleting extra file: $extraFile")
        deleteFile(extraFile)
      }

    case FileContent(fileName, content) =>
      println(s"Synchronizing file: $fileName")
      writeFile(fileName, content)
  }

  private def writeFile(fileName: String, content: String): Unit = {
    val filePath = Paths.get(localFolderPath, fileName)
    Files.write(filePath, content.getBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  }

  private def deleteFile(fileName: String): Unit = {
    val filePath = Paths.get(localFolderPath, fileName)
    if (Files.exists(filePath)) Files.delete(filePath)
  }
}
