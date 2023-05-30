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
        stirDeck1.forEach { card -> card.isRevealed = false }
        stirDeck2.forEach { card -> card.isRevealed = false }
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
        for(i in 0 until players.size) {
            if(players[i].id != playerTurn)
                continue
            playerTurn = if(i == players.size - 1)
                players[0].id
            else
                players[i + 1].id
            break
        }
    }

    fun getCurrentPlayerIndex() : Int {
        for (i in 0 until players.size)
            if (players[i].id == ClientInfo.id)
                return i
        return -1
    }

    fun isRevealed(): Boolean {
        val flag = players.any { player ->
            player.fields.all { card ->
                card.isRevealed
            }
        }
        return flag
    }
    fun swapTen(position: Int) {
        val startIndex = getCurrentPlayerIndex()
        var cardToBeChanged: Card

        for (i in 1 until players.size) {
            val index = (startIndex + i) % players.size
            cardToBeChanged = players[index].fields[position]
            players[index].fields[position] = ClientInfo.chosenCard
            ClientInfo.chosenCard = cardToBeChanged
            ClientInfo.chosenCard.reveal()
        }
    }

    fun revealAllCards() {
        for(player in players)
            for(card in player.fields)
                card.reveal()
    }

    fun calculateScores() {
        for(player in players) {
            player.calculateScore()
        }
    }
}