package destination

import io.circe.syntax._
import io.circe.Json
import io.circe.parser.decode
import PetOwner._
import io.circe.generic.extras.auto._

import java.io.PrintWriter

trait Destination[A] {
  def transform(input: A): A
  def save(filePath: String, data: A): Unit =
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
    input.as[List[PetOwner]].fold(
      decodingFailure => throw decodingFailure,
      petOwners => petOwners.map { case petOwner@PetOwner(_, _, _, pet) =>
        petOwner.copy(pet = pet.copy(pet_type = pet.pet_type.replace(" ", "-")))
      }.asJson
    )
  }
}