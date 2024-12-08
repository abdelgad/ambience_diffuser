package station.displayEngine

abstract class Pattern(background_color: Color)

/**
 * Pattern to define orbite animation.
 *
 * @param background_color               Color of the background (Tuple)
 * @param blur                           Level of blur. (Int)
 * @param relative_orbite_width          Eclipse radius max width. Used to modify orbite movement. Unit: Percent of total width (Float)
 * @param relative_orbite_height         Eclipse radius max height. Used to modify orbite movement. Unit: Percent of total height (Float)
 * @param angular_speed                  Angular speed. Use to describe orbite speed movement. Unit: angle by frame (Float)
 * @param relative_planet_radius         Planet radius. Unit: Percent of total width (Float)
 * @param relative_sattelite_radius      Sattelite radius. Unit: Percent of total width (Float)
 * @param relative_faraway_planet_radius Far away planet radius. Unit: Percent of total width (Float)
 * @param nbr_stars                      Number of star in background. Unit: stars (Int)
 * @param planet_color                   Color of the planet RGB (Tuple3)
 * @param sattelite_color                Color of the sattelite RGB (Tuple3)
 * @param faraway_planet_color           Color of the far away planet RGB (Tuple3)
 * */
case class OrbitePattern(
  background_color: Color, blur: Int,
  relative_orbite_width: Float, relative_orbite_height: Float,
  angular_speed: Float,
  relative_planet_radius: Float, relative_sattelite_radius: Float, relative_faraway_planet_radius: Float,
  nbr_stars: Int,
  planet_color: Color, sattelite_color: Color, faraway_planet_color: Color
) extends Pattern(background_color)

/**
 * Pattern to define orbite animation.
 *
 * @param background_color             Color of the background (Tuple3)
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
 * */
case class RainPattern(
  background_color: Color,
  blur_building: Int, blur_cloud: Int,
  relative_building_max_height: Float, building_color: Color, nbr_building: Int,
  relative_cloud_max_width: Float, cloud_color: Color, nbr_cloud: Int,
  base_speed_drop: Int, nbr_drops: Int
) extends Pattern(background_color)

