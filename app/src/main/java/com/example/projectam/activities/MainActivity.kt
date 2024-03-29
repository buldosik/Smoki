package com.example.projectam.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import com.example.projectam.ClientInfo
import com.example.projectam.FirebaseManager
import com.example.projectam.R
import com.example.projectam.banWordList

class MainActivity : AppCompatActivity() {
    private lateinit var usernameET: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        usernameET = findViewById(R.id.userNameET)

        FirebaseManager.init()
    }

    fun play(view: View) {
        if(!isValidName())
            return
        ClientInfo.username = usernameET.text.toString()
        startActivity(Intent(this, ConnectActivity::class.java))
    }

    private fun isValidName() : Boolean {
        val name = usernameET.text
        return name.all{ it.isLetter() || it.isDigit() } && name.length in 3..16 && banWordList.isValidName(name.toString())
    }
}