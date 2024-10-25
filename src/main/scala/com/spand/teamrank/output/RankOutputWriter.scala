package com.spand.teamrank.output

import java.io.PrintWriter
import scala.util.{Try, Using}

class RankOutputWriter {
  def writeRankingToFile(ranking: List[(String, Int, Int)], outputPath: String): Try[Unit] = {
    Using(new PrintWriter(outputPath)) { pw =>
      ranking.foreach { case (team, points, rank) =>
        pw.write(s"$rank. $team, $points pts\n")
      }
    }
  }
}
