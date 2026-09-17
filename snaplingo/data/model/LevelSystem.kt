package com.example.snaplingo.data.model

object LevelSystem {

    fun getLevelFromXp(xp: Int): Int {

        return when {

            xp < 100 -> 1
            xp < 250 -> 2
            xp < 450 -> 3
            xp < 700 -> 4
            xp < 1000 -> 5
            else -> 6
        }
    }

    fun getXpForCurrentLevel(xp: Int): Int {

        return when (getLevelFromXp(xp)) {

            1 -> 0
            2 -> 100
            3 -> 250
            4 -> 450
            5 -> 700
            else -> 1000
        }
    }

    fun getXpForNextLevel(xp: Int): Int {

        return when (getLevelFromXp(xp)) {

            1 -> 100
            2 -> 250
            3 -> 450
            4 -> 700
            5 -> 1000
            else -> 1000
        }
    }
}