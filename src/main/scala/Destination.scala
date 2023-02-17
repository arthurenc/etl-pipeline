import java.io.PrintWriter
import io.circe.{Decoder, Encoder, Json}
import io.circe.syntax._

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

case object JsonDestination extends Destination[Json] {
  def save(filename: String, data: Json): Unit =
    new PrintWriter(s"./src/main/resources/output/$filename") { write(data.toString); close }

  def transform(input: Json): Json = {
    implicit val decodeOwner: Decoder[Owner] = Decoder.forProduct2("first_name", "last_name")(Owner.apply)
    implicit val decodePet: Decoder[Pet] = Decoder.forProduct2("name", "pet_type")(Pet.apply)
    implicit val decodePetOwner: Decoder[PetOwner] = Decoder.forProduct4("id", "owner", "email", "pet")(PetOwner.apply)
    implicit val encodeOwner: Encoder[Owner] = Encoder.forProduct2("first_name", "last_name")(owner => (owner.first_name, owner.last_name))
    implicit val encodePet: Encoder[Pet] = Encoder.forProduct2("name", "pet_type")(pet => (pet.name, pet.pet_type))
    implicit val encodePetOwner: Encoder[PetOwner] = Encoder.forProduct4("id", "owner", "email", "pet")(petOwner => (petOwner.id, petOwner.owner, petOwner.email, petOwner.pet))

    val decodedJson = input.as[List[PetOwner]].toOption.get
    val transformedJson: List[PetOwner] = decodedJson.map(
      petOwner => petOwner.copy(
        pet = petOwner.pet.copy(pet_type = petOwner.pet.pet_type.replace(" ", "-"))
      )
    )
    transformedJson.asJson
  }
}