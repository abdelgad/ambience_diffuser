package station.displayEngine

import processing.core.{PApplet, PConstants, PGraphics}

class Rain extends Engine {

  this.op = rain_params
  val rop = rain_params

  val blur_level = 8

  var building_max_height: Int = _
  val building_color = (128,128,128)
  val nbr_buildings: Int = 8

  val nbr_drops: Int = 20
  val drop_color: Color = (212, 241, 248)

  type drop = Tuple5[Float, Int, Int, Float, Float] // speed, width, height, x, y
  private var drops: List[drop] = _

  def set_relative_val = {
    building_max_height = rph(70).toInt
  }

  // ==========
  // | Engine |
  // ==========

  override def settings_pattern(): Unit = {
  }

  override def setup_pattern(): Unit = {
    set_relative_val
    setup_drops
    create_buildings_graphic
  }

  override def draw_pattern(): Unit = {
    draw_buildings
    draw_drops
  }

  override def ajustement_pattern(): Unit = {
    update_drops
  }

  // =========
  // | Drops |
  // =========

  /**
   * Generate List with drop.
   * */
  private def setup_drops: Unit = {
    drops = (0 until nbr_drops).map(x => generate_drop).toList
  }

  /**
   * Generate a random drop
   * */
  private def generate_drop: drop = {
    val size_factor = random(0.5f, 1.5f) // size affect by how far is a drop
    ((rph(2) + random(rph(2)))*size_factor, (rpw(5e-1)*size_factor).toInt, (rph(1)*size_factor).toInt + random(rph(1)).toInt, random(0f, width.toFloat), 0f)
  }

  /**
   * Update all drops elements to match with the movement
   * */
  private def update_drops = {
    drops = drops.map(d => {
      if (d(4) > height) generate_drop // Si la drop a atteinds le bas de l'écran
      else (d(0), d(1), d(2), d(3), d(4) + d(0))
    })
  }

  /**
   *  Draw all drops
   * */
  private def draw_drops = {
    // drop = [speed, width, height, x, y]
    fill(drop_color(0), drop_color(1), drop_color(2))
    drops.foreach(d => ellipse(d(3), d(4), d(1), d(2)))
  }

  // ============
  // | Building |
  // ============

  var buildings_graphic: PGraphics = _

  /**
   * Generate buildings graphic to reduce draw ressources usage, and then gain fps.
   * */
  private def create_buildings_graphic = {
    val graphic_height = building_max_height + mg
    buildings_graphic = createGraphics(width, graphic_height)
    buildings_graphic.beginDraw()
    buildings_graphic.clear()
    (0 until nbr_buildings).foreach(add_building_graphic)
    buildings_graphic.endDraw()
    buildings_graphic.filter(PConstants.BLUR, blur_level)
  }

  private def add_building_graphic(index_building: Int) = {
    val graphic_height = building_max_height + mg
    val building_height = random(building_max_height*0.3f, building_max_height).toInt
    val building_width = (rpw(10) + random(rpw(10))).toInt
    val building_x = (index_building * width/nbr_buildings) + random(rpw(5))
    val building_y = graphic_height - building_height

    val building_hue: Color = dark_hue_map(building_color, random(0.2f, 0.6f))

    buildings_graphic.fill(building_hue(0), building_hue(1), building_hue(2))
    buildings_graphic.rect(building_x, building_y, building_width, building_height)
  }

  /**
   * Draw buildings elements.
   * */
  private def draw_buildings: Unit = {
    image(buildings_graphic, 0, height - building_max_height - mg)
  }

}

/**
 *
 * */
object rain_params extends engine_param {
}

object Rain {
  def main(args: Array[String]): Unit = {
    rain_params.display_fps = true
    rain_params.bg_color = (30, 30, 30)

    PApplet.main(classOf[Rain].getName)
  }
}