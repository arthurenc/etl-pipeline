package destination

import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}

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
    implicit val decodeOwner: Decoder[Owner] = Decoder.forProduct2("first_name", "last_name")(Owner.apply)
    implicit val encodeOwner: Encoder[Owner] = Encoder.forProduct2("first_name", "last_name")(owner => (owner.first_name, owner.last_name))
    implicit val decodePet: Decoder[Pet] = Decoder.forProduct2("name", "pet_type")(Pet.apply)
    implicit val encodePet: Encoder[Pet] = Encoder.forProduct2("name", "pet_type")(pet => (pet.name, pet.pet_type))
    implicit val decodePetOwner: Decoder[PetOwner] = Decoder.forProduct4("id", "owner", "email", "pet")(PetOwner.apply)
    implicit val encodePetOwner: Encoder[PetOwner] = Encoder.forProduct4("id", "owner", "email", "pet")(petOwner => (petOwner.id, petOwner.owner, petOwner.email, petOwner.pet))

    val decodedJson = input.as[PetOwner].map(
      petOwner => petOwner.copy(
        pet = petOwner.pet.copy(pet_type = petOwner.pet.pet_type.replace(" ", "-"))
      )
    )
    decodedJson match {
      case Right(decodedJson) => decodedJson.asJson
      case Left(decodingFailure) => throw decodingFailure
    }
  }
}