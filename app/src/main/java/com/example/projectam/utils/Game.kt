package com.example.projectam.utils

import com.example.projectam.ClientInfo

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
        var id = 0
        while(id < players.size) {
            id++
        }
        // Set for client id
        ClientInfo.id = id
        // Set for server id
        player.id = id
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