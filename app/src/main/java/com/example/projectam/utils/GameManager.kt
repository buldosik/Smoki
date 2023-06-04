package com.example.projectam.utils

import com.example.projectam.ClientInfo

class GameManager {
    companion object {
        fun clearPlayerList(game: Game) {
            game.players.clear()
            game.players.add(null)
            game.players.add(null)
            game.players.add(null)
            game.players.add(null)
            game.players.add(null)
        }
        fun addPlayer(game: Game, player: Player) {
            for(id in 0 until game.players.size) {
                if(game.players[id] == null) {
                    // Set for client id
                    ClientInfo.id = id
                    // Set for server id
                    player.id = id
                    game.players[id] = player
                    return
                }
            }
            ClientInfo.id = game.players.size
            // Set for server id
            player.id = game.players.size
            game.players.add(player)
        }

        fun createNewDeck(game: Game) {
            val cardsValues = listOf(-2, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 10, -1)
            //add cards
            for (i in cardsValues) {
                repeat(4) {
                    val card = Card(i, false)
                    game.cardDeck.add(card)
                }
            }
            shuffle(game.cardDeck)
        }
        private fun shuffle(deck: MutableList<Card>) {
            deck.shuffle()
        }
        //leaves top cards in stirs decks. All the other cards go in cardDeck
        private fun stirReset(game: Game) {
            val card1 = game.stirDeck1[game.stirDeck1.size - 1]
            game.stirDeck1.removeAt(game.stirDeck1.size - 1)
            val card2 = game.stirDeck2[game.stirDeck2.size - 1]
            game.stirDeck2.removeAt(game.stirDeck2.size - 1)
            val stir = mutableListOf<Card>()
            game.stirDeck1.forEach { card -> card.isRevealed = false }
            game.stirDeck2.forEach { card -> card.isRevealed = false }
            stir.addAll(game.stirDeck1)
            stir.addAll(game.stirDeck2)
            shuffle(stir)
            game.stirDeck1.clear()
            game.stirDeck1.add(card1)
            game.stirDeck2.clear()
            game.stirDeck2.add(card2)
            stir.addAll(game.cardDeck)
            game.cardDeck.clear()
            game.cardDeck.addAll(stir)
        }


        fun addToStir1(game: Game, card: Card) {
            game.stirDeck1.add(card)
        }

        fun addToStir2(game: Game, card: Card) {
            game.stirDeck2.add(card)
        }

        fun changePlayerTurn(game: Game) {
            for(i in 1 until game.players.size) {
                if(game.players[(game.playerTurn + i) % game.players.size] == null)
                    continue
                game.playerTurn = (game.playerTurn + i) % game.players.size
                break
            }
        }

        fun getCurrentPlayerIndex(game: Game) : Int {
            for (i in 0 until game.players.size) {
                if(game.players[i] == null) {
                    continue
                }
                if (game.players[i]!!.id == ClientInfo.id) {
                    return i
                }
            }
            return -1
        }
        fun getCurrentPlayer(game: Game) : Player? {
            for (i in 0 until game.players.size) {
                if(game.players[i] == null) {
                    continue
                }
                if (game.players[i]!!.id == ClientInfo.id) {
                    return game.players[i]
                }
            }
            return null
        }

        fun isRevealed(game: Game): Boolean {
            val flag = game.players.filterNotNull().any { player ->
                player.fields.all { card ->
                    card.isRevealed
                }
            }
            return flag
        }
        fun swapTen(game: Game, position: Int) {
            val startIndex = getCurrentPlayerIndex(game)
            var cardToBeChanged: Card
            ClientInfo.chosenCard.reveal()

            for (i in 1 until game.players.size) {
                val index = (startIndex + i) % game.players.size
                if(game.players[index] == null)
                    continue
                cardToBeChanged = game.players[index]!!.fields[position]
                game.players[index]!!.fields[position] = ClientInfo.chosenCard
                ClientInfo.chosenCard = cardToBeChanged
                ClientInfo.chosenCard.reveal()
            }
        }

        fun revealAllCards(game: Game) {
            for(player in game.players) {
                if(player == null)
                    continue
                for (card in player.fields) {
                    card.reveal()
                }
            }
        }

        fun calculateScores(game: Game) {
            for(player in game.players) {
                if(player == null)
                    continue
                player.calculateScore()
            }
        }
        fun getCardFromCardDeck(game: Game, isRevealed: Boolean = false): Card {
            if(game.cardDeck.isEmpty())
                stirReset(game)
            val card = game.cardDeck[game.cardDeck.size - 1]
            game.cardDeck.removeAt(game.cardDeck.size - 1)
            if(isRevealed)
                card.reveal()
            return card
        }
        fun getCardFromStir1(game: Game): Card {
            val card = game.stirDeck1[game.stirDeck1.size - 1]
            game.stirDeck1.removeAt(game.stirDeck1.size - 1)
            return card
        }
        fun getCardFromStir2(game: Game): Card {
            val card = game.stirDeck2[game.stirDeck2.size - 1]
            game.stirDeck2.removeAt(game.stirDeck2.size - 1)
            return card
        }
    }
}