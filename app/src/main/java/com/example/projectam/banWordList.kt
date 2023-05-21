package com.example.projectam

class banWordList {
    companion object {
        private val banWordList = listOf("players") // we can add anythin here

        fun isValidName(name: String): Boolean {
            val lowerCaseName = name.toLowerCase()
            return !banWordList.any { lowerCaseName.contains(it) }
        }
    }
}