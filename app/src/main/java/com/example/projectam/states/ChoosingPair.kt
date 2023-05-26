package com.example.projectam.states

import android.util.Log
import com.example.projectam.ClientInfo
import com.example.projectam.activities.GameActivity
import com.example.projectam.utils.Card
import com.example.projectam.utils.ImageConverter

class ChoosingPair (var positionOfNine : Int) : GameState {

    var firstClick: Int = -1

    override fun changeListeners(ctx: GameStateContext) {
        Log.d("ChoosingPair", "changeListeners")
        ctx.deckIV.setOnClickListener(null)
        ctx.stir1IV.setOnClickListener{
            Log.d("ChoosingField", "click on stir1")
            ctx.stir1IV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))
            ctx.deckIV.setBackgroundColor(0)
            ctx.stir1IV.setBackgroundResource(0)
            ctx.stir2IV.setBackgroundResource(0)
            ctx.hintCardIV.setImageResource(ImageConverter.getImage(Card(-10, false)))

            ClientInfo.game.addToStir1(ClientInfo.chosenCard)
            ctx.setState(EndTurn())
        }
        ctx.stir2IV.setOnClickListener{
            Log.d("ChoosingField", "click on stir2")
            ctx.stir2IV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))
            ctx.deckIV.setBackgroundColor(0)
            ctx.stir1IV.setBackgroundResource(0)
            ctx.stir2IV.setBackgroundResource(0)
            ctx.hintCardIV.setImageResource(ImageConverter.getImage(Card(-10, false)))

            ClientInfo.game.addToStir2(ClientInfo.chosenCard)
            ctx.setState(EndTurn())
        }
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