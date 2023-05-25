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