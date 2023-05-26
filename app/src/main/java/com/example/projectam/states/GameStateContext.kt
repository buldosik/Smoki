package com.example.projectam.states

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.ClientInfo
import com.example.projectam.activities.GameActivity
import com.example.projectam.adapters.GameAdapter
import com.example.projectam.utils.Card
import com.example.projectam.utils.Game

class GameStateContext (
    var updatePlayerListener: () -> Unit,
    var deckIV: ImageView,
    var stir1IV: ImageView,
    var stir2IV: ImageView,
    var hintCardIV: ImageView,
    var context: Context
    ) {
    var currentState: GameState

    init{
        currentState = Wait()
    }

    fun setState(state: GameState) {
        currentState = state
        setListeners()
        updatePlayerListener()
    }
    private fun setListeners() {
        currentState.changeListeners(this)
    }
}