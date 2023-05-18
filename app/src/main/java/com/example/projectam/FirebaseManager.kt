package com.example.projectam

import android.util.Log
import com.example.projectam.utils.Game
import com.example.projectam.utils.Player
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseManager {
    companion object {
        private lateinit var database: FirebaseDatabase

        fun init() {
            // Get Connection to Firebase database
            database = Firebase.database
        }

        fun createNewLobby(code: String, username: String) {
            val game = Game(code = code)
            // Add host player
            game.addPlayer(Player(id = 0, username = username, isConnected = true))
            // Set lobby at firebase
            database.getReference(code).setValue(game)
        }

        //ToDo return created player's id
        fun addPlayer(code: String, player: Player) : Int{
            return -1;
        }

        fun getData(code: String) {

        }

        fun getPlayers(code: String) : MutableList<Player> {
            return mutableListOf()
        }
    }
}