import Config.DataType
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._

case class ServiceConfig(
  fileName: String,
  path: String,
  dataType: DataType
)

object Config {
  sealed trait DataType
  case object StringType extends DataType
  case object IntListType extends DataType
  case object JsonType extends DataType

  def configLoader(): Either[ConfigReaderFailures, ServiceConfig] = ConfigSource.default.load[ServiceConfig]match {
    case Left(err) => Left(err)
    case Right(config) => Right(config)
  }
}
