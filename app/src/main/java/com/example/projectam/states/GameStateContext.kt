package com.example.projectam.states

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.ClientInfo
import com.example.projectam.activities.GameActivity
import com.example.projectam.adapters.GameAdapter
import com.example.projectam.utils.Card
import com.example.projectam.utils.Game

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

    init{
        currentState = Wait()
    }

    fun setState(state: GameState) {
        currentState = state
        clearHighlighters()
        setListeners()
        updatePlayerListener()
    }
    private fun setListeners() {
        currentState.changeListeners(this)
        currentState.setHighlighters(this)
    }

    private fun clearHighlighters() {
        playerHighlighters.setBackgroundResource(0)
        deckHighlighter.setBackgroundResource(0)
        stir1Highlighter.setBackgroundResource(0)
        stir2Highlighter.setBackgroundResource(0)
    }
}