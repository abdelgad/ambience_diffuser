package ImageGenerator

import java.awt.Color

abstract class Pattern(frame: Int, primary: Color, secondary: Color, third: Color)

case class Orbite(
  frame: Int,
  primary: Color,
  secondary: Color,
  third: Color,
  orbite_speed: Int,
  star_prob: Float,
  planet_size: Int
) extends Pattern(frame: Int, primary: Color, secondary: Color, third: Color)
