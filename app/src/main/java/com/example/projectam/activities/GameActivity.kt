package com.example.projectam.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.*
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.ClientInfo
import com.example.projectam.FirebaseManager
import com.example.projectam.R
import com.example.projectam.adapters.GameAdapter
import com.example.projectam.states.RevealMirrors
import com.example.projectam.states.ChoosingDeck
import com.example.projectam.states.EndTurn
import com.example.projectam.states.GameStateContext
import com.example.projectam.states.RevealFirstCard
import com.example.projectam.utils.*
import org.w3c.dom.Text
import java.lang.Float.max
import kotlin.math.min

class GameActivity : AppCompatActivity() {
    private var views: MutableList<RecyclerView> = mutableListOf()
    private var names: MutableList<TextView> = mutableListOf()

    private lateinit var deckIV: ImageView
    private lateinit var stir1IV: ImageView
    private lateinit var stir2IV: ImageView

    private  var nicknamesVisibility: Boolean = false

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    private var width: Int = 0
    private var height: Int = 0

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var currentState: GameStateContext
        @SuppressLint("StaticFieldLeak")
        lateinit var hintCardIV: ImageView
    }
    private lateinit var hintCardTV: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // Sample zoom
        val view = layoutInflater.inflate(R.layout.game_activity, null)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener(view))
        setContentView(view)
        width = view.width
        height = view.height

        initViews()
        currentState = GameStateContext(updatePlayer, deckIV, stir1IV, stir2IV, hintCardIV, this)
        createListener()
    }
    // region zoom
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
    }
    inner class ScaleListener(private val view: View) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {

            scaleFactor *= detector.scaleFactor
            scaleFactor = max(0.5f, min(scaleFactor,4.0f))

            view.pivotX = detector.focusX
            view.pivotY = detector.focusY

            view.scaleX = scaleFactor
            view.scaleY = scaleFactor

            return true
        }
    }
    // endregion zoom

    // region firebase
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
    // endregion firebase

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
        hintCardTV = findViewById(R.id.handeCardTV)
    }

    private val updatePlayer = fun() {
        for (player in ClientInfo.game.players) {
            if (player.id == ClientInfo.id) {
                views[ClientInfo.id].adapter = GameAdapter(this, player.fields, currentState)
            }
        }
    }

    private val updateAdapters = @SuppressLint("SetTextI18n")
    fun(game: Game) {
        ClientInfo.game = game
        if(game.isCalculatedScores) {
            startActivity(Intent(this, ResultActivity::class.java))
            return
        }

        val playersId: ArrayList<Int> = arrayListOf()

        if(game.playerTurn == ClientInfo.id){
            currentState.setState(ChoosingDeck())
            if(!game.players[game.getCurrentPlayerIndex()].isRevealedAny())
                currentState.setState(RevealFirstCard())
            if(game.isFinished) {
                Log.d("GameActivity", "State - reveal mirrors")
                currentState.setState(RevealMirrors())
                if(game.players[game.getCurrentPlayerIndex()].fields.all {card ->
                        card.value != -1
                    }) {
                    for(i in game.players[game.getCurrentPlayerIndex()].fields)
                        Log.d("GameActivity", "value - ${i.value}")
                    Log.d("GameActivity", "State - end turn")
                    currentState.setState(EndTurn())
                }
            }
        }

        for (i in 0 until 5) {
            views[i].visibility = View.VISIBLE
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

        if(game.stirDeck1.isNotEmpty()) {
            stir1IV.setImageResource(ImageConverter.getImage(game.stirDeck1[game.stirDeck1.size - 1]))
        }
        else {
            stir1IV.setImageResource(ImageConverter.getImage(Card(0,false)))
        }
        if(game.stirDeck2.isNotEmpty()) {
            stir2IV.setImageResource(ImageConverter.getImage(game.stirDeck2[game.stirDeck2.size - 1]))
        }
        else {
            stir2IV.setImageResource(ImageConverter.getImage(Card(0,false)))
        }
    }

    fun changeVisibilityPlayersNicknames(view: View) {
        for (i in 0 until 5) {
            if(nicknamesVisibility)
                names[i].visibility = View.VISIBLE
            else
                names[i].visibility = View.INVISIBLE
        }
        if(nicknamesVisibility)
            hintCardTV.visibility = View.VISIBLE
        else
            hintCardTV.visibility = View.INVISIBLE
        nicknamesVisibility = !nicknamesVisibility
    }
}