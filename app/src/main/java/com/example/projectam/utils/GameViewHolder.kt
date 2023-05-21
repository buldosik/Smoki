package com.example.projectam.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.R

class GameViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("ResourceType")
    val imageView: ImageView = itemView.findViewById(R.id.playerLayout)

    @SuppressLint("SetTextI18n")
    fun bindView(card: Card) {
        if (card.isRevealed) {
            card.getImage()
        } else {
            imageView.setImageResource(R.drawable.close_image_vert)
        }
    }
}