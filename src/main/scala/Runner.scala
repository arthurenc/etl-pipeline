import java.io.PrintWriter
import scala.io.Source
import pureconfig._
import pureconfig.generic.auto._

object Runner extends App {
  lazy val conf = ConfigSource.default.load[ServiceConf]
  val fileName = conf.toOption.get.fileName
  val filePath = conf.toOption.get.filePath
  val dataType = conf.toOption.get.dataType

  val path = s"$filePath/$fileName"

  val (origin: Origin[_], destination: Destination[_]) = dataType match {
    case "String" => (new stringOrigin, new stringDestination)
    case "IntList" => (new intListOrigin, new intListDestination)
    case _ => (new stringOrigin, new stringDestination)
  }

  val input = origin.extract(path)
  val cleaned =  origin.clean(input)
  val transformed = destination.transform(cleaned)
  destination.save(fileName, transformed)
}

case class stringOrigin() extends Origin[String] {
  def extract(path: String): String = Source.fromFile(path).mkString
  def clean(input: String): String = input.replace(";", "")
}

case class intListOrigin() extends Origin[List[Int]] {
  def extract(path: String): List[Int] =
    Source.fromFile(path).getLines.foldRight(List[Int]())((currentLine, list) => currentLine.toInt :: list)
  def clean(input: List[Int]): List[Int] = input.filter(_%2 == 0)
}

case class stringDestination() extends Destination[String] {
  def save(filename: String, data: String): Unit =
    new PrintWriter(s"/Users/ACL53/Documents/scala-projects/etl-pipeline/src/main/resources/output/$filename") { write(data); close }
  def transform(input: String): String = input.toLowerCase
}

case class intListDestination() extends Destination[List[Int]] {
  def save(filename: String, data: List[Int]): Unit =
    new PrintWriter(s"/Users/ACL53/Documents/scala-projects/etl-pipeline/src/main/resources/output/$filename") { write(data.mkString); close }
  def transform(input: List[Int]): List[Int] = input.reverse
}

case class ServiceConf(fileName: String, filePath: String, dataType: String)
