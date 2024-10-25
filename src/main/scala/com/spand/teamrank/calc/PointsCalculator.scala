package com.spand.teamrank.calc

import com.spand.teamrank.entities.Game

class PointsCalculator {
  def calculatePoints(games: List[Game]): Either[String, Map[String, Int]] = {
    if (games.isEmpty) {
      return Left("No games provided to calculate points.")
    }

    // Fold over the list of games and calculate points
    games.foldLeft(Right(Map.empty[String, Int]): Either[String, Map[String, Int]]) {
      case (accEither, game) =>
        accEither.flatMap { acc =>
          if (isValidGame(game)) {
            Right(updatePointsForGame(acc, game))
          } else {
            Left(s"Invalid game data found: $game")
          }
        }
    }
  }

  private def isValidGame(game: Game): Boolean = {
    game.team1Name.nonEmpty && game.team2Name.nonEmpty && game.team1Score >= 0 && game.team2Score >= 0
  }

  private def updatePointsForGame(acc: Map[String, Int], game: Game): Map[String, Int] = {
    // Winner gets 3 points, Loser gets 0 points
    def updateWinner(acc: Map[String, Int], winner: String, loser: String): Map[String, Int] = {
      val updatedAcc = acc + (winner -> (acc.getOrElse(winner, 0) + 3))
      updatedAcc + (loser -> acc.getOrElse(loser, 0))
    }

    // Draw, both teams get 1 point
    def updateDraw(acc: Map[String, Int], team1: String, team2: String): Map[String, Int] = {
      val updatedAcc = acc + (team1 -> (acc.getOrElse(team1, 0) + 1))
      updatedAcc + (team2 -> (acc.getOrElse(team2, 0) + 1))
    }

    // Match over team scores to assign points
    (game.team1Score, game.team2Score) match {
      case (score1, score2) if score1 > score2 => updateWinner(acc, game.team1Name, game.team2Name)
      case (score1, score2) if score1 < score2 => updateWinner(acc, game.team2Name, game.team1Name)
      case _ => updateDraw(acc, game.team1Name, game.team2Name)
    }
  }
}
