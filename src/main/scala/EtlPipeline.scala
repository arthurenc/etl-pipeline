import destination.Destination
import origin.Origin

trait EtlPipeline[T] {
  val origin: Origin[T]
  val destination: Destination[T]

  def run(config: EtlConfig): Unit = {
    val input = origin.extract(config.inputPath)
    val cleaned = origin.clean(input)
    val transformed = destination.transform(cleaned)
    destination.save(config.outputPath, transformed)
  }
}