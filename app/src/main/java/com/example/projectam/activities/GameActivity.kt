package com.example.projectam.activities

import android.annotation.SuppressLint
import android.graphics.drawable.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.ClientInfo
import com.example.projectam.FirebaseManager
import com.example.projectam.R
import com.example.projectam.adapters.GameAdapter
import com.example.projectam.states.ChoosingDeck
import com.example.projectam.states.GameStateContext
import com.example.projectam.states.RevealFirstCard
import com.example.projectam.utils.*

class GameActivity : AppCompatActivity() {
    private var views: MutableList<RecyclerView> = mutableListOf()
    private var names: MutableList<TextView> = mutableListOf()

    private lateinit var deckIV: ImageView
    private lateinit var stir1IV: ImageView
    private lateinit var stir2IV: ImageView

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var currentState: GameStateContext
        @SuppressLint("StaticFieldLeak")
        lateinit var hintCardIV: ImageView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val view = layoutInflater.inflate(R.layout.game_activity, null)
        setContentView(view)
        // Added sample zoom
        // ToDo do it in some nice way
        /*view.setOnClickListener(object : View.OnClickListener {
            var zoomFactor = 2f
            var zoomedOut = false
            override fun onClick(v: View) {
                if (zoomedOut) {
                    v.scaleX = 1f
                    v.scaleY = 1f
                    zoomedOut = false
                } else {
                    v.scaleX = zoomFactor
                    v.scaleY = zoomFactor
                    zoomedOut = true
                }
            }
        })*/

        initViews()
        currentState = GameStateContext(updatePlayer, deckIV, stir1IV, stir2IV, hintCardIV, this)
        //updateAdapters(Game())
        createListener()
    }
    private val updatePlayer = fun() {
        for (player in ClientInfo.game.players) {
            if (player.id == ClientInfo.id) {
                views[ClientInfo.id].adapter = GameAdapter(this, player.fields, currentState)
            }
        }
    }

    private fun createListener() {
        FirebaseManager.initGameUpdaterListener(ClientInfo.gameCode, updateAdapters, this)
    }
    override fun onResume() {
        super.onResume()
        FirebaseManager.addGameUpdater(ClientInfo.gameCode)
    }
    override fun onPause() {
        super.onPause()
        FirebaseManager.deleteGameUpdater(ClientInfo.gameCode)
    }

    private fun initViews() {
        views.add(findViewById(R.id.player1))
        views.add(findViewById(R.id.player2))
        views.add(findViewById(R.id.player3))
        views.add(findViewById(R.id.player4))
        views.add(findViewById(R.id.player5))

        names.add(findViewById(R.id.namePlayer1))
        names.add(findViewById(R.id.namePlayer2))
        names.add(findViewById(R.id.namePlayer3))
        names.add(findViewById(R.id.namePlayer4))
        names.add(findViewById(R.id.namePlayer5))

        for (view: RecyclerView in views) {
            view.layoutManager = GridLayoutManager(this, 3)
        }

        deckIV = findViewById(R.id.deck)
        stir1IV = findViewById(R.id.stir1)
        stir2IV = findViewById(R.id.stir2)
        hintCardIV = findViewById(R.id.hintCard)
    }


    private val updateAdapters = @SuppressLint("SetTextI18n")
    fun(game: Game) {
        if(game.isFinished) {
            // ToDo show winner
            return
        }

        val playersId: ArrayList<Int> = arrayListOf()

        for (i in 0 until 5) {
            views[i].visibility = View.VISIBLE
        }

        if(game.playerTurn == ClientInfo.id){
            currentState.setState(ChoosingDeck())
            if(!game.players[game.getCurrentPlayerIndex()].isRevealedAny())
                currentState.setState(RevealFirstCard())
        }

        for (player in game.players) {
            playersId.add(player.id)
            views[player.id].adapter = GameAdapter(this, player.fields, null)
            names[player.id].text = player.username
            if (player.id == ClientInfo.id) {
                views[ClientInfo.id].adapter = GameAdapter(this, player.fields, currentState)
            }
        }
        for (i in 0 until 5) {
            if (!playersId.contains(i)) {
                names[i].text = "Empty"
                views[i].visibility = View.INVISIBLE
            }
        }
        ClientInfo.game = game

        if(game.stirDeck1.isNotEmpty())
            stir1IV.setImageResource(ImageConverter.getImage(game.stirDeck1[game.stirDeck1.size - 1]))
        else
            stir1IV.setImageResource(ImageConverter.getImage(Card(0,false)))
        if(game.stirDeck2.isNotEmpty())
            stir2IV.setImageResource(ImageConverter.getImage(game.stirDeck2[game.stirDeck2.size - 1]))
        else
            stir2IV.setImageResource(ImageConverter.getImage(Card(0,false)))
    }
}