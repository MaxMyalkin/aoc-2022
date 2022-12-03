package day_2

import GameElement

data class Rule(
    val myElement: GameElement,
    val opponentElement: GameElement,
    val result: GameResult
)

val ruleSet = listOf(
    Rule(myElement = GameElement.ROCK, opponentElement = GameElement.ROCK, result = GameResult.DRAW),
    Rule(myElement = GameElement.ROCK, opponentElement = GameElement.PAPER, result = GameResult.LOSS),
    Rule(myElement = GameElement.ROCK, opponentElement = GameElement.SCISSORS, result = GameResult.WIN),
    Rule(myElement = GameElement.PAPER, opponentElement = GameElement.ROCK, result = GameResult.WIN),
    Rule(myElement = GameElement.PAPER, opponentElement = GameElement.PAPER, result = GameResult.DRAW),
    Rule(myElement = GameElement.PAPER, opponentElement = GameElement.SCISSORS, result = GameResult.LOSS),
    Rule(myElement = GameElement.SCISSORS, opponentElement = GameElement.ROCK, result = GameResult.LOSS),
    Rule(myElement = GameElement.SCISSORS, opponentElement = GameElement.PAPER, result = GameResult.WIN),
    Rule(myElement = GameElement.SCISSORS, opponentElement = GameElement.SCISSORS, result = GameResult.DRAW),
)