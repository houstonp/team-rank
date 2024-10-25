package com.spand.teamrank.input

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.util.{Success, Failure}
import java.io.{File, PrintWriter}

class GamesInputParserTest extends AnyFunSuite with Matchers {

  val testFilePath = "test_input.txt"

  def createTestFile(content: String): Unit = {
    val pw = new PrintWriter(new File(testFilePath))
    pw.write(content)
    pw.close()
  }

  def deleteTestFile(): Unit = {
    val file = new File(testFilePath)
    if (file.exists()) file.delete()
  }

  test("GamesInputParser should successfully parse input") {
    val input =
      """Team A 3, Team B 2
        |Team C 1, Team D 1
        |Team E 0, Team F 3""".stripMargin
    createTestFile(input)

    val parser = new GamesInputParser
    parser.readInput(testFilePath) match {
      case Success(games) =>
        games.length shouldEqual 3
        games(0).team1Name shouldEqual "Team A"
        games(0).team1Score shouldEqual 3
        games(0).team2Name shouldEqual "Team B"
        games(0).team2Score shouldEqual 2

      case Failure(_) => fail("Expected Success but got Failure")
    }

    deleteTestFile()
  }

  test("GamesInputParser should fail if scores are missing") {
    val input = """Team A 3, Team B"""
    createTestFile(input)

    val parser = new GamesInputParser
    parser.readInput(testFilePath) match {
      case Success(_) => fail("Expected Failure but got Success")
      case Failure(exception) =>
        exception.getMessage should include("Invalid score format for team")
    }

    deleteTestFile()
  }

  test("GamesInputParser should fail if the input file does not exist") {
    val parser = new GamesInputParser
    parser.readInput("non_existent_file.txt") match {
      case Success(_) => fail("Expected Failure but got Success")
      case Failure(exception) =>
        exception.getMessage should include("No such file or directory")
    }
  }

  test("GamesInputParser should return an empty list for empty input file") {
    createTestFile("")

    val parser = new GamesInputParser
    parser.readInput(testFilePath) match {
      case Success(games) => games shouldBe empty
      case Failure(_) => fail("Expected Success but got Failure")
    }

    deleteTestFile()
  }
}
