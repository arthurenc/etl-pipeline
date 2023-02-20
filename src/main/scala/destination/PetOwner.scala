package destination

case class PetOwner(
  id: Int,
  owner: Owner,
  email: String,
  pet: Pet
)

case class Owner(
  first_name: String,
  last_name: String
)

case class Pet(
  name: String,
  pet_type: String
)

