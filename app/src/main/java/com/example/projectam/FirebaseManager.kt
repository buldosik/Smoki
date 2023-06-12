package com.example.projectam

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.projectam.utils.GameManager
import com.example.projectam.utils.Game
import com.example.projectam.utils.Player
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class FirebaseManager {
    companion object {
        private lateinit var database: FirebaseDatabase

        fun init() {
            // Get Connection to Firebase database
            database = Firebase.database
        }
        // region Connect/Create Activity
        fun attemptToCreateNewLobby(context: Context, code: String, player: Player, connectToLobbyActivity: () -> Unit) {
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val game = snapshot.getValue<Game>()
                    // Check is there is a lobby with that code
                    if (game == null) {
                        createLobby(code, player)
                        Log.d("FIREBASE_MANAGER", "Lobby created")
                        connectToLobbyActivity()
                        return
                    }
                    // ToDo probably that need a fix

                    if(!game.isCalculatedScores) {
                        if(game.isStarted) {
                            Toast.makeText(context, "That lobby is active", Toast.LENGTH_SHORT).show()
                            return
                        }
                        else if(game.players.size > 0) {
                            Toast.makeText(context, "That lobby is created and waiting for players", Toast.LENGTH_SHORT).show()
                            return
                        }
                    }
                    createLobby(code, player)
                    Log.d("FIREBASE_MANAGER", "Lobby created")
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
        fun createLobby(code: String, player: Player) {
            val game = Game(code = code)
            // Add playerSlots
            GameManager.clearPlayerList(game)
            // Add host player
            GameManager.addPlayer(game, player)
            // Set lobby at firebase
            database.getReference(code).setValue(game)
        }

        fun connectToLobby(code: String, player: Player, context: Context, connectToLobbyActivity: () -> Unit){
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val game = snapshot.getValue<Game>()
                    // Check is there is a lobby with that code
                    if (game == null) {
                        Toast.makeText(context, "There is no lobby with that code", Toast.LENGTH_SHORT).show()
                        return
                    }
                    // Adding player to the lobby
                    if(!game.isStarted) {
                        GameManager.addPlayer(game, player)
                        database.getReference(code).setValue(game)
                        Log.d("FIREBASE_MANAGER", "Successfully added player")
                    }
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
        fun initLobbyUpdaterListener(updateAdapter: (game: Game) -> Unit, context: Context) {
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
                    updateAdapter(post)
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
        fun initGameUpdaterListener(updateAdapter: (game: Game) -> Unit, context: Context) {
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
        fun setPlayer(code: String, player: Player, id: Int) {
            Log.d("FIREBASE_MANAGER", "Setting player $id")
            database.getReference("$code/players/$id").setValue(player)
        }
        fun deleteUser(code: String, id: Int) {
            Log.d("FIREBASE_MANAGER", "Deleting player $id")
            if(ClientInfo.id != -5) {
                database.getReference("$code/players/$id").removeValue()
            }
            attemptToDestroyLobby(code)
        }
        private fun attemptToDestroyLobby(code: String, isIgnoringRules: Boolean = false) {
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val players = snapshot.getValue<MutableList<Player>>()
                    // Check for at least one player
                    if(players == null || players.isEmpty() || isIgnoringRules) {
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
                    game.isStarted = true
                    GameManager.createNewDeck(game)
                    game.stirDeck1.add(GameManager.getCardFromCardDeck(game, true))
                    game.stirDeck2.add(GameManager.getCardFromCardDeck(game, true))
                    for(j in 1..6) {
                        for(i in game.players) {
                            if(i == null)
                                continue
                            i.fields.add(GameManager.getCardFromCardDeck(game))
                        }
                    }
                    // ToDo refactor/redo random player start
                    var isPlayer = false
                    while (!isPlayer) {
                        game.playerTurn = Random.nextInt(5)
                        isPlayer = game.players.any { it?.id == game.playerTurn }
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

        fun sendPlayerToServer(code: String, player: Player) {
            // Set game at firebase
            database.getReference("$code/players/${player.id}").setValue(player)
        }

        fun sendGameToServer(code: String, game: Game) {
            // Set game at firebase
            database.getReference(code).setValue(game)
        }
        // endregion
    }
}