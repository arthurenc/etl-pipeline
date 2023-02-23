package destination

import io.circe.syntax._
import io.circe.Json
import PetOwner.config

import io.circe.generic.extras.auto._
import java.io.PrintWriter

trait Destination[T] {
  def transform(input: T): T
  def save(filePath: String, data: T): Unit =
    new PrintWriter(filePath) { write(data.toString); close }
}

case object StringDestination extends Destination[String] {
  override def transform(input: String): String = input.toLowerCase
}

case object IntListDestination extends Destination[List[Int]] {
  override def transform(input: List[Int]): List[Int] = input.reverse
}

case object JsonDestination extends Destination[Json] {
  override def transform(input: Json): Json = {
    val decodedJson = input.as[PetOwner].map(petOwner =>
      petOwner.copy(pet = petOwner.pet.copy(pet_type = petOwner.pet.pet_type.replace(" ", "-")))
    )
    decodedJson match {
      case Right(decodedJson) => decodedJson.asJson
      case Left(decodingFailure) => throw decodingFailure
    }
  }
}