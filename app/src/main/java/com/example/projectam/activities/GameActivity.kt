package com.example.projectam.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.ClientInfo
import com.example.projectam.FirebaseManager
import com.example.projectam.R
import com.example.projectam.utils.Card
import com.example.projectam.utils.Game
import com.example.projectam.utils.GameAdapter
import com.example.projectam.utils.OnItemListener

class GameActivity : AppCompatActivity(), OnItemListener {
    private var views: MutableList<RecyclerView> = mutableListOf()
    private var names: MutableList<TextView> = mutableListOf()

    private var chosenDeck: Boolean = false
    private var chosenToStir: Boolean = false

    private lateinit var chosenCard: Card
    lateinit var myGame: Game

    private lateinit var deckIV: ImageView
    private lateinit var stir1IV: ImageView
    private lateinit var stir2IV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.game_activity)

        initViews()
        updateAdapters(Game())
        createListener()
    }

    private fun createListener() {
        FirebaseManager.initGameUpdaterListener(ClientInfo.gameCode, updateAdapters, this)
    }

    override fun onResume() {
        super.onResume()
        FirebaseManager.addGameUpdater(ClientInfo.gameCode)
    }

    override fun onPause() {
        super.onPause()
        FirebaseManager.deleteGameUpdater(ClientInfo.gameCode)
    }

    private fun initViews() {
        views.add(findViewById(R.id.player1))
        views.add(findViewById(R.id.player2))
        views.add(findViewById(R.id.player3))
        views.add(findViewById(R.id.player4))
        views.add(findViewById(R.id.player5))

        names.add(findViewById(R.id.namePlayer1))
        names.add(findViewById(R.id.namePlayer2))
        names.add(findViewById(R.id.namePlayer3))
        names.add(findViewById(R.id.namePlayer4))
        names.add(findViewById(R.id.namePlayer5))

        for (view: RecyclerView in views) {
            view.layoutManager = GridLayoutManager(this, 3)
        }

        deckIV = findViewById(R.id.deck)
        stir1IV = findViewById(R.id.stir1)
        stir2IV = findViewById(R.id.stir2)

        deckIV.setOnClickListener { view ->
            if (ClientInfo.id == myGame.playerTurn) {
                if (chosenToStir) {
                    Toast.makeText(this, "Chose stir for drop card", Toast.LENGTH_SHORT).show()
                } else if (!chosenDeck) {
                    chosenDeck = true;
                    view.setBackgroundResource(R.drawable.image_border)
                    chosenCard = myGame.getCardFromCardDeck()
                    deckIV.setImageResource(chosenCard.getImage())
                }
            }
        }

        stir1IV.setOnClickListener { view ->
            if (ClientInfo.id == myGame.playerTurn) {
                if (chosenDeck) {
                    stir1IV.setImageResource(chosenCard.getImage())
                    deckIV.setImageResource(R.drawable.close_image_vert)
                    deckIV.setBackgroundResource(R.drawable.image_disable_border)

                    chosenDeck = false
                    myGame.addToStir1(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else if (chosenToStir) {
                    chosenToStir = false
                    stir1IV.setImageResource(chosenCard.getImage())

                    myGame.addToStir1(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else {
                    if (myGame.stirDeck1.isEmpty()) {
                        Toast.makeText(this, "Stir has no cards", Toast.LENGTH_SHORT).show()
                    } else {
                        view.setBackgroundResource(R.drawable.image_border)
                        chosenCard = myGame.getCardFromStir1()

                        if (myGame.stirDeck1.isNotEmpty()){
                            stir1IV.setImageResource(myGame.stirDeck1[myGame.stirDeck1.size - 1].getImage())
                        } else {
                            stir1IV.setImageResource(R.drawable.close_image)
                        }
                    }
                }
            }
        }

        stir2IV.setOnClickListener { view ->
            if (ClientInfo.id == myGame.playerTurn) {
                if (chosenDeck) {
                    stir2IV.setImageResource(chosenCard.getImage())
                    deckIV.setImageResource(R.drawable.close_image_vert)
                    deckIV.setBackgroundResource(R.drawable.image_disable_border)

                    chosenDeck = false
                    myGame.addToStir2(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else if (chosenToStir) {
                    chosenToStir = false
                    stir2IV.setImageResource(chosenCard.getImage())

                    myGame.addToStir2(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else {
                    if (myGame.stirDeck2.isEmpty()) {
                        Toast.makeText(this, "Stir has no cards", Toast.LENGTH_SHORT).show()
                    } else {
                        view.setBackgroundResource(R.drawable.image_border)
                        chosenCard = myGame.getCardFromStir2()

                        if (myGame.stirDeck2.isNotEmpty()){
                            stir2IV.setImageResource(myGame.stirDeck2[myGame.stirDeck2.size - 1].getImage())
                        } else {
                            stir2IV.setImageResource(R.drawable.close_image)
                        }
                    }
                }
            }
        }
    }

    private val updateAdapters = @SuppressLint("SetTextI18n")
    fun(game: Game) {
        var playersId: ArrayList<Int> = arrayListOf()

        for (i in 0 until 5) {
            views[i].visibility = View.VISIBLE
        }
        for (player in game.players) {
            playersId.add(player.id)
            views[player.id].adapter = GameAdapter(this, player.fields, null)
            names[player.id].text = player.username
            if (player.id == ClientInfo.id) {
                views[ClientInfo.id].adapter = GameAdapter(this, player.fields, this)
            }
        }


        for (i in 0 until 5) {
            if (!playersId.contains(i)) {
                names[i].text = "Empty"
                views[i].visibility = View.INVISIBLE
            }
        }
        myGame = game
    }
    override fun onItemClick(position: Int) {
        for (player in myGame.players) {
            if (player.id != ClientInfo.id)
                continue
            if (ClientInfo.isStarted) {
                player.fields[position].reveal()
                FirebaseManager.sendPlayerToServer(ClientInfo.gameCode, player)
                ClientInfo.isStarted = false
            } else {
                chosenToStir = true
                player.fields[position] =
                    chosenCard.also { chosenCard = player.fields[position] }
                views[player.id].adapter = GameAdapter(this, player.fields, this)
            }
            break
        }
    }
}