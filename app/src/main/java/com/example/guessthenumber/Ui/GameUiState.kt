package com.example.guessthenumber.Ui

import com.example.guessthenumber.Data.Levels

data class GameUiState (
    val randomNumber : Int = 0,
    val highest : Int = 0,
    val level : Levels = Levels.EASY,
    val range: IntRange = 1..0,
    val score : Int = 0,
    val count : Int = 1
)