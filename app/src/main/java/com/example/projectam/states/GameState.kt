package com.example.projectam.states

import android.media.MediaPlayer
import com.example.projectam.R

interface GameState {
    fun changeListeners(ctx: GameStateContext)
    fun setHighlighters(ctx: GameStateContext)
    fun callSound(ctx: GameStateContext) {
        ctx.mediaPlayer.release()
        ctx.mediaPlayer = MediaPlayer.create(ctx.context, R.raw.card_flip)
        ctx.mediaPlayer.start()
    }
    fun onItemClick(position: Int)
}