package com.example.projectam.states

interface GameState {
    fun changeListeners(ctx: GameStateContext)
    fun onItemClick(position: Int)
}