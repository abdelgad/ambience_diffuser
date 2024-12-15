package station.displayEngine

import processing.core.{PApplet, PConstants, PGraphics}

import scala.math.{cos, sin}

/**
 * Class that implement orbite pattern in video engine.
 *
 * Use orbite_params to set argument !
 * @tparam width Window width. Unit: pixel (Int)
 * @tparam height Window height. Unit: pixel (Int)
 * @tparam bg_color Color of the background (Tuple)
 * @tparam erw Eclipse radius max width. Used to modify orbite movement. Unit: Percent of total width (Float)
 * @tparam erh Eclipse radius max height. Used to modify orbite movement. Unit: Percent of total height (Float)
 * @tparam ang_speed Angular speed. Use to describe orbite speed movement. Unit: angle by frame (Float)
 * @tparam planet_radius Planet radius. Unit: Percent of total width (Float)
 * @tparam sattelite_radius Sattelite radius. Unit: Percent of total width (Float)
 * @tparam faraway_planet_radius Far away planet radius. Unit: Percent of total width (Float)
 * @tparam nbr_stars Number of star in background. Unit: stars (Int)
 * @tparam planet_color Color of the planet RGB (Tuple3)
 * @tparam sattelite_color Color of the sattelite RGB (Tuple3)
 * @tparam faraway_planet_color Color of the far away planet RGB (Tuple3)
 * @tparam display_fps Display fps. (Boolean) default = False
 * @tparam blur_level Level of blur. (Int) default = 5
 * */
class Orbite extends Engine {

  this.op = orbite_params
  val oop = orbite_params

  var angle: Float = 0 // Angle position for mouvement

  var centerX: Int = _ // Centre X du mouvement
  var centerY: Int = _ // Centre Y du mouvement

  var x_faraway: Int = _
  var y_faraway: Int = _

  var aerw: Float = _ //absolute eclipse radius max width (unit: distance)
  var aerh: Float = _ //absolute eclipse radius max height (unit: distance)

  var apr: Int = _ //absolute planet radius (unit: pixel)
  var asr: Int = _ //absolute sattelite radius (unit: pixel)
  var afr: Int = _ //absolute far away planet radius (unit: pixel)

  val ang_speed: Float = oop.ang_speed
  val nbr_stars: Int = oop.nbr_stars

  val planet_color: Color = oop.planet_color
  val sattelite_color: Color = oop.sattelite_color
  val faraway_color: Color = oop.faraway_planet_color

  type star = Tuple3[Float, Float, Float] // x, y, brightness
  var stars: List[star] = _ // Coordonnée et luminosité des étoiles

  val blur_level: Int = oop.blur_level

  def set_relative_val = {
    centerX = super.rpw(25).toInt
    centerY = super.rph(50).toInt

    x_faraway = super.rpw(80).toInt
    y_faraway = super.rph(30).toInt

    aerw = super.rpw(oop.erw)
    aerh = super.rph(oop.erh)

    apr = super.rpw(oop.planet_radius).toInt
    asr = super.rpw(oop.sattelite_radius).toInt
    afr = super.rpw(oop.faraway_planet_radius).toInt
  }

  // ==========
  // | Engine |
  // ==========

  override def setup_pattern(): Unit = {
    setup_stars(nbr_stars)
    set_relative_val
    create_faraway_graphic
    create_planet_graphic
    create_sattelite_graphic
  }

  override def draw_pattern(): Unit = {
    draw_star
    draw_faraway
    if(is_orbite_top) draw_planet
    draw_sattelite
    if(!is_orbite_top) draw_planet
  }

  override def ajustement_pattern(): Unit = {
    // Compute next angle.
    angle += ang_speed
  }

  // =========
  // | Stars |
  // =========

  /**
   * Generate List with stars coordinates and luminosities with random value.
   * @param nbr_stars Number stars to generate. Unit: star (Int)
   * @note Side effect: Change this.stars value.
   * */
  private def setup_stars(nbr_stars: Int): Unit = {
    this.stars = (0 until nbr_stars).map(x => {
      (random(0f, this.width.toFloat), random(0f, this.height.toFloat), random(10f, 150f))
    }).toList
  }

  /**
   * Draw stars on main graphic.
   * */
  private def draw_star: Unit = {
    // Star = Tuple3[position_X: Float, position_Y: Float, luminosité: Float]
    this.stars.foreach { star => {
      fill(200 - star(2))
      rect(star(0), star(1), 5, 5)
    }}
  }

  // ===============
  // | Main Planet |
  // ===============
  
  var planet_graph: PGraphics = _

  /**
   * Generate planet graphic to reduce draw ressources usage, and then gain fps.
   * */
  private def create_planet_graphic: Unit = {
    this.planet_graph = createGraphics(apr * 2 + mg * 2, apr * 2 + mg * 2)
    this.planet_graph.beginDraw()
    this.planet_graph.clear()
    this.planet_graph.noStroke()
    this.planet_graph.fill(planet_color(0), planet_color(1), planet_color(2))
    this.planet_graph.ellipse(apr + mg, apr + mg, apr * 2, apr * 2)
    this.planet_graph.endDraw()
    this.planet_graph.filter(PConstants.BLUR, blur_level)
  }

  /**
   * Draw planet element.
   * */
  private def draw_planet: Unit = {
    image(this.planet_graph, centerX-apr-mg, centerY-apr-mg)
  }

  // =============
  // | Sattelite |
  // =============

  var sattelite_graph: PGraphics = _

  /**
   * Get x coordinates through orbite movement
   * */
  private def x = centerX + aerw * cos(angle).toFloat

  /**
   * Get y coordinates through orbite movement
   * */
  private def y = centerY + aerh * sin(angle).toFloat

  /**
   * Is sattelite behind or in front of planet
   * */
  private def is_orbite_top = 0 < sin(angle)

  /**
   * Generate sattelite graphic to reduce draw ressources usage, and then gain fps.
   * */
  private def create_sattelite_graphic = {
    this.sattelite_graph = createGraphics(asr * 2 + mg * 2, asr * 2 + mg * 2)
    this.sattelite_graph.beginDraw()
    this.sattelite_graph.clear()
    this.sattelite_graph.noStroke()
    this.sattelite_graph.fill(sattelite_color(0), sattelite_color(1), sattelite_color(2))
    this.sattelite_graph.ellipse(asr + mg, asr + mg, asr * 2, asr * 2)
    this.sattelite_graph.endDraw()
    this.sattelite_graph.filter(PConstants.BLUR, blur_level)
  }

  /**
   * Draw sattelite element.
   * */
  private def draw_sattelite = {
    image(this.sattelite_graph, x-asr-mg, y-asr-mg)
  }

  // ==================
  // | Faraway planet |
  // ==================

  var faraway_graph: PGraphics = _

  /**
   * Generate faraway planet graphic to reduce draw ressources usage, and then gain fps.
   * */
  private def create_faraway_graphic = {
    this.faraway_graph = createGraphics(afr * 2 + mg * 2, afr * 2 + mg * 2)
    this.faraway_graph.beginDraw()
    this.faraway_graph.clear()
    this.faraway_graph.noStroke()
    this.faraway_graph.fill(faraway_color(0), faraway_color(1), faraway_color(2))
    this.faraway_graph.ellipse(afr + mg, afr + mg, afr * 2, afr * 2)
    this.faraway_graph.endDraw()
    this.faraway_graph.filter(PConstants.BLUR, blur_level)
  }

  /**
   * Draw faraway planet element.
   * */
  private def draw_faraway = {

    image(this.faraway_graph, x_faraway - afr - mg, y_faraway - afr - mg)
  }
}

/**
 * Object that contains orbite engine parameters
 *
 * @param width                  Window width. Unit: pixel (Int)
 * @param height                 Window height. Unit: pixel (Int)
 * @param full_screen            Enable fullscreen if true. (Boolean)
 * @param bg_color               Color of the background (Tuple)
 * @param erw                    Eclipse radius max width. Used to modify orbite movement. Unit: Percent of total width (Float)
 * @param erh                    Eclipse radius max height. Used to modify orbite movement. Unit: Percent of total height (Float)
 * @param ang_speed              Angular speed. Use to describe orbite speed movement. Unit: angle by frame (Float)
 * @param planet_radius          Planet radius. Unit: Percent of total width (Float)
 * @param sattelite_radius       Sattelite radius. Unit: Percent of total width (Float)
 * @param faraway_planet_radius  Far away planet radius. Unit: Percent of total width (Float)
 * @param nbr_stars              Number of star in background. Unit: stars (Int)
 * @param planet_color           Color of the planet RGB (Tuple3)
 * @param sattelite_color        Color of the sattelite RGB (Tuple3)
 * @param faraway_planet_color   Color of the far away planet RGB (Tuple3)
 * @param display_fps            Display fps. (Boolean) default = false
 * @param blur_level             Level of blur. (Int) default = 5
 * */
object orbite_params extends engine_param {

  var erw: Float = 25
  var erh: Float = 5

  var ang_speed: Float = 3e-3

  var planet_radius: Float = 10
  var sattelite_radius: Float = 2
  var faraway_planet_radius: Float = 4

  var nbr_stars: Int = 40

  var planet_color: Color = (170, 170, 170)
  var sattelite_color: Color = (20,40,180)
  var faraway_planet_color: Color = (252, 100, 73)

  var blur_level: Int = 5

  def setup_pattern(orbite_pattern: OrbitePattern) = {
    bg_color = orbite_pattern.background_color
    blur_level = orbite_pattern.blur
    erw = orbite_pattern.relative_orbite_width
    erh = orbite_pattern.relative_orbite_height
    ang_speed = orbite_pattern.angular_speed
    planet_radius = orbite_pattern.relative_planet_radius
    sattelite_radius = orbite_pattern.relative_sattelite_radius
    faraway_planet_radius = orbite_pattern.relative_faraway_planet_radius
    nbr_stars = orbite_pattern.nbr_stars
    planet_color = orbite_pattern.planet_color
    sattelite_color = orbite_pattern.sattelite_color
    faraway_planet_color = orbite_pattern.faraway_planet_color
  }
}

object Orbite {
  def main(args: Array[String]): Unit = {
    orbite_params.width = 1240
    orbite_params.height = 720
    orbite_params.full_screen = true
    orbite_params.bg_color = (30, 30, 30)

    orbite_params.erw = 15
    orbite_params.erh = 5
    orbite_params.ang_speed = 1e-2
    orbite_params.planet_radius = 10
    orbite_params.sattelite_radius = 2
    orbite_params.nbr_stars = 40
    orbite_params.planet_color = (0, 175, 100)
    orbite_params.sattelite_color = (255, 0, 0)
    orbite_params.display_fps = true
    orbite_params.blur_level = 5

    PApplet.main(classOf[Orbite].getName)
  }
}
