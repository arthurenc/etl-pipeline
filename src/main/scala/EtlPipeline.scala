import destination.Destination
import origin.Origin

trait EtlPipeline[T] {
  val origin: Origin[T]
  val destination: Destination[T]

  def run(config: ServiceConfig): Unit = {
    val input = origin.extract(config.path)
    val cleaned = origin.clean(input)
    val transformed = destination.transform(cleaned)
    destination.save(config.path, transformed)
  }
}