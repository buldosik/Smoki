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
        val dragons = listOf(-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 10)
        //add dragons
        for (dragon in dragons) {
            repeat(4) {
                val card = Card(dragon, false)
                cardDeck.add(card)
            }
        }
        shuffle(cardDeck)
    }
    fun shuffle(deck: MutableList<Card>) {
        deck.shuffle()
    }

    fun stirReset(stir: MutableList<Card>) {
        // leaves top cards in stir decks. All the other cards go in cardDeck
        while (stir.size > 1) {
            cardDeck.add(stir[0])
            stir.removeAt(0)
        }
    }
    fun addCardToStir1(card: Card){
        stirDeck1.add(card)
    }
    fun addCardToStir2(card: Card){
        stirDeck2.add(card)
    }

    fun changePlayerTurn() {
        playerTurn = (playerTurn + 1) % players.size
    }
    //reveals all players cards
    fun revevealAllPlayersCards(){
        for(player in players){
            for(card in player.fields){
                if(!card.isRevealed){
                    card.reveal()
                }
            }
        }
    }
    fun swapTen(cardTen: Card, cardsPosition: Int, playersId: Int){
        //the idea is to go from an el: a -> 0 from 4 -> a+1

        var cardToBeChanged = cardTen
        var temporaryCard = players[playersId].fields[cardsPosition]
        if(players.size == 1){
            players[playersId].fields[cardsPosition] = cardToBeChanged
            cardToBeChanged = temporaryCard
            return
        }
        // goes for example from [2->1->0]
        for (id in playersId downTo 0){
            if(!players[id].isConnected) continue;
            players[id].fields[cardsPosition] = cardToBeChanged
            cardToBeChanged = temporaryCard
        }
        // goes f.ex from [4->3->2)
        for(id in players.size - 1 downTo playersId+1){
            if(!players[id].isConnected) continue;
            players[id].fields[cardsPosition] = cardToBeChanged
            cardToBeChanged = temporaryCard
        }
    }
    fun swapNine(cardsPositionFrom: Int, cardsPositionTo: Int, playersId: Int) {
        val temporary = players[playersId].fields[cardsPositionFrom]
        players[playersId].fields[cardsPositionFrom] = players[playersId].fields[cardsPositionTo]
        players[playersId].fields[cardsPositionTo] = temporary
    }
    fun copyNextCardsValue(cardsPositionReflactor: Int, cardsPositionBeReflacted: Int, playersId: Int){
        when (cardsPositionReflactor) {
            1, 4 -> {
                players[playersId].fields[cardsPositionReflactor].value = players[playersId].fields[cardsPositionReflactor-1].value
            }
            0, 3 -> {
                players[playersId].fields[cardsPositionReflactor].value= players[playersId].fields[cardsPositionReflactor+1].value
            }
            else -> {
                players[playersId].fields[cardsPositionReflactor].value= players[playersId].fields[cardsPositionBeReflacted].value
            }
        }
    }
}