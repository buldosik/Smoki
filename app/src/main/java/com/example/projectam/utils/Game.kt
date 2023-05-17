package com.example.projectam.utils

import com.example.projectam.utils.Player

class Game {
    companion object {
        lateinit var currentPlayer: Player
        var players: ArrayList<Player> = arrayListOf()
    }
}