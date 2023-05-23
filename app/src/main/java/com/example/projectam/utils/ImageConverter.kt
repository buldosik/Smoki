package com.example.projectam.utils

import com.example.projectam.R

class ImageConverter {
    companion object {
        fun getImage(card: Card): Int {
            return if (card.isRevealed) {
               when (card.value) {
                    -2 -> return R.drawable.minus_two
                    -1 -> return R.drawable.zero // TODO change to special card
                    0 -> return R.drawable.zero
                    1 -> return R.drawable.onea
                    2 -> return R.drawable.twoa
                    3 -> return R.drawable.threea
                    4 -> return R.drawable.foura
                    5 -> return R.drawable.fivea
                    6 -> return R.drawable.six
                    7 -> return R.drawable.seven
                    8 -> return R.drawable.eight
                    9 -> return R.drawable.nine
                    10 -> return R.drawable.ten
                    else -> R.drawable.close_image_vert
                }
            } else {
                return R.drawable.close_image_vert
            }
        }
    }
}