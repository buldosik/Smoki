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
import com.example.projectam.adapters.ResultAdapter
import com.example.projectam.utils.Player

class ResultActivity  : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.result_activity)
        // Init all widgets
        recyclerView = findViewById(R.id.lobbyView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = ResultAdapter(this, ClientInfo.game.players)
    }
    fun leaveLobby(view: View) {
        startActivity(Intent(this, ConnectActivity::class.java))
    }
}