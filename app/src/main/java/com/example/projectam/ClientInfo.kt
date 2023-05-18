package com.example.projectam

class ClientInfo {
    companion object {
        var username: String = ""
        var id: Int = -1
        var gameCode: String = ""

        fun init(code: String, name: String) {
            username = name
            gameCode = code
        }
    }
}