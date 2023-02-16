import pureconfig._
import pureconfig.generic.auto._

object Runner extends App {
  lazy val conf = ConfigSource.default.load[ServiceConf]
  val dataType = conf.toOption.get.dataType

  val runner = dataType match {
    case "String" => new Runner[String] {
      val origin = StringOrigin
      val destination = StringDestination
    }
    case "IntList" => new Runner[List[Int]] {
      val origin = IntListOrigin
      val destination = IntListDestination
    }
  }
  runner.run()
}

trait Runner[T] {
  val origin: Origin[T]
  val destination: Destination[T]

  lazy val conf = ConfigSource.default.load[ServiceConf]
  val fileName = conf.toOption.get.fileName
  val filePath = conf.toOption.get.filePath

  val path = s"$filePath/$fileName"

  def run(): Unit = {
    val input = origin.extract(path)
    val cleaned = origin.clean(input)
    val transformed = destination.transform(cleaned)
    destination.save(fileName, transformed)
  }
}

case class ServiceConf(fileName: String, filePath: String, dataType: String)
