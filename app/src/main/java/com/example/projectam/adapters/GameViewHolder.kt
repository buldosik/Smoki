package com.example.projectam.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectam.R
import com.example.projectam.utils.Card
import com.example.projectam.utils.ImageConverter

class GameViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("ResourceType")
    val imageView: ImageView = itemView.findViewById(R.id.imageView4)

    @SuppressLint("SetTextI18n")
    fun bindView(card: Card) {
        if (card.isRevealed) {
            ImageConverter.getImage(card)
        } else {
            imageView.setImageResource(R.drawable.close_image_vert)
        }
    }
}