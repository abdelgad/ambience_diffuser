package station.displayEngine

class Rain extends Engine {
  
  
  // ==========
  // | Engine |
  // ==========

  override def settings_pattern(): Unit = {
  }

  override def setup_pattern(): Unit = {
    frameRate(30)
  }

  override def draw_pattern(): Unit = {
  }

  override def ajustement_pattern(): Unit = {
  }
}

/**
 * 
 * @param display_fps Display fps. (Boolean) default = false
 * */
object rain_params {
  var display_fps: Boolean= false
}