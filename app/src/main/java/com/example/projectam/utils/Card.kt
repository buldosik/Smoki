package com.example.projectam.utils

import com.example.projectam.R

data class Card(
    var value: Int = 0,
    var isRevealed: Boolean = false
) {
    fun reveal() {
        isRevealed = true
    }
}
