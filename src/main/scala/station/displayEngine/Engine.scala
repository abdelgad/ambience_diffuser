package station.displayEngine

import processing.core.PApplet

abstract class Engine extends PApplet {
  
  private val op: engine_param = engine_param_object // Object parameter that hold instance parameters
  
  override def settings(): Unit = {
    if(op.full_screen) fullScreen()
    else size(op.width, op.height)
    settings_pattern()
  }

  /**
   * Settings for pattern. Set class and windows settings.
   * @Note Optionnal implementation
   * */
  def settings_pattern(): Unit = {}

  override def setup(): Unit = {
    background(op.bg_color(0), op.bg_color(1), op.bg_color(2))
    setup_pattern()
  }

  /**
   * Setup for pattern use. Setup graphic, executed once.
   * @Note Optionnal implementation
   * */
  def setup_pattern(): Unit = {}

  override def draw(): Unit = {
    background(op.bg_color(0), op.bg_color(1), op.bg_color(2))
    draw_pattern()
    ajustement_pattern()
  }

  /**
   * Draw pattern element. Executed at each frame.
   * @Note Optionnal implementation
   * */
  def draw_pattern(): Unit = {}

  /**
   * Ajustement pour la prochaine frame. Executed once at the end of an iteration.
   * @Note Optionnal implementation
   * */
  def ajustement_pattern(): Unit = {}

  /**
   * Relative distance by width. Return x% * width. Allow relative positionning.
   * @param percent Relative distance to compute. Unit: percent (Float)
   * */
  def rpw(percent: Float): Float = (this.width * percent / 100)

  /**
   * Relative distance by height. Return x% * height. Allow relative positionning.
   * @param percent Relative distance to compute. Unit: percent (Float)
   * */
  def rph(percent: Float): Float = (this.height * percent / 100)

}

class engine_param {
  var width: Int = 1280
  var height: Int = 720
  var bg_color: Color = (30, 30, 30)
  var full_screen: Boolean = true
}

object engine_param_object extends engine_param
