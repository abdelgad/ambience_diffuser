package station.displayEngine

abstract class Pattern(background_color: Color, blur: Int)

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
) extends Pattern(background_color, blur)

