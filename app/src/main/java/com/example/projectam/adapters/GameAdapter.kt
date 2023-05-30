package com.example.projectam.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.R
import com.example.projectam.states.GameState
import com.example.projectam.states.GameStateContext
import com.example.projectam.utils.Card
import com.example.projectam.utils.OnItemListener

class GameAdapter (
    private val context: Context,
    private val cards: MutableList <Card>,
    private val listener: GameStateContext?
        ) : RecyclerView.Adapter<GameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.game_cell, parent, false)
        return GameViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bindView(cards[position])
        if (listener != null) {
            holder.imageView.setOnClickListener{ listener.currentState.onItemClick(position) }
        }
    }
}



