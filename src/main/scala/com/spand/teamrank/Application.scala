package com.spand.teamrank

import com.spand.teamrank.calc.PointsCalculator
import com.spand.teamrank.input.GamesInputParser
import com.spand.teamrank.output.RankOutputWriter
import com.spand.teamrank.rank.TeamRanker
import scala.util.{Failure, Success}

object Application {
    def main(args: Array[String]): Unit = {
        val exitCode = run(args)
        System.exit(exitCode)
    }

    private def run(args: Array[String]): Int = {
        validateArgs(args) match {
            case Right((inputPath, outputPath)) =>
                runRanking(inputPath, outputPath)
                0  // Success
            case Left(errorMessage) =>
                System.err.println(errorMessage)
                1  // Failure
        }
    }

    private def validateArgs(args: Array[String]): Either[String, (String, String)] = {
        if (args.length != 2) {
            Left("Expected two arguments: <input file path> <output file path>. Please provide both.")
        } else {
            Right((args(0), args(1)))
        }
    }

    private def runRanking(inputPath: String, outputPath: String): Unit = {
        val gamesInputParser = new GamesInputParser
        val pointsCalculator = new PointsCalculator
        val teamRanker = new TeamRanker
        val rankOutputWriter = new RankOutputWriter

        gamesInputParser.readInput(inputPath) match {
            case Success(games) =>
                pointsCalculator.calculatePoints(games) match {
                    case Right(points) =>
                        teamRanker.rankTeams(points) match {
                            case Right(rankedPoints) =>
                                // If ranking succeeded, write to output
                                rankOutputWriter.writeRankingToFile(rankedPoints, outputPath) match {
                                    case Success(_) => println("Ranking written successfully. Please check your output file.")
                                    case Failure(ex) => println(s"Error writing ranking: ${ex.getMessage}")
                                }
                            case Left(errorMessage) =>
                                // Handle the error from TeamRanker
                                println(s"Error ranking teams: $errorMessage")
                        }
                    case Left(errorMessage) =>
                        // Handle the error from PointsCalculator
                        println(s"Error calculating points: $errorMessage")
                }
            case Failure(exception) =>
                // Handle failure in reading games from the input file
                println(s"Error reading games: ${exception.getMessage}")
        }
    }
}