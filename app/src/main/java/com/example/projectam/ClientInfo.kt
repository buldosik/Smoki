package com.example.projectam

import com.example.projectam.utils.Game

class ClientInfo {
    companion object {
        var username: String = ""
        var id: Int = -1
        var gameCode: String = ""
        var isStarted: Boolean = true
        fun init(code: String, name: String) {
            username = name
            gameCode = code
        }
    }
}