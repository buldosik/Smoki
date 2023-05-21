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
    private fun shuffle(deck: MutableList<Card>) {
        deck.shuffle()
    }

    private fun stirReset(stir: MutableList<Card>) {
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
        stirDeck1.add(card)
    }
    fun getCardFromCardDeck(isRevealed: Boolean = false): Card {
        // Return top card from cardDeck
        if(cardDeck.getOrNull(cardDeck.size - 1) == null){
            stirReset(stirDeck1)
            stirReset(stirDeck2)
            shuffle(cardDeck)
        }
        return cardDeck[cardDeck.size - 1]
    }
    fun getCardFromStir1(): Card {
        //  Return top card from stir1
        return stirDeck1[stirDeck1.size - 1]
    }
    fun getCardFromStir2(): Card {
        return stirDeck2[stirDeck2.size - 1]
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
}