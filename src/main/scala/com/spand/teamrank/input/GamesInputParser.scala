package com.spand.teamrank.input

import com.spand.teamrank.entities.Game
import scala.io.Source
import scala.util.{Try, Using}

class GamesInputParser {
  def readInput(inputPath: String): Try[List[Game]] = {
    Using(Source.fromFile(inputPath)) { source =>
      source.getLines().toList.flatMap(Game.extract(_) match {
        case Right(game) => Some(game)
        case Left(err) => throw new IllegalArgumentException(err)
      })
    }
  }
}
