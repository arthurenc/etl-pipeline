sealed trait DataType {
  def name: String
}
object DataType {
  case object String extends DataType {
    override def name: String = "String"
  }
  case object IntList extends DataType {
    override def name: String = "IntList"
  }
}
