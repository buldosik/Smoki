package com.example.projectam.states

import android.util.Log
import com.example.projectam.ClientInfo
import com.example.projectam.activities.GameActivity
import com.example.projectam.utils.ImageConverter

class RevealFirstCard : GameState {
    override fun changeListeners(ctx: GameStateContext) {
        ctx.deckIV.setOnClickListener(null)
        ctx.stir1IV.setOnClickListener(null)
        ctx.stir2IV.setOnClickListener(null)
    }

    override fun onItemClick(position: Int) {
        Log.d("RevealFirstCard", "click on player field")
        for (player in ClientInfo.game.players) {
            if (player.id != ClientInfo.id)
                continue
            Log.d("RevealFirstCard", "reveal card")
            player.fields[position].reveal()

            GameActivity.currentState.setState(ChoosingDeck())
            break
        }
    }
}