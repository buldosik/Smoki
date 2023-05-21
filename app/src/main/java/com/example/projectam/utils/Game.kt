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
        val cardsValues = listOf(-2, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 10, -1)
        //add cards
        for (i in cardsValues) {
            repeat(4) {
                val card = Card(i, false)
                cardDeck.add(card)
            }
        }
        shuffle(cardDeck)
    }
    private fun shuffle(deck: MutableList<Card>) {
        deck.shuffle()
    }
    //leaves top cards in stirs decks. All the other cards go in cardDeck
    fun stirReset() {
        val card1 = stirDeck1[stirDeck1.size - 1]
        stirDeck1.removeAt(stirDeck1.size - 1)
        val card2 = stirDeck2[stirDeck2.size - 1]
        stirDeck2.removeAt(stirDeck2.size - 1)
        val stir = mutableListOf<Card>()
        stir.addAll(stirDeck1)
        stir.addAll(stirDeck2)
        shuffle(stir)
        stirDeck1.clear()
        stirDeck1.add(card1)
        stirDeck2.clear()
        stirDeck2.add(card2)
        stir.addAll(cardDeck)
        cardDeck.clear()
        cardDeck.addAll(stir)
    }


    fun addToStir1(card: Card) {
        stirDeck1.add(card)
    }

    fun addToStir2(card: Card) {
        stirDeck2.add(card)
    }

    fun changePlayerTurn() {
        playerTurn = (playerTurn + 1) % players.size
    }

    fun checkIsFirstAction() {
        isStarted = players.any { player ->
            player.fields.all { card ->
                !card.isRevealed
            }
        }
    }
}