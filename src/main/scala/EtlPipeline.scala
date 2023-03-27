import destination.Destination
import origin.Origin

trait EtlPipeline[A] {
  val origin: Origin[A]
  val destination: Destination[A]

  def run(config: Config): Unit = {
    val input = origin.extract(config.filePath+"input/"+config.fileName)
    val cleaned = origin.clean(input)
    val transformed = destination.transform(cleaned)
    destination.save(config.filePath+"output/"+config.fileName, transformed)
  }
}