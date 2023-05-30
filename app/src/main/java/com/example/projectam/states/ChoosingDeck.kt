package com.example.projectam.states

import android.util.Log
import android.widget.Toast
import com.example.projectam.ClientInfo
import com.example.projectam.R
import com.example.projectam.utils.CardManager
import com.example.projectam.utils.ImageConverter

class ChoosingDeck : GameState {
    override fun changeListeners(ctx: GameStateContext) {
        Log.d("ChoosingDeck", "changeListeners")
        ctx.deckIV.setOnClickListener{ view ->
            view.setBackgroundResource(R.drawable.image_border)
            Log.d("ChoosingDeck", "click on deck")

            ClientInfo.chosenCard = CardManager.getCardFromCardDeck(ClientInfo.game)
            ClientInfo.chosenCard.reveal()
            ctx.hintCardIV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))

            ctx.setState(ChoosingField())
        }
        ctx.stir1IV.setOnClickListener{ view ->
            if (ClientInfo.game.stirDeck1.isEmpty()) {
                Toast.makeText(ctx.context, "Stir has no cards", Toast.LENGTH_SHORT).show()
            }
            else {
                view.setBackgroundResource(R.drawable.image_border)
                Log.d("ChoosingDeck", "click on stir1")
                ctx.setState(ChoosingField(true))
                ClientInfo.chosenCard = CardManager.getCardFromStir1(ClientInfo.game)
                ctx.hintCardIV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))

                if (ClientInfo.game.stirDeck1.isNotEmpty()){
                    ctx.stir1IV.setImageResource(ImageConverter.getImage(ClientInfo.game.stirDeck1[ClientInfo.game.stirDeck1.size - 1]))
                }
                else {
                    ctx.stir1IV.setImageResource(R.drawable.close_image_vert)
                }
            }
        }
        ctx.stir2IV.setOnClickListener{ view ->
            if (ClientInfo.game.stirDeck2.isEmpty()) {
                Toast.makeText(ctx.context, "Stir has no cards", Toast.LENGTH_SHORT).show()
            }
            else {
                Log.d("ChoosingDeck", "click on stir2")
                view.setBackgroundResource(R.drawable.image_border)
                ctx.setState(ChoosingField(true))
                ClientInfo.chosenCard = CardManager.getCardFromStir2(ClientInfo.game)
                ctx.hintCardIV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))

                if (ClientInfo.game.stirDeck2.isNotEmpty()){
                    ctx.stir2IV.setImageResource(ImageConverter.getImage(ClientInfo.game.stirDeck2[ClientInfo.game.stirDeck2.size - 1]))
                }
                else {
                    ctx.stir2IV.setImageResource(R.drawable.close_image_vert)
                }
            }
        }
    }

    override fun onItemClick(position: Int) {
        // Nothing
    }

}