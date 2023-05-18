package com.example.projectam.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.projectam.FirebaseManager
import com.example.projectam.R

class MainActivity : AppCompatActivity() {
    private lateinit var usernameET: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameET = findViewById(R.id.userNameET)

        FirebaseManager.init()
    }

    fun play(view: View) {
        //ToDo add checks for shit symbols
        if(usernameET.text.length <= 4)
            return
        val intent = Intent(this, ConnectActivity::class.java)
        intent.putExtra("username", usernameET.text)
        startActivity(intent)
    }
}