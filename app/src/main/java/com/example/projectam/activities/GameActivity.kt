package com.example.projectam.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.*
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
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
import kotlin.math.max
import kotlin.math.min

class GameActivity : AppCompatActivity() {
    private var views: MutableList<RecyclerView> = mutableListOf()
    private var viewsHighlighters: MutableList<FrameLayout> = mutableListOf()
    private var names: MutableList<TextView> = mutableListOf()

    private lateinit var deckIV: ImageView
    private lateinit var deckHighlighter: FrameLayout
    private lateinit var stir1IV: ImageView
    private lateinit var stir1Highlighter: FrameLayout
    private lateinit var stir2IV: ImageView
    private lateinit var stir2Highlighter: FrameLayout

    private  var nicknamesVisibility: Boolean = false

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

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

        initViews()
        currentState = GameStateContext(
            updatePlayer,
            viewsHighlighters[ClientInfo.id],
            deckIV, deckHighlighter,
            stir1IV, stir1Highlighter,
            stir2IV, stir2Highlighter,
            hintCardIV, this)
        createListener()
    }
    // region zoom
    // ToDo it is not working correctly
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
        FirebaseManager.initGameUpdaterListener(updateAdapters, this)
    }
    override fun onResume() {
        super.onResume()
        FirebaseManager.addGameUpdater(ClientInfo.gameCode)
    }
    override fun onPause() {
        super.onPause()
        FirebaseManager.deleteGameUpdater(ClientInfo.gameCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseManager.deleteUser(ClientInfo.gameCode, ClientInfo.id)
    }
    // endregion firebase


    private fun initViews() {
        views.add(findViewById(R.id.player1))
        views.add(findViewById(R.id.player2))
        views.add(findViewById(R.id.player3))
        views.add(findViewById(R.id.player4))
        views.add(findViewById(R.id.player5))


        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.highlightBorderColor, typedValue, true)
        val borderColor = typedValue.data

        viewsHighlighters.add(findViewById(R.id.player1Highlighter))
        viewsHighlighters.add(findViewById(R.id.player2Highlighter))
        viewsHighlighters.add(findViewById(R.id.player3Highlighter))
        viewsHighlighters.add(findViewById(R.id.player4Highlighter))
        viewsHighlighters.add(findViewById(R.id.player5Highlighter))

        names.add(findViewById(R.id.namePlayer1))
        names.add(findViewById(R.id.namePlayer2))
        names.add(findViewById(R.id.namePlayer3))
        names.add(findViewById(R.id.namePlayer4))
        names.add(findViewById(R.id.namePlayer5))

        deckHighlighter = findViewById(R.id.deckHighlighter)
        stir1Highlighter = findViewById(R.id.stir1Highlighter)
        stir2Highlighter = findViewById(R.id.stir2Highlighter)

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
            if(player == null)
                continue
            if (player.id == ClientInfo.id) {
                views[ClientInfo.id].adapter = GameAdapter(this, player.fields, currentState)
                break
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

        for (index in 0 until ClientInfo.gameSize) {
            if(index >= game.players.size) {
                names[index].text = "Empty"
                views[index].visibility = View.INVISIBLE
                continue
            }
            val player = game.players[index]
            if(player == null) {
                names[index].text = "Empty"
                views[index].visibility = View.INVISIBLE
                continue
            }
            views[index].visibility = View.VISIBLE
            views[index].adapter = GameAdapter(this, player.fields, null)
            names[index].text = player.username
        }

        if(game.playerTurn == ClientInfo.id){
            Log.d("GameActivity", "State - choose deck")
            currentState.setState(ChoosingDeck())
            if(!game.players[GameManager.getCurrentPlayerIndex(game)]!!.isRevealedAny()) {
                Log.d("GameActivity", "State - reveal first card")
                currentState.setState(RevealFirstCard())
            }
            if(game.isFinished) {
                Log.d("GameActivity", "State - reveal mirrors")
                currentState.setState(RevealMirrors())
                if(game.players[GameManager.getCurrentPlayerIndex(game)]!!.fields.all {card ->
                        card.value != -1
                    }) {
                    Log.d("GameActivity", "State - end turn")
                    currentState.setState(EndTurn())
                }
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

    fun leaveLobby(view: View) {
        FirebaseManager.deleteGameUpdater(ClientInfo.gameCode)
        FirebaseManager.deleteUser(ClientInfo.gameCode, ClientInfo.id)
        startActivity(Intent(this, ConnectActivity::class.java))
    }
}