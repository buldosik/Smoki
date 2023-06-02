package com.example.projectam.utils

import com.example.projectam.ClientInfo

data class Game (
    var code: String = "",
    var players: MutableList<Player> = mutableListOf(),
    var cardDeck: MutableList<Card> = mutableListOf(),
    var stirDeck1: MutableList<Card> = mutableListOf(),
    var stirDeck2: MutableList<Card> = mutableListOf(),
    var isStarted: Boolean = false,
    var isFinished: Boolean = false,
    var isCalculatedScores: Boolean = false,
    var playerTurn: Int = 0
        )