package com.example.projectam

import android.content.Context
import android.util.Log
import android.widget.Toast
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
        // region Connect/Create Activity
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
        // endregion

        // region Lobby Activity
        lateinit var postListenerLobby: ValueEventListener
        fun initLobbyUpdaterListener(code: String, updateAdapter: (players: MutableList<Player>) -> Unit, context: Context) {
            postListenerLobby = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.getValue<Game>()
                    // Check is there is a lobby with that code
                    if (post == null) {
                        Toast.makeText(
                            context,
                            "There is no lobby with that code",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    Log.d("FIREBASE_MANAGER", "Call updateAdapter")
                    // Call updateAdapter
                    updateAdapter(post.players)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Fail to connect / check
                    Toast.makeText(context, "Fail to connect", Toast.LENGTH_SHORT).show()
                    Log.w("FIREBASE_MANAGER", "loadPost:onCancelled", error.toException())
                }
            }
        }
        fun addLobbyUpdater(code: String) {
            database.getReference(code).addValueEventListener(postListenerLobby)
        }
        fun deleteLobbyUpdater(code: String) {
            database.getReference(code).removeEventListener(postListenerLobby)
        }
        // endregion

        // region Game Activity
        lateinit var postListenerGame: ValueEventListener
        fun initGameUpdaterListener(code: String, updateAdapter: (game: Game) -> Unit, context: Context) {
            postListenerGame = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.getValue<Game>()
                    // Check is there is a lobby with that code
                    if (post == null) {
                        Toast.makeText(
                            context,
                            "There is no lobby with that code",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    Log.d("FIREBASE_MANAGER", "Call updateAdapter")
                    // Call updateAdapter
                    updateAdapter(post)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Fail to connect / check
                    Toast.makeText(context, "Fail to connect", Toast.LENGTH_SHORT).show()
                    Log.w("FIREBASE_MANAGER", "loadPost:onCancelled", error.toException())
                }
            }
        }
        fun addGameUpdater(code: String) {
            database.getReference(code).addValueEventListener(postListenerGame)
        }
        fun deleteGameUpdater(code: String) {
            database.getReference(code).removeEventListener(postListenerGame)
        }
        // endregion

        // region Additional functions
        fun deleteUser(code: String, id: Int) {
            database.getReference("$code/players/$id").removeValue()
            //database.getReference(code).child("players").child(id.toString()).removeValue()
            tryToDestroyLobby(code)
        }
        private fun tryToDestroyLobby(code: String) {
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val players = snapshot.getValue<MutableList<Player>>()
                    // Check for at least one player
                    if(players == null || players.isEmpty()) {
                        Log.d("FIREBASE_MANAGER", "Successfully delete lobby")
                        database.getReference(code).removeValue()
                    }
                    else {
                        Log.d("FIREBASE_MANAGER", "At least one player in")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Fail to connect / check
                    Log.d("FIREBASE_MANAGER", "Deleting is cancelled")
                    Log.w("FIREBASE_MANAGER", "loadPost:onCancelled", error.toException())
                }
            }
            database.getReference("$code/players").addListenerForSingleValueEvent(postListener)
        }

        fun startGame(code: String, context: Context) {
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val game = snapshot.getValue<Game>()
                    // Check is there is a lobby with that code
                    if (game == null) {
                        Toast.makeText(context, "There is no lobby with that code", Toast.LENGTH_SHORT).show()
                        return
                    }
                    //  Game start
                    game.createNewDeck()
                    game.stirDeck1.add(game.getCardFromCardDeck(true))
                    game.stirDeck2.add(game.getCardFromCardDeck(true))
                    for(j in 1..6) {
                        for(i in game.players) {
                            i.fields.add(game.getCardFromCardDeck())
                        }
                    }
                    database.getReference(code).setValue(game)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Fail to connect / check
                    Toast.makeText(context, "Fail to connect", Toast.LENGTH_SHORT).show()
                    Log.w("FIREBASE_MANAGER", "loadPost:onCancelled", error.toException())
                }
            }
            database.getReference(code).addListenerForSingleValueEvent(postListener)
        }
        // endregion
    }
}