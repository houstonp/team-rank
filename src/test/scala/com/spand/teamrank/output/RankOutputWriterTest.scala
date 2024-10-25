package com.spand.teamrank.output

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.io.Source
import scala.util.{Success, Failure}
import java.io.{File, PrintWriter, IOException}

class RankOutputWriterTest extends AnyFunSuite with Matchers {

  val outputFilePath = "test_output.txt"

  def readFileContent(filePath: String): String = {
    Source.fromFile(filePath).getLines().mkString("\n")
  }

  def deleteTestFile(): Unit = {
    val file = new File(outputFilePath)
    if (file.exists()) file.delete()
  }

  test("RankOutputWriter should successfully write rankings to a file") {
    val rankings = List(
      ("Team A", 6, 1),
      ("Team B", 3, 2),
      ("Team C", 1, 3)
    )

    val writer = new RankOutputWriter
    writer.writeRankingToFile(rankings, outputFilePath) match {
      case Success(_) =>
        val fileContent = readFileContent(outputFilePath)
        fileContent shouldEqual
          """1. Team A, 6 pts
            |2. Team B, 3 pts
            |3. Team C, 1 pts""".stripMargin

      case Failure(_) => fail("Expected Success but got Failure")
    }

    deleteTestFile()
  }

  test("RankOutputWriter should write nothing for an empty rankings list") {
    val rankings = List.empty[(String, Int, Int)]

    val writer = new RankOutputWriter
    writer.writeRankingToFile(rankings, outputFilePath) match {
      case Success(_) =>
        val fileContent = readFileContent(outputFilePath)
        fileContent shouldBe empty
      case Failure(_) => fail("Expected Success but got Failure")
    }

    deleteTestFile()
  }

  test("RankOutputWriter should fail when writing to an invalid file path") {
    val rankings = List(
      ("Team A", 6, 1),
      ("Team B", 3, 2)
    )

    val invalidFilePath = "/invalid/path/output.txt"
    val writer = new RankOutputWriter
    writer.writeRankingToFile(rankings, invalidFilePath) match {
      case Success(_) => fail("Expected Failure but got Success")
      case Failure(exception) =>
        exception shouldBe a [IOException]
    }
  }

  test("RankOutputWriter should correctly write a single ranking entry") {
    val rankings = List(
      ("Team A", 9, 1)
    )

    val writer = new RankOutputWriter
    writer.writeRankingToFile(rankings, outputFilePath) match {
      case Success(_) =>
        val fileContent = readFileContent(outputFilePath)
        fileContent shouldEqual "1. Team A, 9 pts"

      case Failure(_) => fail("Expected Success but got Failure")
    }

    deleteTestFile()
  }

  test("RankOutputWriter should correctly handle a large number of rankings") {
    val rankings = (1 to 100).map(i => (s"Team $i", i, i)).toList

    val writer = new RankOutputWriter
    writer.writeRankingToFile(rankings, outputFilePath) match {
      case Success(_) =>
        val fileContent = readFileContent(outputFilePath)
        fileContent.split("\n").length shouldEqual 100
        fileContent should include("100. Team 100, 100 pts")

      case Failure(_) => fail("Expected Success but got Failure")
    }

    deleteTestFile()
  }
}
