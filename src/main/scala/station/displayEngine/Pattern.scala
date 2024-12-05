package station.displayEngine

abstract class Pattern(width: Int, height: Int, background_color: Color, blur: Int)
case class OrbitePattern(
  width: Int, height: Int, background_color: Color, blur: Int,
  relative_orbite_width: Float, relative_orbite_height: Float,
  angular_speed: Float,
  relative_planet_radius: Float, relative_sattelite_radius: Float, relative_faraway_planet_radius: Float,
  nbr_star: Int,
  planet_color: Color, sattelite_color: Color, faraway_planet_color: Color
) extends Pattern(width, height, background_color, blur)

