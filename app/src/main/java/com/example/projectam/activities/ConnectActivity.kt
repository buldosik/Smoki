package com.example.projectam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projectam.FirebaseManager
import com.example.projectam.R
import com.example.projectam.utils.Player

class ConnectActivity : AppCompatActivity() {
    private lateinit var romeCodeET: EditText

    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connect_activity)

        username = intent.getStringExtra("username").toString()
        romeCodeET = findViewById(R.id.roomCodeET)
    }

    fun connectToLobby(view: View) {
        //ToDo add checks for shit symbols
        if(romeCodeET.text.length <= 3)
            return
        startActivity(Intent(this, LobbyActivity::class.java))
    }

    fun createLobby(view: View) {
        //ToDo add checks for shit symbols
        if(romeCodeET.text.length <= 3)
            return

        // Call creating of game at firebase
        FirebaseManager.createNewLobby(romeCodeET.text.toString(), username)

        // Sending to next activity user id
        val intent = Intent(this, ConnectActivity::class.java)
        intent.putExtra("host_id", "0")
        startActivity(intent)
    }
}