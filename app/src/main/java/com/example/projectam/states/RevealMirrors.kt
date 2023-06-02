package com.example.projectam.states

import android.util.Log
import com.example.projectam.ClientInfo
import com.example.projectam.activities.GameActivity
import com.example.projectam.utils.GameManager
import kotlin.math.abs

class RevealMirrors : GameState {
    override fun changeListeners(ctx: GameStateContext) {
        ctx.deckIV.setOnClickListener(null)
        ctx.stir1IV.setOnClickListener(null)
        ctx.stir2IV.setOnClickListener(null)
    }

    var firstClick: Int = -1

    override fun onItemClick(position: Int) {
        if(firstClick == -1 && ClientInfo.game.players[GameManager.getCurrentPlayerIndex(ClientInfo.game)].fields[position].value == -1) {
            Log.d("RevealMirrors", "remember first card")
            firstClick = position
            return
        }
        if(ClientInfo.game.players[GameManager.getCurrentPlayerIndex(ClientInfo.game)].fields[position].value != -1 && abs(position - firstClick) != 1){
            Log.d("RevealMirrors", "not separate card")
            return
        }
        for (player in ClientInfo.game.players) {
            if (player.id != ClientInfo.id)
                continue
            Log.d("RevealMirrors", "revealing mirror")
            player.fields[firstClick] = player.fields[position]

            if(player.fields.all { card -> card.value != -1 })
                GameActivity.currentState.setState(EndTurn())
            else
                firstClick = -1
        }
    }

}
