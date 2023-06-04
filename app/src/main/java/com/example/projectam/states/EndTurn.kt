package com.example.projectam.states

import android.util.Log
import com.example.projectam.ClientInfo
import com.example.projectam.FirebaseManager
import com.example.projectam.R
import com.example.projectam.utils.GameManager
import java.lang.ref.Cleaner

class EndTurn : GameState {
    override fun changeListeners(ctx: GameStateContext) {
        ctx.deckIV.setOnClickListener(null)
        ctx.stir1IV.setOnClickListener(null)
        ctx.stir2IV.setOnClickListener(null)
        if (GameManager.isRevealed(ClientInfo.game) && !ClientInfo.game.isFinished) {
            Log.d("EndTurn", "Game is finished")
            ClientInfo.game.isFinished = true
            GameManager.revealAllCards(ClientInfo.game)
        }
        if(ClientInfo.game.isFinished && ClientInfo.game.players.filterNotNull().all { player ->
                player.fields.all { card ->
                    card.value != -1
                }
            }) {
            Log.d("EndTurn", "Mirrors are revealed")
            ClientInfo.game.isCalculatedScores = true
            GameManager.calculateScores(ClientInfo.game)
        }
        GameManager.changePlayerTurn(ClientInfo.game)

        Log.d("EndTurn", "send data to Firebase")

        FirebaseManager.sendGameToServer(ClientInfo.gameCode, ClientInfo.game)
        ctx.setState(Wait())
    }
    override fun setHighlighters(ctx: GameStateContext) {
        // Nothing
    }

    override fun callSound(ctx: GameStateContext) {
        ctx.mediaPlayer.release()
    }

    override fun onItemClick(position: Int) {
        // Nothing
    }
}