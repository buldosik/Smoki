package com.example.projectam.utils

class CardManager {
    companion object {
        fun getCardFromCardDeck(game: Game, isRevealed: Boolean = false): Card {
            // Return top card from cardDeck
            if(game.cardDeck[game.cardDeck.size - 1] == null){
                game.stirReset(game.stirDeck1)
                game.stirReset(game.stirDeck2)
                game.shuffle(game.cardDeck)
            }
            val card = game.cardDeck[game.cardDeck.size - 1]
            game.cardDeck.removeAt(game.cardDeck.size - 1)
            if(isRevealed)
                card.reveal()
            return card
        }
        fun getCardFromStir1(game: Game): Card {
            //  Return top card from stir1
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