package com.example.projectam.states

import android.util.Log
import com.example.projectam.ClientInfo
import com.example.projectam.FirebaseManager

class EndTurn : GameState {
    override fun changeListeners(ctx: GameStateContext) {
        ctx.deckIV.setOnClickListener(null)
        ctx.stir1IV.setOnClickListener(null)
        ctx.stir2IV.setOnClickListener(null)
        if (ClientInfo.game.isRevealed())
            ClientInfo.game.isFinished = true
        ClientInfo.game.changePlayerTurn()

        Log.d("EndTurn", "send data to Firebase")

        FirebaseManager.sendGameToServer(ClientInfo.gameCode, ClientInfo.game)
        ctx.setState(Wait())
    }

    override fun onItemClick(position: Int) {
        // Nothing
    }
}