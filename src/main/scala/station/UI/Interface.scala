package station.UI

import message.{InitializeUI, SelectDown, SelectUp, Selected}

import java.awt.event.{KeyAdapter, KeyEvent}
import java.awt.{Color, Component, Font, GraphicsEnvironment}
import javax.swing.{DefaultListCellRenderer, DefaultListModel, JFrame, JLabel, JList, JScrollPane, ListModel, SwingConstants}
import akka.actor.{Actor, ActorRef}
import station.{Snapshot, SnapshotFile, snapLoader}
import station.algorithme.AnimationController


class Interface(fileListActor: ActorRef) extends Actor {

  val bg_color: Color = new Color(140, 162, 180)
  val selected_color: Color = new Color(140, 162, 180)

  val element_h_pos = SwingConstants.CENTER
  val fontName: String = "Calibri"
  val fontSize: Int = 20

  var cursor: Int = 0
  var fileList: JList[SnapshotFile] = _

  var frame: JFrame = _

  var processing_is_active: Boolean = false

  override def receive: Receive = {
    case InitializeUI(listModel) =>
      init(listModel)
    case SelectDown =>
      move_cursor(-1)
    case SelectUp =>
      move_cursor(1)
    case Selected =>
      selecting
  }

  /**
   * Initialize the interface. Has to be called once.
   * */
  private def init(listModel: DefaultListModel[SnapshotFile]) = {
    frame = new JFrame("File List in Full Screen")
    val fontCust = new Font(fontName, Font.BOLD, fontSize)

    // Used to set fullscreen
    val graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment
    val graphicsDevice = graphicsEnvironment.getDefaultScreenDevice
    frame.setUndecorated(true)
    graphicsDevice.setFullScreenWindow(frame)

    // Create List element
    fileList = new JList[SnapshotFile](listModel)

    fileList.setBackground(bg_color)

    fileList.setCellRenderer(new DefaultListCellRenderer() {
      override def getListCellRendererComponent(list: JList[_], value: Any, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component = {
        val label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
        label match
          case jLabel: JLabel =>
            jLabel.setHorizontalAlignment(element_h_pos)
            jLabel.setFont(fontCust)
            if (!isSelected) {
              jLabel.setBackground(selected_color)
              jLabel.setOpaque(true)
            }
          case _ =>
        label
      }
    })

    fileList.setSelectedIndex(cursor)
    fileList.setFocusable(true)

    val scrollPane = new JScrollPane(fileList)
    frame.add(scrollPane)

    frame.setVisible(true)

    fileList.requestFocusInWindow()
  }

  /**
   * Allow to move the selection up and down
   * @param move value to move by. If positive, move down, if negative, move up.
   * */
  private def move_cursor(move: Int) = {
    cursor += move
    if (cursor < 0) cursor = 0
    if (cursor >= fileList.getModel.getSize) cursor = fileList.getModel.getSize - 1
    fileList.setSelectedIndex(cursor)
  }

  private def selecting = {
    if (processing_is_active) stop_processing
    else run_processing
  }

  private def run_processing = {
    val current_element: SnapshotFile = fileList.getSelectedValue.asInstanceOf[SnapshotFile]
    val snap = snapLoader.load_snap(current_element.get_fullname)
    AnimationController.triggerAnimations(snap)
    frame.setVisible(false)
    processing_is_active = true
  }

  private def stop_processing = {
    AnimationController.stop_annimation()
    frame.setVisible(true)
    processing_is_active = false
  }

}
