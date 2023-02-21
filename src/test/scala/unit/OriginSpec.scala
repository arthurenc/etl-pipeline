package unit

import org.scalatest.flatspec.AnyFlatSpec
import origin.{IntListOrigin, JsonOrigin, StringOrigin}
import org.scalatest.BeforeAndAfter
import io.circe.{Json, ParsingFailure, parser}

import scala.reflect.io.Directory
import java.nio.file.{Files, Paths}
import java.io.{FileNotFoundException, PrintWriter}

class OriginSpec extends AnyFlatSpec with BeforeAndAfter {

  "Origin.openFile" should "throw a FileNotFoundException if the file does not exist" in new OriginFixture {
    assertThrows[FileNotFoundException]{
      StringOrigin.openFile(path+invalidFile)
    }
  }

  "StringOrigin.extract" should "return a file as a string" in new OriginFixture {
    val file = StringOrigin.extract(path+stringFile)
    assert(file == string)
  }

  "StringOrigin.clean" should "remove semicolons from a string" in new OriginFixture {
    val file = StringOrigin.extract(path+stringFile)
    val cleanedFile = StringOrigin.clean(file)
    assert(cleanedFile == cleanedString)
  }

  "IntListOrigin.extract" should "return a file as a list of integers" in new OriginFixture {
    val file = IntListOrigin.extract(path+intListFile)
    assert(file == intList)
  }

  "IntListOrigin.clean" should "remove semicolons from a list of integers" in new OriginFixture {
    val file = IntListOrigin.extract(path+intListFile)
    val cleanedFile = IntListOrigin.clean(file)
    assert(cleanedFile == cleanedIntList)
  }

  "JsonOrigin.extract" should "return a file as a Json" in new OriginFixture {
    val file = JsonOrigin.extract(path+jsonFile)
    val decerializedJson: Json = parser.parse(json).getOrElse(Json.Null)
    assert(file == decerializedJson)
  }

  it should "throw a ParsingFailure if the file cannot be converted to Json" in new OriginFixture {
    assertThrows[ParsingFailure]{
      JsonOrigin.extract(path+invalidJsonFile)
    }
  }

  "JsonOrigin.clean" should "remove the data wrapper from a Json" in new OriginFixture {
    val file = JsonOrigin.extract(path+jsonFile)
    val cleanedFile = JsonOrigin.clean(file)
    assert(cleanedFile == cleanedJson)
  }

  before {
    new OriginFixture {
      val dirPath = Paths.get(path)
      if(!(Files.exists(dirPath) && Files.isDirectory(dirPath))) Files.createDirectory(dirPath)
      new PrintWriter(path+stringFile) { write(string); close }
      new PrintWriter(path+intListFile) { write(intList.mkString("\n")); close }
      new PrintWriter(path+jsonFile) { write(json); close }
      new PrintWriter(path+invalidJsonFile) { write(string); close }
    }
  }

  after {
    new OriginFixture {
      val directory = Directory(path+"/")
      directory.deleteRecursively()
    }
  }

  trait OriginFixture {
    val stringFile = "test1.txt"
    val string: String = "Hello; World!"
    val cleanedString: String = "Hello World!"

    val intListFile = "test2.txt"
    val intList: List[Int] = List(1,2,3,4)
    val cleanedIntList: List[Int] = List(2,4)

    val jsonFile = "test3.txt"
    val invalidJsonFile = "invalid-json.txt"
    val json: String = "{\"data\":{\"first_name\":\"John\",\"last_name\":\"Smith\"}}"
    val cleanedJson: Json = parser.parse("{\"first_name\":\"John\",\"last_name\":\"Smith\"}").getOrElse(Json.Null)

    val invalidFile = "test0.txt"
    val path = "./src/test/resources/temp/"
  }

}
