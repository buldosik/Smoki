package com.example.projectam.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.*
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat.setRotation
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

    //выбор колоды
    private var chosenDeck: Boolean = false
    private var chosenStir1: Boolean = false
    private var chosenStir2: Boolean = false
    //выбор сброса
    private var chosenToStir: Boolean = false
    private var timesClicked = 0
    private var fromPosition = -1
    private var toPosition = -1
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
        updateAdapters(Game(), true)
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
            if (ClientInfo.id == myGame.playerTurn && !ClientInfo.isStarted && !chosenStir1 && !chosenStir2) {
                if (chosenToStir) {
                    Toast.makeText(this, "Pick up stir tile", Toast.LENGTH_SHORT).show()
                } else if (!chosenDeck) {
                    chosenDeck = true;
                    view.setBackgroundResource(R.drawable.image_border)

                    chosenCard = CardManager.getCardFromCardDeck(myGame)
                    println()
                    chosenCard.reveal()
                    deckIV.setImageResource(ImageConverter.getImage(chosenCard))
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
                    chosenCard.setSwap(false)
                    myGame.addCardToStir1(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else if (chosenToStir) {
                    stir1IV.setImageResource(ImageConverter.getImage(chosenCard))
                    stir2IV.setBackgroundResource(0)

                    resetFlags()
                    chosenCard.setSwap(false)
                    myGame.addCardToStir1(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else if (!chosenStir2) {
                    if (myGame.stirDeck1.isEmpty()) {
                        Toast.makeText(this, "Stir has no cards", Toast.LENGTH_SHORT).show()
                    } else {
                    //TODO add rotation to background
                    /*val backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.image_border) // Load the background drawable
                    val rotatedDrawable = RotateDrawable().apply {
                    drawable = backgroundDrawable // Set the background drawable as the inner drawable
                    fromDegrees = 0f // Starting rotation angle
                    toDegrees = 270f // Ending rotation angle (adjust as needed)
                    pivotX = 0.5f // X-axis pivot point (0.5 means center)
                    pivotY = 0.5f // Y-axis pivot point (0.5 means center)
                    }

                    val layerDrawable = LayerDrawable(arrayOf(rotatedDrawable))
                    layerDrawable.getDrawable(0).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)

                    view.background = layerDrawable*/

                        chosenCard = CardManager.getCardFromStir1(myGame)
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
                    chosenCard.setSwap(false)
                    myGame.addCardToStir2(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else if (chosenToStir) {
                    stir2IV.setImageResource(ImageConverter.getImage(chosenCard))
                    stir1IV.setBackgroundResource(0)
                    resetFlags()
                    chosenCard.setSwap(false)
                    myGame.addCardToStir2(chosenCard)
                    myGame.changePlayerTurn()
                    FirebaseManager.sendGameToServer(ClientInfo.gameCode, myGame)
                } else if (!chosenStir1) {
                    if (myGame.stirDeck2.isEmpty()) {
                        Toast.makeText(this, "Stir has no cards", Toast.LENGTH_SHORT).show()
                    } else {

                        //TODO add rotation to background
                        /*val backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.image_border) // Load the background drawable
                        val rotatedDrawable = RotateDrawable().apply {
                        drawable = backgroundDrawable // Set the background drawable as the inner drawable
                        fromDegrees = 0f // Starting rotation angle
                        toDegrees = 270f // Ending rotation angle (adjust as needed)
                        pivotX = 0.5f // X-axis pivot point (0.5 means center)
                        pivotY = 0.5f // Y-axis pivot point (0.5 means center)
                        }

                        val layerDrawable = LayerDrawable(arrayOf(rotatedDrawable))
                        layerDrawable.getDrawable(0).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)

                        view.background = layerDrawable*/
                        chosenCard = CardManager.getCardFromStir2(myGame)
                        chosenStir2 = true

                        if (myGame.stirDeck2.isNotEmpty()) {
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
    }

    private val updateAdapters = @SuppressLint("SetTextI18n")
    fun(game: Game, isInit: Boolean) {
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
        // make disconnected guys invisible
        for (i in 0 until 5) {
            if (!playersId.contains(i)) {
                names[i].text = "Empty"
                views[i].visibility = View.INVISIBLE
            }
        }
        myGame = game
        if(!isInit && ClientInfo.isStarted) {
            stir1IV.setImageResource(ImageConverter.getImage(game.stirDeck1[game.stirDeck1.size - 1]))
            stir2IV.setImageResource(ImageConverter.getImage(game.stirDeck2[game.stirDeck1.size - 1]))
        }
    }
    override fun onItemClick(position: Int) {
        timesClicked++
        Handler().postDelayed({
            if(timesClicked == 1){
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
                        views[player.id].adapter = GameAdapter(this, player.fields, this)
                    }
                    break
                }
            }
            else if(timesClicked == 2){
                System.out.println("two clicks")
                for (player in myGame.players) {
                    if (player.id != ClientInfo.id || player.fields[position].isSwapped)
                        continue

                    if(fromPosition!=-1){
                        toPosition = position
                        System.out.println("taken 2 pos " + toPosition)
                        //mark nine to make further swaps impossible
                        player.fields[fromPosition].setSwap(true)
                        myGame.swapNine(fromPosition, toPosition, player.id)
                        views[player.id].adapter = GameAdapter(this, player.fields, this)
                        Toast.makeText(this, "Swap done", Toast.LENGTH_SHORT).show()
                        fromPosition = -1
                        toPosition = -1
                    }else if(player.fields[position].value == 9){
                        System.out.println("taken 1 pos " + fromPosition)
                        fromPosition = position
                    }
                    break
                }
            }
            timesClicked = 0
            print(timesClicked)
        }, 500L)
    }
}