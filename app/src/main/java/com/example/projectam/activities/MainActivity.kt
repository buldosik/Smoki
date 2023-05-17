package com.example.projectam.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.projectam.utils.Game
import com.example.projectam.R
import com.example.projectam.utils.Player

class MainActivity : AppCompatActivity() {
    private lateinit var userNameET: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userNameET = findViewById(R.id.userNameET)
    }

    fun play(view: View) {
        val player = Player(userNameET.text.toString())
        Game.currentPlayer = player
        Game.players.add(player)

        startActivity(Intent(this, ConnectActivity::class.java))
    }
}