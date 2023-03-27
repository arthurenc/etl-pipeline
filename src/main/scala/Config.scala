import Config.DataType
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._

case class Config(
  fileName: String,
  filePath: String,
  dataType: DataType
)

object Config {
  sealed trait DataType
  case object StringData extends DataType
  case object IntListData extends DataType
  case object JsonData extends DataType

  def configLoader(): Either[ConfigReaderFailures, Config] = ConfigSource.default.load[Config] match {
    case Left(e) => Left(e)
    case Right(config) => Right(config)
  }
}
