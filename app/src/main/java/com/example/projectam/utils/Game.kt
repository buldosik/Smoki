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
            if(id != players[id].id)
                break
            id++
        }
        // Set for client id
        ClientInfo.id = id
        // Set for server id
        player.id = id
        players.add(player)
    }

    fun createNewDeck() {
        // ToDo add all cards to cardDeck
    }
    private fun shuffle(deck: MutableList<Card>) {
        // ToDo shuffle deck
    }

    private fun stirReset() {
        // ToDo leaves top cards in stir decks. All the other cards go in cardDeck
    }
    fun getCardFromCardDeck(isRevealed: Boolean = false): Card {
        // ToDo Return top card from cardDeck
        return Card(0, true)
    }
    fun getCardFromStir1(): Card {
        // ToDo Return top card from stir1
        return Card(0,true)
    }
    fun getCardFromStir2(): Card {
        // ToDo Return top card from stir2
        return Card(0,true)
    }

    fun changePlayerTurn() {
        playerTurn = (playerTurn + 1) % players.size
    }
}