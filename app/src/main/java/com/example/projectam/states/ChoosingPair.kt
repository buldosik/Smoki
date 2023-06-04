package com.example.projectam.states

import android.util.Log
import com.example.projectam.ClientInfo
import com.example.projectam.R
import com.example.projectam.activities.GameActivity
import com.example.projectam.utils.Card
import com.example.projectam.utils.GameManager
import com.example.projectam.utils.ImageConverter

class ChoosingPair (var positionOfNine : Int) : GameState {

    var firstClick: Int = -1

    override fun changeListeners(ctx: GameStateContext) {
        Log.d("ChoosingPair", "changeListeners")
        ctx.deckIV.setOnClickListener(null)
        ctx.stir1IV.setOnClickListener{
            Log.d("ChoosingField", "click on stir1")
            ctx.stir1IV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))
            ctx.hintCardIV.setImageResource(ImageConverter.getImage(Card(-10, false)))

            GameManager.addToStir1(ClientInfo.game, ClientInfo.chosenCard)
            ctx.setState(EndTurn())
        }
        ctx.stir2IV.setOnClickListener{
            Log.d("ChoosingField", "click on stir2")
            ctx.stir2IV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))
            ctx.hintCardIV.setImageResource(ImageConverter.getImage(Card(-10, false)))

            GameManager.addToStir2(ClientInfo.game, ClientInfo.chosenCard)
            ctx.setState(EndTurn())
        }
    }

    override fun setHighlighters(ctx: GameStateContext) {
        ctx.deckHighlighter.setBackgroundResource(R.drawable.highlight_border)
        ctx.playerHighlighters.setBackgroundColor(R.drawable.highlight_border)
        ctx.stir1Highlighter.setBackgroundResource(R.drawable.highlight_border)
        ctx.stir2Highlighter.setBackgroundResource(R.drawable.highlight_border)
    }

    override fun onItemClick(position: Int) {
        if(position == positionOfNine)
            return
        if(firstClick == -1) {
            Log.d("ChoosingPair", "remember first card")
            firstClick = position
            return
        }
        for (player in ClientInfo.game.players) {
            if (player == null)
                continue
            if (player.id != ClientInfo.id)
                continue
            Log.d("ChoosingPair", "swapping cards")
            val tempCard = player.fields[firstClick]
            player.fields[firstClick] = player.fields[position]
            player.fields[position] = tempCard

            GameActivity.currentState.setState(ChoosingStir())
        }
    }
}