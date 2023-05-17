package com.example.projectam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projectam.R

class ConnectActivity : AppCompatActivity() {
    private lateinit var romeCodeET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connect_activity)

        romeCodeET = findViewById(R.id.roomCodeET)
    }

    fun connectToLobby(view: View) {
        startActivity(Intent(this, LobbyActivity::class.java))
    }

    fun createLobby(view: View) {
        startActivity(Intent(this, LobbyActivity::class.java))
    }
}