import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import processing.core.PApplet
import station.displayEngine.{Orbite, orbite_params}

class VideoGeneratorTests extends AnyFlatSpec {

  val width = 1280
  val height = 720

  val primary_color: Tuple3[Int, Int, Int] = (252, 131, 73)
  val secondary_color: Tuple3[Int, Int, Int] = (252, 189, 73)
  val third_color: Tuple3[Int, Int, Int] = (252, 100, 73)

  val space_black: Tuple3[Int, Int, Int] = (30, 30, 30)

  "Generate orbite video" should "not failed" in {
    orbite_params.width = width
    orbite_params.height = height
    orbite_params.bg_color = space_black

    orbite_params.erw = 15
    orbite_params.erh = 5
    orbite_params.ang_speed = 1e-2
    orbite_params.planet_radius = 10
    orbite_params.sattelite_radius = 2
    orbite_params.nbr_stars = 40
    orbite_params.planet_color = primary_color
    orbite_params.sattelite_color = secondary_color
    
    noException should be thrownBy {
      PApplet.main(classOf[Orbite].getName)
      Thread.sleep(3000) 
    }
  }

}
