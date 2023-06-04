package com.example.projectam.states

import android.media.MediaPlayer
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import com.example.projectam.ClientInfo
import com.example.projectam.R
import com.example.projectam.utils.GameManager
import com.example.projectam.utils.ImageConverter

class ChoosingDeck : GameState {
    override fun changeListeners(ctx: GameStateContext) {
        Log.d("ChoosingDeck", "changeListeners")

        ctx.deckIV.setOnClickListener{ view ->
            Log.d("ChoosingDeck", "click on deck")

            ClientInfo.chosenCard = GameManager.getCardFromCardDeck(ClientInfo.game)
            ClientInfo.chosenCard.reveal()
            ctx.hintCardIV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))

            ctx.setState(ChoosingField())
        }
        ctx.stir1IV.setOnClickListener{ view ->
            if (ClientInfo.game.stirDeck1.isEmpty()) {
                Toast.makeText(ctx.context, "Stir has no cards", Toast.LENGTH_SHORT).show()
            }
            else {
                Log.d("ChoosingDeck", "click on stir1")
                ctx.setState(ChoosingField(true))
                ClientInfo.chosenCard = GameManager.getCardFromStir1(ClientInfo.game)
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
                ctx.setState(ChoosingField(true))
                ClientInfo.chosenCard = GameManager.getCardFromStir2(ClientInfo.game)
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
    override fun setHighlighters(ctx: GameStateContext) {
        ctx.deckHighlighter.setBackgroundResource(R.drawable.highlight_border)
        ctx.stir1Highlighter.setBackgroundResource(R.drawable.highlight_border)
        ctx.stir2Highlighter.setBackgroundResource(R.drawable.highlight_border)
    }

    override fun callSound(ctx: GameStateContext) {
        ctx.mediaPlayer.release()
        ctx.mediaPlayer = MediaPlayer.create(ctx.context, R.raw.notification)
        ctx.mediaPlayer.start()
    }

    override fun onItemClick(position: Int) {
        // Nothing
    }

}