package com.example.projectam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.projectam.R

class ConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connect_activity)
    }

    fun connectToLobby(view: View) {
        startActivity(Intent(this, LobbyActivity::class.java))
    }

    fun createLobby(view: View) {
        startActivity(Intent(this, LobbyActivity::class.java))
    }
}