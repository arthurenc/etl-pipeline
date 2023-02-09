trait Destination[T] {
  def save(filename: String, data: T): Unit
  def transform(input: T): T
}
