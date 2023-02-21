import destination.{Destination, IntListDestination, JsonDestination, StringDestination}
import pureconfig._
import pureconfig.generic.auto._
import io.circe.Json
import origin.{IntListOrigin, JsonOrigin, Origin, StringOrigin}

object Runner extends App {
  lazy val conf = ConfigSource.default.loadOrThrow[ServiceConf]
  val dataType = conf.dataType

  val runner = dataType match {
    case "String" => new Runner[String] {
      val origin = StringOrigin
      val destination = StringDestination
    }
    case "IntList" => new Runner[List[Int]] {
      val origin = IntListOrigin
      val destination = IntListDestination
    }
    case "Json" => new Runner[Json] {
      val origin = JsonOrigin
      val destination = JsonDestination
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
