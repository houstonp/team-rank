package com.spand.teamrank.output

import java.io.PrintWriter
import scala.util.{Try, Using}

class RankOutputWriter {
  def writeRankingToFile(ranking: List[(String, Int, Int)], outputPath: String): Try[Unit] = {
    Using(new PrintWriter(outputPath)) { pw =>
      ranking.foreach { case (team, points, rank) =>
        pw.write(s"$rank. $team, $points ${if(points == 1) "pt" else "pts"}\n")
      }
    }
  }
}
