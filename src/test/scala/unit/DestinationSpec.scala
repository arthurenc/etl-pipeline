package unit

import destination.{IntListDestination, JsonDestination, StringDestination}
import io.circe.{Json, parser}
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec

import java.nio.file.{Files, Path, Paths}
import scala.reflect.io.{Directory, File}

class DestinationSpec extends AnyFlatSpec with BeforeAndAfter {

  "Destination.Save" should "save a file to a directory" in new DestinationFixture {
    val filePath = path+"/"+stringFile
    StringDestination.save(filePath, string)
    assert(File(filePath).exists)
  }

  "StringDestination.Transform" should "convert a string to lowercase" in new DestinationFixture {
    val file = StringDestination.transform(string)
    assert(file == transformedString)
  }

  "IntListDestination.Transform" should "reverse a list of integers" in new DestinationFixture {
    val file = IntListDestination.transform(intList)
    assert(file == transformedIntList)
  }

  "JsonDestination.Transform" should "replace spaces in pet_type with a dash" in new DestinationFixture {
    val file = JsonDestination.transform(json)
    assert(file == transformedJson)
  }

  before {
    new DestinationFixture {
      val dirPath: Path = Paths.get(path)
      if(!(Files.exists(dirPath) && Files.isDirectory(dirPath))) Files.createDirectory(dirPath)
    }
  }

  after {
    new DestinationFixture {
      val directory = Directory(path+"/")
      directory.deleteRecursively()
    }
  }

  trait DestinationFixture {
    val stringFile = "test1.txt"
    val string: String = "Hello World!"
    val transformedString: String = "hello world!"

    val intList: List[Int] = List(1,2,3,4)
    val transformedIntList: List[Int] = List(4,3,2,1)

    val json: Json = parser.parse("{\"id\":1,\"owner\":{\"first_name\":\"John\",\"last_name\":\"Smith\"},\"email\":\"johnsmith@mail.ru\",\"pet\":{\"name\":\"Luna\",\"pet_type\":\"Domestic Cat\"}}").getOrElse(Json.Null)
    val transformedJson: Json = parser.parse("{\"id\":1,\"owner\":{\"first_name\":\"John\",\"last_name\":\"Smith\"},\"email\":\"johnsmith@mail.ru\",\"pet\":{\"name\":\"Luna\",\"pet_type\":\"Domestic-Cat\"}}").getOrElse(Json.Null)

    val path = "./src/test/resources/temp/"
  }
}
