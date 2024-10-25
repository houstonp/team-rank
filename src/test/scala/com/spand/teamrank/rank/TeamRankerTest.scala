package com.spand.teamrank.rank

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TeamRankerTest extends AnyFunSuite with Matchers {

  val teamRanker = new TeamRanker

  test("TeamRanker should correctly rank teams based on points") {
    val points = Map(
      "Team A" -> 6,
      "Team B" -> 3,
      "Team C" -> 4,
      "Team D" -> 1
    )

    teamRanker.rankTeams(points) match {
      case Right(rankedTeams) =>
        rankedTeams.length shouldEqual 4
        rankedTeams.head shouldEqual ("Team A", 6, 1)  // Team A is ranked 1st
        rankedTeams(1) shouldEqual ("Team C", 4, 2)   // Team C is ranked 2nd
        rankedTeams(2) shouldEqual ("Team B", 3, 3)   // Team B is ranked 3rd
        rankedTeams(3) shouldEqual ("Team D", 1, 4)   // Team D is ranked 4th
      case Left(_) => fail("Expected success but got failure")
    }
  }

  test("TeamRanker should fail when the points map is empty") {
    val points = Map.empty[String, Int]

    teamRanker.rankTeams(points) match {
      case Right(_) => fail("Expected failure but got success")
      case Left(errorMessage) =>
        errorMessage should include("Cannot rank teams")
    }
  }

  test("TeamRanker should rank teams with tied points in alphabetical order") {
    val points = Map(
      "Team A" -> 3,
      "Team B" -> 3,
      "Team C" -> 6
    )

    teamRanker.rankTeams(points) match {
      case Right(rankedTeams) =>
        rankedTeams.length shouldEqual 3
        rankedTeams.head shouldEqual ("Team C", 6, 1)   // Team C is ranked 1st
        rankedTeams(1) shouldEqual ("Team A", 3, 2)     // Team A is ranked 2nd
        rankedTeams(2) shouldEqual ("Team B", 3, 2)     // Team B is ranked 2nd
      case Left(_) => fail("Expected success but got failure")
    }
  }

  test("TeamRanker should correctly rank a single team") {
    val points = Map("Team A" -> 9)

    teamRanker.rankTeams(points) match {
      case Right(rankedTeams) =>
        rankedTeams.length shouldEqual 1
        rankedTeams.head shouldEqual ("Team A", 9, 1)  // Team A is ranked 1st
      case Left(_) => fail("Expected success but got failure")
    }
  }

  test("TeamRanker should correctly rank multiple teams with varying points") {
    val points = Map(
      "Team A" -> 7,
      "Team B" -> 4,
      "Team C" -> 6
    )

    teamRanker.rankTeams(points) match {
      case Right(rankedTeams) =>
        rankedTeams.length shouldEqual 3
        rankedTeams.head shouldEqual ("Team A", 7, 1)  // Team A is ranked 1st
        rankedTeams(1) shouldEqual ("Team C", 6, 2)    // Team C is ranked 2nd
        rankedTeams(2) shouldEqual ("Team B", 4, 3)    // Team B is ranked 3rd
      case Left(_) => fail("Expected success but got failure")
    }
  }
}
