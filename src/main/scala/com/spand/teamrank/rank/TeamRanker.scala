package com.spand.teamrank.rank

class TeamRanker {
  def rankTeams(points: Map[String, Int]): Either[String, List[(String, Int, Int)]] = {
    if (points.isEmpty) {
      Left("Cannot rank teams: No points provided.")
    } else {
      // Sort such that points is in descending order and team name is in alphabetical order should two teams,
      // have the same points
      val sortedPoints = points.toSeq.sortBy(x => (-x._2, x._1))

      val rankedTeams = sortedPoints.zipWithIndex.foldLeft(List[(String, Int, Int)]()) {
        case (acc, ((team, points), index)) =>
          // acquire previous team rank
          val previousRank = if (acc.isEmpty) 1 else acc.last._3
          // increment index by 1 to use as the next rank only if the previous team's points are not the same,
          // or the accumulator is empty
          val currentRank = if (acc.isEmpty || acc.last._2 != points) index + 1 else previousRank
          acc :+ (team, points, currentRank)
      }

      Right(rankedTeams)
    }
  }
}
