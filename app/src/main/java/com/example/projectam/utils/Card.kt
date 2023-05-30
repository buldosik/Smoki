package com.example.projectam.utils

data class Card(
    var value: Int = 0,
    var isRevealed: Boolean = false,
    var isSwapped: Boolean = false
) {
    fun reveal() {
        isRevealed = true
    }
    fun setSwap(falseFlag: Boolean){
        isSwapped = falseFlag
    }
}
