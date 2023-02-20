package unit

import org.scalatest.flatspec.AnyFlatSpec
import origin.{IntListOrigin, JsonOrigin, StringOrigin}
import org.scalatest.BeforeAndAfter
import io.circe.{Json, parser}
import scala.reflect.io.Directory
import java.nio.file.{Files, Paths}
import java.io.{FileNotFoundException, PrintWriter}

class OriginSpec extends AnyFlatSpec with BeforeAndAfter  {

  "StringOrigin.extract" should "return a file from a dir as a String" in new OriginFixture {
    val file = StringOrigin.extract(path+stringFileName)
    assert(file == stringFile)
  }

  it should "return a FileNotFoundException if the file does not exist in the dir" in new OriginFixture {
    assertThrows[FileNotFoundException]{
      StringOrigin.extract(path+fakeFileName)
    }
  }

  "StringOrigin.clean" should "remove semicolons from the string file" in new OriginFixture {
    val file = StringOrigin.extract(path+stringFileName)
    val cleanedFile = StringOrigin.clean(file)
    assert(cleanedFile == cleanedStringFile)
  }

  "IntListOrigin.extract" should "return a file from a dir as a List of Integers" in new OriginFixture {
    val file = IntListOrigin.extract(path+intListFileName)
    assert(file == intListFile)
  }

  it should "return a FileNotFoundException if the file does not exist in the dir" in new OriginFixture {
    assertThrows[FileNotFoundException]{
      IntListOrigin.extract(path+fakeFileName)
    }
  }

  "IntListOrigin.clean" should "remove semicolons from the string file" in new OriginFixture {
    val file = IntListOrigin.extract(path+intListFileName)
    val cleanedFile = IntListOrigin.clean(file)
    assert(cleanedFile == cleanedIntListFile)
  }

  "JsonOrigin.extract" should "return a file from a dir as a Json" in new OriginFixture {
    val file = JsonOrigin.extract(path+jsonFileName)
    assert(file == jsonFile)
  }

  it should "return a FileNotFoundException if the file does not exist in the dir" in new OriginFixture {
    assertThrows[FileNotFoundException]{
      IntListOrigin.extract(path+fakeFileName)
    }
  }

  it should "return a empty Json if the Json is invalid" in new OriginFixture {
    val file = JsonOrigin.extract(path+invalidJsonFileName)
    assert(file == Json.Null)
  }

  "JsonOrigin.clean" should "remove semicolons from the string file" in new OriginFixture {
    val file = IntListOrigin.extract(path+intListFileName)
    val cleanedFile = IntListOrigin.clean(file)
    assert(cleanedFile == cleanedIntListFile)
  }

  before {
    new OriginFixture {
      val dirPath = Paths.get(path)
      if(!(Files.exists(dirPath) && Files.isDirectory(dirPath))) Files.createDirectory(dirPath)
      new PrintWriter(path+stringFileName) { write(stringFile); close }
      new PrintWriter(path+intListFileName) { write(intListFile.mkString("\n")); close }
      new PrintWriter(path+jsonFileName) { write(jsonFileString); close }
      new PrintWriter(path+invalidJsonFileName) { write(invalidJsonFileString); close }
    }
  }

  after {
    new OriginFixture {
      val directory = Directory(path+"/")
      directory.deleteRecursively()
    }
  }

  trait OriginFixture {
    val stringFileName = "string.txt"
    val stringFile: String = "Hello; world!"
    val cleanedStringFile: String = "Hello world!"
    val intListFileName = "int-list.txt"
    val intListFile: List[Int] = List(1,2,3,4)
    val cleanedIntListFile: List[Int] = List(2,4)
    val jsonFileName = "json.txt"
    val jsonFileString: String = "{ \"data\": [{\"id\":1,\"owner\":{\"first_name\":\"Carmelia\",\"last_name\":\"Tappin\"},\"email\":\"ltappin0@mail.ru\",\"pet\":{\"name\":\"Loreen\",\"pet_type\":\"Four-horned antelope\"}}]}"
    val jsonFile: Json = parser.parse("{ \"data\": [{\"id\":1,\"owner\":{\"first_name\":\"Carmelia\",\"last_name\":\"Tappin\"},\"email\":\"ltappin0@mail.ru\",\"pet\":{\"name\":\"Loreen\",\"pet_type\":\"Four-horned antelope\"}}]}").getOrElse(Json.Null)
    val cleanedJsonFile: Json = parser.parse("[{\"id\":1,\"owner\":{\"first_name\":\"Carmelia\",\"last_name\":\"Tappin\"},\"email\":\"ltappin0@mail.ru\",\"pet\":{\"name\":\"Loreen\",\"pet_type\":\"Four-horned antelope\"}}]").getOrElse(Json.Null)
    val invalidJsonFileName = "invalid-json.txt"
    val invalidJsonFileString: String = "invalid json"
    val fakeFileName = "fakeFile.txt"
    val path = "./src/test/resources/temp/"
  }

}
