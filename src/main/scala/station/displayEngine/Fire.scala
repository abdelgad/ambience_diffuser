package station.displayEngine

import processing.core.{PApplet, PConstants, PGraphics}

class Fire extends Engine {
  this.op = fire_params
  val fop = fire_params

  def set_relative_val = {}

  // ==========
  // | Engine |
  // ==========

  override def settings_pattern(): Unit = {
  }

  override def setup_pattern(): Unit = {
    set_relative_val
  }

  override def draw_pattern(): Unit = {
    draw_flame
  }

  override def ajustement_pattern(): Unit = {
  }

  // =========
  // | Flame |
  // =========

  def draw_flame = {
    fill(0, 0, 0)
    triangle(0, height, width, height, width/2, height/2)
  }

}

object fire_params extends engine_param {
}

object Fire {
  def main(args: Array[String]): Unit = {
    PApplet.main(classOf[Fire].getName)
  }
}