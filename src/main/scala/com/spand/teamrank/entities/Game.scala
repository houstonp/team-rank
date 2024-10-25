package com.spand.teamrank.entities

import scala.util.{Try, Success, Failure}

case class Game(team1Name: String, team1Score: Int, team2Name: String, team2Score: Int)

object Game {
  def extract(source: String): Either[String, Game] = {
    val teamSplit = source.split(",").map(_.trim)
    if (teamSplit.length != 2) {
      return Left(s"Input string does not contain exactly two teams: $source")
    }

    // extract team info from game split
    val team1 = extractTeamInfo(teamSplit(0))
    val team2 = extractTeamInfo(teamSplit(1))

    (team1, team2) match {
      case (Right(t1), Right(t2)) => Right(Game(t1._1, t1._2, t2._1, t2._2))
      case (Left(err), _) => Left(err)
      case (_, Left(err)) => Left(err)
    }
  }

  private def extractTeamInfo(teamInfo: String): Either[String, (String, Int)] = {
    val lastSpaceIndex = teamInfo.lastIndexOf(" ")

    if (lastSpaceIndex == -1) {
      return Left(s"Invalid team info format: $teamInfo")
    }

    val teamName = teamInfo.substring(0, lastSpaceIndex).trim
    val scoreTry = Try(teamInfo.substring(lastSpaceIndex + 1).toInt)

    scoreTry match {
      case Success(score) if teamName.nonEmpty && score >= 0 => Right((teamName, score))
      case Success(score) if score < 0 => Left(s"Score cannot be negative for team: $teamInfo")
      case Success(_) => Left(s"Team name cannot be empty: $teamInfo")
      case Failure(_) => Left(s"Invalid score format for team: $teamInfo")
    }
  }
}