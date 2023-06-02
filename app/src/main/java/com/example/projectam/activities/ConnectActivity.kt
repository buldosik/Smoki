package com.example.projectam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectam.ClientInfo
import com.example.projectam.FirebaseManager
import com.example.projectam.R
import com.example.projectam.banWordList
import com.example.projectam.utils.Player

class ConnectActivity : AppCompatActivity() {
    private lateinit var romeCodeET: EditText

    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.connect_activity)
        username = ClientInfo.username
        romeCodeET = findViewById(R.id.roomCodeET)
        ClientInfo.init()
    }

    fun attemptToConnectToLobby(view: View) {
        if(!isValidCode()) {
            Toast.makeText(this, "Code contains specific characters", Toast.LENGTH_SHORT).show()
            return
        }

        // Attempt to add new player to lobby
        FirebaseManager.connectToLobby(romeCodeET.text.toString(), Player(username = username, isConnected = true), this, connectToLobbyActivity)
    }

    fun createLobby(view: View) {
        if(!isValidCode()) {
            Toast.makeText(this, "Code contains specific characters", Toast.LENGTH_SHORT).show()
            return
        }

        // Call to create lobby at firebase
        FirebaseManager.attemptToCreateNewLobby(this, romeCodeET.text.toString(), Player(username = username, isConnected = true), connectToLobbyActivity)
    }

    private val connectToLobbyActivity = fun() {
        ClientInfo.gameCode = romeCodeET.text.toString()
        startActivity(Intent(this, LobbyActivity::class.java))
    }

    private fun isValidCode() : Boolean {
        val code = romeCodeET.text
        return code.all{ it.isLetter() || it.isDigit() } && code.length in 2..10 && banWordList.isValidName(code.toString())
    }
}