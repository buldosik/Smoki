package com.example.projectam.states

import android.util.Log
import com.example.projectam.R

class Wait : GameState {
    override fun changeListeners(ctx: GameStateContext) {
        Log.d("Wait", "waiting")
        ctx.deckIV.setOnClickListener(null)
        ctx.stir1IV.setOnClickListener(null)
        ctx.stir2IV.setOnClickListener(null)
    }
    override fun setHighlighters(ctx: GameStateContext) {
        // Nothing
    }
    override fun onItemClick(position: Int) {
        // Nothing
    }
}