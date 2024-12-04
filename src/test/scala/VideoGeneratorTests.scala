package DisplayEngine

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*
import processing.core.PApplet

class VideoGeneratorTests extends AnyFlatSpec {

  val width = 1280
  val height = 720

  val primary_color: Tuple3[Int, Int, Int] = (252, 131, 73)
  val secondary_color: Tuple3[Int, Int, Int] = (252, 189, 73)
  val third_color: Tuple3[Int, Int, Int] = (252, 100, 73)

  val space_black: Tuple3[Int, Int, Int] = (30, 30, 30)

  "Generate Video" should "not failed" in {
    val engine: Engine = new Orbite(width, height, space_black, 25, 5, 5e-3, 5, 1, 40, primary_color)
    PApplet.main(engine.getClass.getName)
  }

}
