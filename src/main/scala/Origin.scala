trait Origin[T] {
  def extract(path: String): T
  def clean(input: T): T
}