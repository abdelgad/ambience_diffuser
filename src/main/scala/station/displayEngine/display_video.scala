package station.displayEngine

import processing.core.PApplet

object display_video  {
  def display_pattern(pattern: Pattern): Option[Unit] = {
    pattern match
      case orbite: OrbitePattern => {
        orbite_params.setup_pattern(orbite)
        Some(run_engine(classOf[Orbite].getName))
      }
      case rain: RainPattern => {
        rain_params.setup_pattern(rain)
        Some(run_engine(classOf[Rain].getName))
      }
      case _ => None
  }
  
  private def run_engine(engine: String): Unit = PApplet.main(engine)
}
