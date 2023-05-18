package com.example.projectam

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.projectam.activities.ConnectActivity
import com.example.projectam.activities.LobbyActivity
import com.example.projectam.utils.Game
import com.example.projectam.utils.Player
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FirebaseManager {
    companion object {
        private lateinit var database: FirebaseDatabase

        fun init() {
            // Get Connection to Firebase database
            database = Firebase.database
        }

        fun createNewLobby(code: String, player: Player) {
            val game = Game(code = code)
            // Add host player
            game.addPlayer(player)
            // Set that client as player 0 (host)
            ClientInfo.id = 0
            // Set lobby at firebase
            database.getReference(code).setValue(game)
        }

        fun connectToLobby(code: String, player: Player, context: Context, connectToLobbyActivity: () -> Unit){
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.getValue<Game>()
                    // Check is there is a lobby with that code
                    if (post == null) {
                        Toast.makeText(context, "There is no lobby with that code", Toast.LENGTH_SHORT).show()
                        return
                    }
                    // Adding player to the lobby
                    post.addPlayer(player)
                    database.getReference(code).setValue(post)
                    Log.d("FIREBASE_MANAGER", "Successfully added player")
                    // Launch lobby activity
                    connectToLobbyActivity()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Fail to connect / check
                    Toast.makeText(context, "Fail to connect", Toast.LENGTH_SHORT).show()
                    Log.w("FIREBASE_MANAGER", "loadPost:onCancelled", error.toException())
                }
            }
            database.getReference(code).addListenerForSingleValueEvent(postListener)
        }

        fun getData(code: String) {

        }

        fun getPlayers(code: String) : MutableList<Player> {
            return mutableListOf()
        }
    }
}