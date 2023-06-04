package com.example.projectam.states

import android.util.Log
import com.example.projectam.ClientInfo
import com.example.projectam.R
import com.example.projectam.activities.GameActivity
import com.example.projectam.utils.Card
import com.example.projectam.utils.GameManager
import com.example.projectam.utils.ImageConverter

class ChoosingField(var isStirChosen: Boolean = false) : GameState {
    override fun changeListeners(ctx: GameStateContext) {
        Log.d("ChoosingField", "changeListeners")
        ctx.deckIV.setOnClickListener (null)
        if(ClientInfo.chosenCard.value == 10 || isStirChosen) {
            ctx.stir1IV.setOnClickListener(null)
            ctx.stir2IV.setOnClickListener(null)
            return
        }
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
        ctx.playerHighlighters?.setBackgroundResource(R.drawable.highlight_border)
        if(ClientInfo.chosenCard.value == 10 || isStirChosen)
            return
        ctx.stir1Highlighter.setBackgroundResource(R.drawable.highlight_border)
        ctx.stir2Highlighter.setBackgroundResource(R.drawable.highlight_border)
    }

    override fun onItemClick(position: Int) {
        Log.d("ChoosingField", "click on player field")
        for (player in ClientInfo.game.players) {
            if (player == null)
                continue
            if (player.id != ClientInfo.id)
                continue
            Log.d("ChoosingField", "update state")
            val tempCard = player.fields[position]
            player.fields[position] = ClientInfo.chosenCard
            ClientInfo.chosenCard = tempCard

            ClientInfo.chosenCard.reveal()
            GameActivity.hintCardIV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))

            if(player.fields[position].value == 9) {
                GameActivity.currentState.setState(ChoosingPair(position))
                return
            }
            if(player.fields[position].value == 10) {
                GameManager.swapTen(ClientInfo.game, position)
                GameActivity.hintCardIV.setImageResource(ImageConverter.getImage(ClientInfo.chosenCard))
            }
            GameActivity.currentState.setState(ChoosingStir())
            break
        }
    }
}

