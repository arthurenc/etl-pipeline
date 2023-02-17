import scala.io.Source
import io.circe.{HCursor, Json, parser}

trait Origin[T] {
  def extract(path: String): T
  def clean(input: T): T
}

case object StringOrigin extends Origin[String] {
  def extract(path: String): String = Source.fromFile(path).mkString
  def clean(input: String): String = input.replace(";", "")
}

case object IntListOrigin extends Origin[List[Int]] {
  def extract(path: String): List[Int] =
    Source.fromFile(path).getLines.foldRight(List[Int]())((currentLine, list) => currentLine.toInt :: list)
  def clean(input: List[Int]): List[Int] = input.filter(_%2 == 0)
}

case object JsonOrigin extends Origin[Json] {
  def extract(path: String): Json = parser.parse(Source.fromFile(path).mkString).getOrElse(Json.Null)
  def clean(input: Json): Json = {
    val cursor: HCursor = input.hcursor
    cursor.downField("data").focus.get
  }
}