package com.example.projectam

import com.example.projectam.utils.Game

class ClientInfo {
    companion object {
        var username: String = ""
        var id: Int = -1
        var gameCode: String = ""
        lateinit var game: Game

        fun init(code: String, name: String) {
            username = name
            gameCode = code
        }
    }
}