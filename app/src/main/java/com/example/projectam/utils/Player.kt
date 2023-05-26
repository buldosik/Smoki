package com.example.projectam.utils

data class Player (
    var id: Int = 0,
    var username: String = "",
    var isConnected: Boolean = false,
    var fields: MutableList<Card> = mutableListOf(),
    var score: Int = 0
        ) {
    fun isRevealedAny(): Boolean {
        val flag = fields.any { card ->
            card.isRevealed
        }
        return flag
    }

    fun calculateScore() {
        score = 0
        for(i in 0..2) {
            if(fields[i] != fields[i + 3]) {
                score += fields[i].value
                score += fields[i + 3].value
            }
        }
    }
}