package com.example.projectam.states

import android.util.Log

class Wait : GameState {
    override fun changeListeners(ctx: GameStateContext) {
        Log.d("Wait", "waiting")
        ctx.deckIV.setOnClickListener(null)
        ctx.stir1IV.setOnClickListener(null)
        ctx.stir2IV.setOnClickListener(null)
    }

    override fun onItemClick(position: Int) {
        // Nothing
    }
}