import scala.io.Source

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