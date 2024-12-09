package message

import javax.swing.DefaultListModel

case class FileList(files: Set[String])
case class RequestFile(fileName: String)
case class FileContent(fileName: String, content: String)
case object SelectDown
case object SelectUp
case object Selected
case class ReloadListUI()
case class InitializeUI(listModel: DefaultListModel[String])