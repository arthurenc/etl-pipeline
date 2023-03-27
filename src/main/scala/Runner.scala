import Config.{IntListData, JsonData, StringData}
import destination.{IntListDestination, JsonDestination, StringDestination}
import io.circe.Json
import origin.{IntListOrigin, JsonOrigin, StringOrigin}

object Runner extends App {
  Config.configLoader() match {
    case Right(config) =>
      val runner = config.dataType match {
        case StringData => new EtlPipeline[String] {
          val origin = StringOrigin
          val destination = StringDestination
        }
        case IntListData => new EtlPipeline[List[Int]] {
          val origin = IntListOrigin
          val destination = IntListDestination
        }
        case JsonData => new EtlPipeline[Json] {
          val origin = JsonOrigin
          val destination = JsonDestination
        }
      }
      runner.run(config)

    case Left(err) => println(err)
  }
}
