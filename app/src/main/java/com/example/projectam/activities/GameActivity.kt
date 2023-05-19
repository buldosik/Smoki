package com.example.projectam.activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.ClientInfo
import com.example.projectam.FirebaseManager
import com.example.projectam.R
import com.example.projectam.utils.Game

class GameActivity : AppCompatActivity() {
    private var views: MutableList<RecyclerView> = mutableListOf()

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
    }

    private fun createListener() {
        // ToDo createGameUpdater
    }

    override fun onResume() {
        super.onResume()
        // ToDo addGameUpdater
    }

    override fun onPause() {
        super.onPause()
        // ToDo deleteGameUpdater
    }

    private fun initViews() {
        views.add(findViewById(R.id.player1))
        views.add(findViewById(R.id.player2))
        views.add(findViewById(R.id.player3))
        views.add(findViewById(R.id.player4))
        views.add(findViewById(R.id.player5))

        deckIV = findViewById(R.id.deck)
        stir1IV = findViewById(R.id.stir1)
        stir2IV = findViewById(R.id.stir2)
    }

    private val updateAdapters = fun(game: Game) {
        TODO("Not yet implemented")
    }
}