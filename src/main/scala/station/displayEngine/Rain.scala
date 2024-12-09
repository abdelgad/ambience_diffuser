package station.displayEngine

import processing.core.{PApplet, PConstants, PGraphics}
import scala.util.Random

class Rain extends Engine {

  this.op = rain_params
  val rop = rain_params

  val blur_building = rop.blur_building
  val blur_cloud = rop.blur_cloud

  var building_max_height: Int = _
  var building_graphic_height: Int = _
  val building_color = rop.building_color
  val nbr_buildings: Int = rop.nbr_buildings

  var cloud_max_height: Int = _
  var cloud_graphic_height: Int = _
  val cloud_color = rop.cloud_color
  val nbr_cloud = rop.nbr_cloud

  var base_speed_drop: Int = rop.base_speed_drop
  val nbr_drops: Int = rop.nbr_drops
  val drop_color: Color = rop.drop_color

  type drop = Tuple5[Float, Int, Int, Float, Float] // speed, width, height, x, y
  private var drops: List[drop] = _

  def set_relative_val = {
    building_max_height = rph(rop.relative_building_max_height).toInt
    building_graphic_height = building_max_height + mg

    cloud_max_height = rph(rop.relative_cloud_max_height).toInt
    cloud_graphic_height = cloud_max_height + mg
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
    create_cloud_graphic
  }

  override def draw_pattern(): Unit = {
    draw_buildings
    draw_drops
    draw_clouds
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
    (random(rph(base_speed_drop*0.5f), rph(base_speed_drop*1.5f))*size_factor, (rpw(2e-1)*size_factor).toInt, (rph(1)*size_factor).toInt + random(rph(1)).toInt, random(0f, width.toFloat), 0f)
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
    noStroke()
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

    buildings_graphic = createGraphics(width, building_graphic_height)
    buildings_graphic.beginDraw()
    buildings_graphic.clear()

    Random.shuffle((0 until nbr_buildings).toList).foreach(add_building_graphic)

    buildings_graphic.endDraw()
    buildings_graphic.filter(PConstants.BLUR, blur_building)
  }

  /**
   * Add a building element in building_graphic.
   * @param index_building The index of building to create. It knows the total nbr of building, then it can compute x position of building.
   * */
  private def add_building_graphic(index_building: Int) = {
    val building_height = random(building_max_height*0.3f, building_max_height).toInt
    val building_width = (rpw(10) + random(rpw(10))).toInt
    val building_x = (index_building * width/nbr_buildings) + random(rpw(5))
    val building_y = building_graphic_height - building_height

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

  // =========
  // | Cloud |
  // =========

  var cloud_graphic: PGraphics = _

  /**
   * Generate cloud graphic to reduce draw ressources usage, and then gain fps.
   * */
  private def create_cloud_graphic = {
    cloud_graphic = createGraphics(width, cloud_graphic_height)
    cloud_graphic.beginDraw()
    cloud_graphic.clear()
    cloud_graphic.noStroke()

    Random.shuffle((0 until nbr_cloud).toList).foreach(add_cloud_graphic)

    cloud_graphic.endDraw()
    cloud_graphic.filter(PConstants.BLUR, blur_cloud)
  }

  /**
   * Add a cloud element in cloud_graphic.
   *
   * @param index The index of cloud to create. It knows the total nbr of cloud, then it can compute x position of cloud.
   * */
  private def add_cloud_graphic(index: Int) = {

    val cloud_x = (index * width / nbr_cloud) + random(rpw(5))
    val cloud_y = 0

    val cloud_width = (width/nbr_cloud)*2.5f +random(rpw(5))
    val cloud_height = random(cloud_max_height*0.3f, cloud_max_height) * 2 //'cause only half is visible

    val hue = bright_hue_map(cloud_color, random(0.4f, 0.8f))

    fill(hue(0), hue(1), hue(2))
    cloud_graphic.ellipse(cloud_x, cloud_y, cloud_width, cloud_height)
  }

  /**
   * Draw cloud elements.
   * */
  private def draw_clouds: Unit = {
    image(cloud_graphic, 0, 0)
  }

}

/**
 * Object that contains rain engine parameters
 *
 * @param width                        Window width. Unit: pixel (Int)
 * @param height                       Window height. Unit: pixel (Int)
 * @param full_screen                  Enable fullscreen if true. (Boolean)
 * @param display_fps                  Display fps. (Boolean) default = false
 * @param bg_color                     Color of the background (Tuple)
 * @param blur_building                Level of blur to apply to buildings graphic (Int)
 * @param blur_cloud                   Level of blur to apply to cloud graphic (Int)
 * @param relative_building_max_height The maximum height that a building can reach. Unit: percent of the height (Float)
 * @param building_color               The base color of the building. will create darker hue to match the scene. (Tuple3)
 * @param nbr_building                 The number of building to display (Int)
 * @param relative_cloud_max_width     The maximum height a cloud will reach. Unit: percent of the height (Float)
 * @param cloud_color                  The base color of the cloud. will create brighter hue to match the scene. (Tuple3)
 * @param nbr_cloud                    The number of cloud that will be display. (Int)
 * @param base_speed_drop              The mean speed of drop. Unit: pixel/frame(Int)
 * @param nbr_drops                    The number of drop being dropped. (Int)
 * @param drop_color                   The color of drop (Tuple3)
 * */
object rain_params extends engine_param {
  var blur_building = 8
  var blur_cloud = 8

  var relative_building_max_height: Float = 70
  var building_color = (128, 128, 128)
  var nbr_buildings: Int = 8

  var relative_cloud_max_height: Float = 30
  var cloud_color = (86, 86, 86)
  var nbr_cloud = 20

  var base_speed_drop: Int = 3
  var nbr_drops: Int = 30
  var drop_color: Color = (212, 241, 248)

  def setup_pattern(rain: RainPattern) = {
    bg_color = rain.background_color

    blur_building = rain.blur_building
    blur_cloud = rain.blur_cloud
    relative_building_max_height = rain.relative_building_max_height
    building_color = rain.building_color
    nbr_buildings = rain.nbr_building
    relative_cloud_max_height = rain.relative_cloud_max_width
    cloud_color = rain.cloud_color
    nbr_cloud = rain.nbr_cloud
    base_speed_drop = rain.base_speed_drop
    nbr_drops = rain.nbr_drops
  }

}

object Rain {
  def main(args: Array[String]): Unit = {
    rain_params.display_fps = true
    rain_params.bg_color = (30, 30, 30)

    PApplet.main(classOf[Rain].getName)
  }
}