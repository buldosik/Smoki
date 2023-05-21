package com.example.projectam.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.ClientInfo
import com.example.projectam.FirebaseManager
import com.example.projectam.R
import com.example.projectam.utils.Game
import com.example.projectam.utils.GameAdapter
import com.example.projectam.utils.OnItemListener

class GameActivity : AppCompatActivity(), OnItemListener {
    private var views: MutableList<RecyclerView> = mutableListOf()
    private var names: MutableList<TextView> = mutableListOf()

    private var chosenDeck: Boolean = false
    private var chosenStir1: Boolean = false
    private var chosenStir2: Boolean = false

    private var chosenCard: Int = -1

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
//        createListener()
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
            view.setBackgroundResource(R.drawable.image_border)
        }

        stir1IV.setOnClickListener { view ->
            view.setBackgroundResource(R.drawable.image_border)
        }

        stir2IV.setOnClickListener { view ->
            view.setBackgroundResource(R.drawable.image_border)
        }
    }

    private val updateAdapters = @SuppressLint("SetTextI18n")
    fun(game: Game) {
        for (i in 0 until 5) {
            views[i].visibility = View.VISIBLE
        }
        for (i in 0 until game.players.size) {
            views[i].adapter = GameAdapter(this, game.players[i].fields, this)
            names[i].text = game.players[i].username
        }
        for (i in game.players.size until 5) {
            names[i].text = "Empty"
            views[i].visibility = View.INVISIBLE
        }
        ClientInfo.game = game
    }

    override fun onItemClick(position: Int) {
        for (player in ClientInfo.game.players) {
            if (player.id == ClientInfo.id) {
                player.fields[position].value = chosenCard
            }
        }
    }
}