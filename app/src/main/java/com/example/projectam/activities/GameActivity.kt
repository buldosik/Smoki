package com.example.projectam.activities

import android.os.Bundle
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
import com.example.projectam.utils.Player

class GameActivity : AppCompatActivity() {
    private var views: MutableList<RecyclerView> = mutableListOf()
    private var names: MutableList<TextView> = mutableListOf()

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

        for (view: RecyclerView in views) {
            view.layoutManager = GridLayoutManager(this, 3)
        }

        deckIV = findViewById(R.id.deck)
        stir1IV = findViewById(R.id.stir1)
        stir2IV = findViewById(R.id.stir2)
    }

    private val updateAdapters = fun(game: Game) {
        for (i in 0 until game.players.size) {
            views[i].adapter = GameAdapter(this, game.players[i].fields)
        }
    }
}