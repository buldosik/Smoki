package com.example.projectam.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.R
import com.example.projectam.utils.Player

class ResultAdapter(
    private val context: Context,
    private val players: MutableList <Player>
) : RecyclerView.Adapter<ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.result_cell, parent, false)
        return ResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        if (position > players.size - 1) {
            holder.bindView(null)
        } else {
            holder.bindView(players[position])
        }
    }
}