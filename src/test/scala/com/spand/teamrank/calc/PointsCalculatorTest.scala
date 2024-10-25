package com.spand.teamrank.calc

import com.spand.teamrank.entities.Game
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PointsCalculatorTest extends AnyFunSuite with Matchers {

  val pointsCalculator = new PointsCalculator

  test("PointsCalculator should correctly calculate points for valid games") {
    val games = List(
      Game("Team A", 3, "Team B", 1),  // Team A wins
      Game("Team C", 2, "Team D", 2),  // Draw
      Game("Team A", 0, "Team C", 1)   // Team C wins
    )

    pointsCalculator.calculatePoints(games) match {
      case Right(points) =>
        points("Team A") shouldEqual 3  // 1 win, 3 pts
        points("Team B") shouldEqual 0  // 1 loss, 0 pts
        points("Team C") shouldEqual 4  // 1 win, 1 draw, 4pts
        points("Team D") shouldEqual 1  // 1 draw, 1 pt
      case Left(_) => fail("Expected success but got failure")
    }
  }

  test("PointsCalculator should return an empty map for an empty list of games") {
    val games = List.empty[Game]

    pointsCalculator.calculatePoints(games) match {
      case Right(_) => fail("Expected failure but got success")
      case Left(errorMessage) => errorMessage shouldEqual "No games provided to calculate points."
    }
  }

  test("PointsCalculator should return failure for negative scores") {
    val games = List(Game("Team A", 3, "Team B", -1))

    pointsCalculator.calculatePoints(games) match {
      case Right(_) => fail("Expected failure but got success")
      case Left(errorMessage) => errorMessage should include("Invalid game data found")
    }
  }

  test("PointsCalculator should correctly handle draws") {
    val games = List(
      Game("Team A", 1, "Team B", 1),
      Game("Team C", 2, "Team D", 2)
    )

    pointsCalculator.calculatePoints(games) match {
      case Right(points) =>
        points("Team A") shouldEqual 1
        points("Team B") shouldEqual 1
        points("Team C") shouldEqual 1
        points("Team D") shouldEqual 1
      case Left(_) => fail("Expected success but got failure")
    }
  }

  test("PointsCalculator should fail and stop processing when encountering invalid game data") {
    val games = List(
      Game("Team A", 3, "Team B", 2),  // Valid game
      Game("Team C", 1, "Team D", -1), // Invalid game (negative score)
      Game("Team E", 0, "Team F", 1)   // This should not be processed
    )

    pointsCalculator.calculatePoints(games) match {
      case Right(_) => fail("Expected failure but got success")
      case Left(errorMessage) =>
        errorMessage should include("Invalid game data found")
    }
  }
}
