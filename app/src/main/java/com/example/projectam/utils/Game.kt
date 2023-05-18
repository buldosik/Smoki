package com.example.projectam.utils

data class Game (
    var code: String = "",
    var players: MutableList<Player> = mutableListOf(),
    var cardDeck: MutableList<Card> = mutableListOf(),
    var stirDeck1: MutableList<Card> = mutableListOf(),
    var stirDeck2: MutableList<Card> = mutableListOf(),
    var isStarted: Boolean = false,
    var playerTurn: Int = 0
        ) {
    fun addPlayer(player: Player) {
        //ToDo add a player with some id, correspond to an empty slots
        players.add(player)
    }

    fun createNewDeck() {
        //ToDo add all cards to deck
    }
    fun shuffle() {
        //ToDo shuffle Deck
    }
    fun stirReset(stir: List<Card>) {
        //ToDo update current Card
    }
    fun getCardFromDeck() {
        //ToDo return top card
    }

    fun changePlayerTurn() {
        playerTurn = (playerTurn + 1) % players.size
    }
}