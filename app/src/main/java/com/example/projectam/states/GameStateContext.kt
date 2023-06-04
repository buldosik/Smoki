package com.example.projectam.states

import android.content.Context
import android.media.MediaPlayer
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.projectam.R

class GameStateContext (
    var updatePlayerListener: () -> Unit,
    var playerHighlighters: FrameLayout,
    var deckIV: ImageView,
    var deckHighlighter: FrameLayout,
    var stir1IV: ImageView,
    var stir1Highlighter: FrameLayout,
    var stir2IV: ImageView,
    var stir2Highlighter: FrameLayout,
    var hintCardIV: ImageView,
    var context: Context
    ) {
    var currentState: GameState
    var mediaPlayer: MediaPlayer

    init{
        currentState = Wait()
        mediaPlayer = MediaPlayer.create(context, R.raw.notification)
    }

    fun setState(state: GameState) {
        currentState = state
        clearHighlighters()
        setState()
        updatePlayerListener()
    }

    private fun setState() {
        currentState.changeListeners(this)
        currentState.setHighlighters(this)
        currentState.callSound(this)
    }

    private fun clearHighlighters() {
        playerHighlighters.setBackgroundResource(0)
        deckHighlighter.setBackgroundResource(0)
        stir1Highlighter.setBackgroundResource(0)
        stir2Highlighter.setBackgroundResource(0)
    }
}