import pureconfig._
import pureconfig.generic.auto._

object Runner extends App {
  lazy val conf = ConfigSource.default.load[ServiceConf]
  val fileName = conf.toOption.get.fileName
  val filePath = conf.toOption.get.filePath
  val dataType = conf.toOption.get.dataType

  val path = s"$filePath/$fileName"

  dataType match {
    case "String" =>
      val (origin, destination) = (StringOrigin, StringDestination)
      val input = origin.extract(path)
      val cleaned =  origin.clean(input)
      val transformed = destination.transform(cleaned)
      destination.save(fileName, transformed)
    case "IntList" =>
      val (origin, destination) = (IntListOrigin, IntListDestination)
      val input = origin.extract(path)
      val cleaned =  origin.clean(input)
      val transformed = destination.transform(cleaned)
      destination.save(fileName, transformed)
  }
}

case class ServiceConf(fileName: String, filePath: String, dataType: String)
