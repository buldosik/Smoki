package com.example.projectam.states

import android.util.Log
import com.example.projectam.ClientInfo
import com.example.projectam.R
import com.example.projectam.utils.Card
import com.example.projectam.utils.GameManager
import com.example.projectam.utils.ImageConverter

class ChoosingStir : GameState {
    override fun changeListeners(ctx: GameStateContext) {
        Log.d("ChoosingStir", "changeListeners")
        ctx.deckIV.setOnClickListener (null)
        ctx.stir1IV.setOnClickListener{
            Log.d("ChoosingStir", "click on stir1")

            ctx.stir1IV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))
            ctx.hintCardIV.setImageResource(ImageConverter.getImage(Card(-10, false)))

            GameManager.addToStir1(ClientInfo.game, ClientInfo.chosenCard)
            ctx.setState(EndTurn())
        }
        ctx.stir2IV.setOnClickListener{
            Log.d("ChoosingStir", "click on stir2")

            ctx.stir2IV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))
            ctx.hintCardIV.setImageResource(ImageConverter.getImage(Card(-10, false)))

            GameManager.addToStir2(ClientInfo.game, ClientInfo.chosenCard)
            ctx.setState(EndTurn())
        }
    }
    override fun setHighlighters(ctx: GameStateContext) {
        ctx.stir1Highlighter.setBackgroundResource(R.drawable.highlight_border)
        ctx.stir2Highlighter.setBackgroundResource(R.drawable.highlight_border)
    }

    override fun onItemClick(position: Int) {
        // Nothing
    }
}