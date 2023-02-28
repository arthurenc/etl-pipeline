package destination

import io.circe.generic.extras.Configuration
import PetOwner.{Pet, Owner}

case class PetOwner(
                     id: Int,
                     owner: Owner,
                     email: String,
                     pet: Pet
                   )

object PetOwner {
  implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  case class Owner(
                    first_name: String,
                    last_name: String
                  )

  case class Pet(
                  name: String,
                  pet_type: String
                )
}

