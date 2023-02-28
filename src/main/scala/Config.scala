import Config.DataType
import pureconfig.ConfigSource
import pureconfig.generic.auto._

case class EtlConfig(
  inputPath: String,
  outputPath: String,
  dataType: DataType
)

object Config {
  def load():EtlConfig = ConfigSource.default.loadOrThrow[EtlConfig]

  sealed trait DataType
  case object StringType extends DataType
  case object IntListType extends DataType
  case object JsonType extends DataType
}
