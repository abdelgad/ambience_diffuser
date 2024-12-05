import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

import station.displayEngine.{OrbitePattern, Color, display_video}

class VideoGeneratorTests extends AnyFlatSpec {

  val width = 1280
  val height = 720

  val primary_color: Color = (252, 131, 73)
  val secondary_color: Color = (252, 189, 73)
  val third_color: Color = (252, 100, 73)

  val space_black: Color = (30, 30, 30)

  "Generate orbite video" should "not failed" in {
    
    val pattern = new OrbitePattern(
      background_color = space_black, blur = 5,
      relative_orbite_width = 15, relative_orbite_height = 5,
      angular_speed = 1e-2,
      relative_planet_radius = 10, relative_sattelite_radius = 2, relative_faraway_planet_radius = 6,
      nbr_stars = 40,
      planet_color = primary_color, sattelite_color = secondary_color, faraway_planet_color = third_color
    )

    noException should be thrownBy {
      display_video.display_pattern(pattern)
      Thread.sleep(3000) 
    }
  }

}
