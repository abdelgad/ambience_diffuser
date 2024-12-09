import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Route
import spray.json.*
import spray.json.DefaultJsonProtocol.*

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import java.nio.file.{Files, Paths}
import scala.util.{Failure, Success}

class APIService(jsonFolder: String)(implicit ec: ExecutionContext) {
  // Fetch the list of JSON files in the folder
  private def getJsonFiles: List[String] = {
    val path = Paths.get(jsonFolder)
    if (Files.exists(path) && Files.isDirectory(path)) {
      Files.list(path).toArray.map(_.toString).toList.filter(_.endsWith(".json"))
    } else {
      List.empty
    }
  }

  // Read and parse all JSON files into a list of JsValue
  private def readJsonFiles: Future[List[JsValue]] = Future {
    getJsonFiles.flatMap { filePath =>
      val fileSource = Source.fromFile(filePath)
      try {
        Some(fileSource.mkString.parseJson) // Parse each file's content into JsValue
      } catch {
        case _: Exception => None
      } finally {
        fileSource.close()
      }
    }
  }

  // Define the API routes
  val routes: Route =
    pathPrefix("api" / "all-files") {
      get {
        onComplete(readJsonFiles) {
          case Success(jsonList) =>
            complete(HttpEntity(ContentTypes.`application/json`, jsonList.toJson.prettyPrint))
          case Failure(ex) =>
            complete(500, s"Error reading files: ${ex.getMessage}")
        }
      }
    }
}
