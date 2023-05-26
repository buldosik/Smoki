package com.example.projectam.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.R
import com.example.projectam.utils.Player

class ResultViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("ResourceType")
    val playerView: LinearLayout = itemView.findViewById(R.id.playerLayout)

    @SuppressLint("SetTextI18n")
    fun bindView(player: Player?) {
        if (player != null) {
            playerView.findViewById<TextView>(R.id.playerID).text = player.username
            playerView.findViewById<TextView>(R.id.score).text = player.score.toString()
        } else {
            playerView.findViewById<TextView>(R.id.playerID).text = "Empty"
        }
    }
}