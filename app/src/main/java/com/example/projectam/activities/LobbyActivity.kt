package com.example.projectam.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.ClientInfo
import com.example.projectam.FirebaseManager
import com.example.projectam.R
import com.example.projectam.adapters.LobbyAdapter
import com.example.projectam.utils.Game
import com.example.projectam.utils.Player

class LobbyActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gameCode: TextView
    private lateinit var startButton: Button

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.lobby_activity)
        // Init all widgets
        recyclerView = findViewById(R.id.lobbyView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = LobbyAdapter(this, mutableListOf(Player(ClientInfo.id, ClientInfo.username, true)))
        gameCode = findViewById(R.id.gameCodeTV)
        gameCode.text = "Code: " + ClientInfo.gameCode
        startButton = findViewById(R.id.start)
        if (ClientInfo.id != 0) {
            startButton.visibility = View.INVISIBLE
        }
        // CreateListener
        createListener()
    }
    private fun createListener() {
        FirebaseManager.initLobbyUpdaterListener(updateAdapter, this)
    }

    override fun onResume() {
        super.onResume()
        FirebaseManager.addLobbyUpdater(ClientInfo.gameCode)
    }

    override fun onPause() {
        super.onPause()
        FirebaseManager.deleteLobbyUpdater(ClientInfo.gameCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseManager.deleteUser(ClientInfo.gameCode, ClientInfo.id)
    }

    private val updateAdapter = fun(game: Game) {
        if(game.isStarted) {
            startActivity(Intent(this, GameActivity::class.java))
            return
        }
        recyclerView.adapter = LobbyAdapter(this, game.players)
    }

    fun startGame(view: View) {
        FirebaseManager.startGame(ClientInfo.gameCode, this)
        startActivity(Intent(this, GameActivity::class.java))
    }

    fun leaveLobby(view: View) {
        FirebaseManager.deleteUser(ClientInfo.gameCode, ClientInfo.id)
        startActivity(Intent(this, ConnectActivity::class.java))
    }
}