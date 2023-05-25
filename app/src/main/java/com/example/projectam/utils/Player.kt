package com.example.projectam.utils

data class Player (
    var id: Int = 0,
    var username: String = "",
    var isConnected: Boolean = false,
    var fields: MutableList<Card> = mutableListOf()
)