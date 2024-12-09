package station.displayEngine

import processing.core.{PApplet, PConstants, PGraphics}

class Fire extends Engine {
  this.op = fire_params
  val fop = fire_params

  var flames: List[Flame] = List()
  var smallFlames: List[Flame] = List()
  var sparks: List[Spark] = List()

  def set_relative_val = {}

  // ========== 
  // | Engine |
  // ==========

  override def settings_pattern(): Unit = {
  }

  override def setup_pattern(): Unit = {
    set_relative_val
    generate_flames()
    generate_sparks()
  }

  override def draw_pattern(): Unit = {
    draw_flames()
    draw_small_flames()
    draw_sparks()
  }

  override def ajustement_pattern(): Unit = {
    update_flames()
    update_small_flames()
    update_sparks()
  }

  // ========== 
  // | Flames |
  // ==========

  case class Flame(var x: Float, var y: Float, var size: Float, var speed: Float)

  def generate_flames(): Unit = {
    flames = (0 until fop.flame_intensity).map { _ =>
      Flame(random(width), height, random(50, 150), random(fop.flame_speed))
    }.toList

    smallFlames = (0 until fop.flame_intensity).map { _ =>
      Flame(random(width), height, random(20, 70), random(fop.flame_speed))
    }.toList
  }

  def draw_flames(): Unit = {
    fill(fop.flame_color._1, fop.flame_color._2, fop.flame_color._3, 150)
    noStroke()
    flames.foreach { flame =>
      ellipse(flame.x, flame.y, flame.size, flame.size)
    }
  }

  def draw_small_flames(): Unit = {
    fill(fop.flame_color._1, fop.flame_color._2 - 50, fop.flame_color._3, 100)
    noStroke()
    smallFlames.foreach { flame =>
      ellipse(flame.x, flame.y, flame.size, flame.size)
    }
  }

  def update_flames(): Unit = {
    flames = flames.map { flame =>
      flame.y -= flame.speed
      flame.size -= fop.flame_shrink_speed
      if (flame.size <= 0) {
        flame.y = height
        flame.size = random(50, 150)
      }
      flame
    }
  }

  def update_small_flames(): Unit = {
    smallFlames = smallFlames.map { flame =>
      flame.y -= flame.speed
      flame.size -= fop.flame_shrink_speed
      if (flame.size <= 0) {
        flame.y = height
        flame.size = random(20, 70)
      }
      flame
    }
  }

  // ========== 
  // | Sparks |
  // ==========

  case class Spark(var x: Float, var y: Float, var size: Float, var speed: Float)

  def generate_sparks(): Unit = {
    sparks = (0 until fop.spark_intensity).map { _ =>
      Spark(random(width), height, random(5, 15), random(fop.spark_speed))
    }.toList
  }

  def draw_sparks(): Unit = {
    fill(fop.spark_color._1, fop.spark_color._2, fop.spark_color._3, 200)
    noStroke()
    sparks.foreach { spark =>
      ellipse(spark.x, spark.y, spark.size, spark.size)
    }
  }

  def update_sparks(): Unit = {
    sparks = sparks.map { spark =>
      spark.y -= spark.speed
      spark.size -= fop.spark_shrink_speed
      if (spark.size <= 0) {
        spark.y = height - random(100)
        spark.size = random(5, 15)
      }
      spark
    }
  }
}

object fire_params extends engine_param {
  var flame_color: (Int, Int, Int) = (255, 100, 0)
  var spark_color: (Int, Int, Int) = (255, 255, 0)
  var flame_speed: Float = 2f
  var spark_speed: Float = 5f
  var flame_intensity: Int = 100
  var spark_intensity: Int = 50
  var flame_shrink_speed: Float = 0.5f
  var spark_shrink_speed: Float = 0.1f

  def setup_pattern(fire_pattern: FirePattern) = {
    bg_color = fire_pattern.background_color
    flame_color = fire_pattern.flame_color
    spark_color = fire_pattern.spark_color
    flame_speed = fire_pattern.flame_speed
    spark_speed = fire_pattern.spark_speed
    flame_intensity = fire_pattern.flame_intensity
    spark_intensity = fire_pattern.spark_intensity
    flame_shrink_speed = fire_pattern.flame_shrink_speed
    spark_shrink_speed = fire_pattern.spark_shrink_speed
  }
}

object Fire {
  def main(args: Array[String]): Unit = {
    fire_params.full_screen = true
    fire_params.bg_color = (0, 0, 0)
    fire_params.flame_color = (255, 100, 0)
    fire_params.spark_color = (255, 255, 0)
    fire_params.flame_speed = 2f
    fire_params.spark_speed = 5f
    fire_params.flame_intensity = 100
    fire_params.spark_intensity = 50

    PApplet.main(classOf[Fire].getName)
  }
}