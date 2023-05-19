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

    //ToDo add checks for shit symbols
    private fun isValidName(): Boolean {
        if(usernameET.text.length <= 4)
            return false
        return true
    }
}