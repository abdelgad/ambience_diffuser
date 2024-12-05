package station.displayEngine

import processing.core.PApplet

class DisplayVideo  {
  def display_pattern(pattern: Pattern): Option[Unit] = {
    pattern match
      case orbite: OrbitePattern => {
        orbite_params.setup_pattern(orbite)
        Some(run_engine(classOf[Orbite].getName))
      }
      case _ => None
  }
  
  def run_engine(engine: String) = PApplet.main(engine)
}
