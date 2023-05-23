package com.example.projectam.activities

import android.annotation.SuppressLint
import android.graphics.drawable.*
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
import com.example.projectam.adapters.GameAdapter
import com.example.projectam.utils.*

class GameActivity : AppCompatActivity(), OnItemListener {
    private var views: MutableList<RecyclerView> = mutableListOf()
    private var names: MutableList<TextView> = mutableListOf()

    private var chosenDeck: Boolean = false
    private var chosenStir1: Boolean = false
    private var chosenStir2: Boolean = false
    private var chosenToStir: Boolean = false

    private lateinit var chosenCard: Card
    lateinit var myGame: Game

    private lateinit var deckIV: ImageView
    private lateinit var stir1IV: ImageView
    private lateinit var stir2IV: ImageView
    private lateinit var hintCardIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val view = layoutInflater.inflate(R.layout.game_activity, null)

        setContentView(view)
        // Added sample zoom
        // ToDo do it in some nice way
        view.setOnClickListener(object : View.OnClickListener {
            var zoomFactor = 2f
            var zoomedOut = false
            override fun onClick(v: View) {
                if (zoomedOut) {
                    v.scaleX = 1f
                    v.scaleY = 1f
                    zoomedOut = false
                } else {
                    v.scaleX = zoomFactor
                    v.scaleY = zoomFactor
                    zoomedOut = true
                }
            }
        })
        initViews()

        // FOR LOCAL TESTS
//        game = Game()
//        game.addPlayer(Player(username = "Maxon", isConnected = true))
//        game.isStarted = true
//        game.createNewDeck()
//        game.stirDeck1.add(CardManager.getCardFromCardDeck(game, true))
//        game.stirDeck2.add(CardManager.getCardFromCardDeck(game, true))
//        for(j in 1..6) {
//            for(i in game.players) {
//                i.fields.add(CardManager.getCardFromCardDeck(game))
//            }
//        }
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
        hintCardIV = findViewById(R.id.hintCard)

        deckIV.setOnClickListener { view ->
            if (ClientInfo.id == myGame.playerTurn && !ClientInfo.isStarted && !chosenStir1 && !chosenStir2) {
                if (chosenToStir) {
                    Toast.makeText(this, "Chose stir for drop card", Toast.LENGTH_SHORT).show()
                } else if (!chosenDeck) {
                    chosenDeck = true;
                    view.setBackgroundResource(R.drawable.image_border)
                    chosenCard = CardManager.getCardFromCardDeck(myGame)
                    chosenCard.reveal()
                    hintCardIV.setImageResource(ImageConverter.getImage(chosenCard))
                }
            }
        }

        stir1IV.setOnClickListener { view ->
            if (ClientInfo.id == myGame.playerTurn && !ClientInfo.isStarted) {
                if (chosenDeck) {
                    stir1IV.setImageResource(ImageConverter.getImage(chosenCard))
                    deckIV.setImageResource(ImageConverter.getImage(myGame.cardDeck[myGame.cardDeck.size - 1]))
                    deckIV.setBackgroundResource(0)

                    resetFlags()
                    myGame.addToStir1(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else if (chosenToStir) {
                    stir1IV.setImageResource(ImageConverter.getImage(chosenCard))
                    stir1IV.setBackgroundResource(0)
                    stir2IV.setBackgroundResource(0)

                    resetFlags()
                    myGame.addToStir1(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else if (!chosenStir2) {
                    if (myGame.stirDeck1.isEmpty()) {
                        Toast.makeText(this, "Stir has no cards", Toast.LENGTH_SHORT).show()
                    } else {
                        view.setBackgroundResource(R.drawable.image_border)

                        chosenCard = CardManager.getCardFromStir1(myGame)
                        hintCardIV.setImageResource(ImageConverter.getImage(chosenCard))
                        chosenStir1 = true

                        if (myGame.stirDeck1.isNotEmpty()){
                            stir1IV.setImageResource(ImageConverter.getImage(myGame.stirDeck1[myGame.stirDeck1.size - 1]))
                        } else {
                            stir1IV.setImageResource(R.drawable.close_image_vert)
                        }
                    }
                }
            }
        }

        stir2IV.setOnClickListener { view ->
            if (ClientInfo.id == myGame.playerTurn && !ClientInfo.isStarted) {
                if (chosenDeck) {
                    stir2IV.setImageResource(ImageConverter.getImage(chosenCard))
                    deckIV.setImageResource(ImageConverter.getImage(myGame.cardDeck[myGame.cardDeck.size - 1]))
                    deckIV.setBackgroundResource(0)

                    resetFlags()
                    myGame.addToStir2(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else if (chosenToStir) {
                    stir2IV.setImageResource(ImageConverter.getImage(chosenCard))
                    stir1IV.setBackgroundResource(0)
                    stir2IV.setBackgroundResource(0)

                    resetFlags()
                    myGame.addToStir2(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else if (!chosenStir1) {
                    if (myGame.stirDeck2.isEmpty()) {
                        Toast.makeText(this, "Stir has no cards", Toast.LENGTH_SHORT).show()
                    } else {
                        view.setBackgroundResource(R.drawable.image_border)
                        chosenCard = CardManager.getCardFromStir2(myGame)
                        hintCardIV.setImageResource(ImageConverter.getImage(chosenCard))
                        chosenStir2 = true

                        if (myGame.stirDeck2.isNotEmpty()){
                            stir2IV.setImageResource(ImageConverter.getImage(myGame.stirDeck2[myGame.stirDeck2.size - 1]))
                        } else {
                            stir2IV.setImageResource(R.drawable.close_image_vert)
                        }
                    }
                }
            }
        }
    }

    private fun resetFlags() {
        chosenDeck = false
        chosenStir1 = false
        chosenStir2 = false
        chosenToStir = false
        hintCardIV.setImageResource(R.drawable.close_image_vert)
    }

    private val updateAdapters = @SuppressLint("SetTextI18n")
    fun(game: Game) {
        val playersId: ArrayList<Int> = arrayListOf()

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

        if(game.stirDeck1.isNotEmpty()) {
            stir1IV.setImageResource(ImageConverter.getImage(game.stirDeck1[game.stirDeck1.size - 1]))
        }
        else
            stir1IV.setImageResource(ImageConverter.getImage(Card(0,false)))
        if(game.stirDeck2.isNotEmpty()) {
            stir2IV.setImageResource(ImageConverter.getImage(game.stirDeck2[game.stirDeck2.size - 1]))
        }
        else
            stir2IV.setImageResource(ImageConverter.getImage(Card(0,false)))
    }
    override fun onItemClick(position: Int) {
        for (player in myGame.players) {
            if (player.id != ClientInfo.id)
                continue
            if (ClientInfo.isStarted) {
                player.fields[position].reveal()
                FirebaseManager.sendPlayerToServer(ClientInfo.gameCode, player)
                ClientInfo.isStarted = false
//                views[player.id].adapter = GameAdapter(this, player.fields, this)
            } else if (chosenDeck || chosenStir1 || chosenStir2) {
                chosenToStir = true
                player.fields[position] =
                    chosenCard.also { chosenCard = player.fields[position] }
                chosenCard.reveal()
                hintCardIV.setImageResource(ImageConverter.getImage(chosenCard))

                views[player.id].adapter = GameAdapter(this, player.fields, this)
            }
            break
        }
    }
}