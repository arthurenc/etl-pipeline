import java.io.PrintWriter

trait Destination[T] {
  def save(filename: String, data: T): Unit
  def transform(input: T): T
}

case object StringDestination extends Destination[String] {
  def save(filename: String, data: String): Unit =
    new PrintWriter(s"./src/main/resources/output/$filename") { write(data); close }
  def transform(input: String): String = input.toLowerCase
}

case object IntListDestination extends Destination[List[Int]] {
  def save(filename: String, data: List[Int]): Unit =
    new PrintWriter(s"./src/main/resources/output/$filename") { write(data.mkString); close }
  def transform(input: List[Int]): List[Int] = input.reverse
}