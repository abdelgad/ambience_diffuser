package message

case class FileList(files: Set[String])
case class RequestFile(fileName: String)
case class FileContent(fileName: String, content: String)