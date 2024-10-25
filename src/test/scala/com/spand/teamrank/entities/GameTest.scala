package com.spand.teamrank.entities

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GameTest extends AnyFunSuite with Matchers {

  test("Game should successfully input") {
    val validInput = "Team A 3, Team B 2"

    Game.extract(validInput) match {
      case Right(game) =>
        game.team1Name shouldEqual "Team A"
        game.team1Score shouldEqual 3
        game.team2Name shouldEqual "Team B"
        game.team2Score shouldEqual 2
      case Left(_) => fail("Expected success but got failure")
    }
  }

  test("Game should fail if there are missing scores") {
    val invalidInput = "Team A, Team B"

    Game.extract(invalidInput) match {
      case Right(_) => fail("Expected failure but got success")
      case Left(errorMessage) =>
        errorMessage should include("Invalid score format for team")
    }
  }

  test("Game should fail with non-numeric scores") {
    val invalidInput = "Team A X, Team B 2"

    Game.extract(invalidInput) match {
      case Right(_) => fail("Expected failure but got success")
      case Left(errorMessage) =>
        errorMessage should include("Invalid score format")
    }
  }

  test("Game should fail if team names are missing") {
    val invalidInput = " 3, Team B 2"

    Game.extract(invalidInput) match {
      case Right(_) => fail("Expected failure but got success")
      case Left(errorMessage) =>
        errorMessage should include("Invalid team info format")
    }
  }

  test("Game should fail if input doesn't have a comma delimiter") {
    val invalidInput = "Team A 3 Team B 2"

    Game.extract(invalidInput) match {
      case Right(_) => fail("Expected failure but got success")
      case Left(errorMessage) =>
        errorMessage should include("Input string does not contain exactly two teams")
    }
  }

  test("Game should be successful if there are extra spaces") {
    val validInput = "  Team A   3 ,   Team B   2  "

    Game.extract(validInput) match {
      case Right(game) =>
        game.team1Name shouldEqual "Team A"
        game.team1Score shouldEqual 3
        game.team2Name shouldEqual "Team B"
        game.team2Score shouldEqual 2
      case Left(_) => fail("Expected success but got failure")
    }
  }

  test("Game should fail if there are negative scores") {
    val invalidInput = "Team A -1, Team B 2"

    Game.extract(invalidInput) match {
      case Right(_) => fail("Expected failure but got success")
      case Left(errorMessage) =>
        errorMessage should include("Score cannot be negative")
    }
  }
}
