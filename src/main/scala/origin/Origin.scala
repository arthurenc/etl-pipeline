package origin

import io.circe.{HCursor, Json, ParsingFailure, parser}

import java.io.FileNotFoundException
import scala.io.{BufferedSource, Source}

trait Origin[T] {
  def openFile(path: String): BufferedSource =
    try {
      Source.fromFile(path)
    } catch {
      case e: FileNotFoundException => throw e
    }
  def extract(path: String): T
  def clean(input: T): T
}

case object StringOrigin extends Origin[String] {
  override def extract(path: String): String = openFile(path).mkString
  override def clean(input: String): String = input.replace(";", "")
}

case object IntListOrigin extends Origin[List[Int]] {
  override def extract(path: String): List[Int] =
    openFile(path).getLines.foldRight(List[Int]())((currentLine, list) => currentLine.toInt :: list)
  override def clean(input: List[Int]): List[Int] = input.filter(_%2 == 0)
}

case object JsonOrigin extends Origin[Json] {
  override def extract(path: String): Json = {
    val file: String = openFile(path).mkString
    parser.parse(file) match {
      case Right(json: Json) => json
      case Left(e: ParsingFailure) => throw e
    }
  }
  override def clean(input: Json): Json = {
    val cursor: HCursor = input.hcursor
    cursor.downField("data").focus.get
  }
}