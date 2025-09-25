package com.example.guessthenumber.Ui

import androidx.lifecycle.ViewModel
import com.example.guessthenumber.Data.GuessedLebel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class GameViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        resetGame()
    }

    // reset game
    fun resetGame(){
        _uiState.update {
            currentState ->
            currentState.copy(
                randomNumber = Random.Default.nextInt(1,10),
                score = 0,
                count = 0
            )
        }
    }

    // check handler

    fun checkHandler(guessedNumber : Int) : GuessedLebel {

//        if ( uiState.value.count == 10) resetGame()
        val guessedLebel = getGuessMatchLebel(guessedNumber)
        val score = getScoreByGuessedLebel(guessedLebel)

        _uiState.update {
            current -> current.copy(
                count = current.count + 1,
                score = score + current.score,
                randomNumber = Random.Default.nextInt(1,10),
                highest = if( current.score > current.highest) current.score else current.highest
            )
        }

        return guessedLebel
    }

    // get average
    fun getScoreAvg() : Int{
        val avg = uiState.value.score * 2   //    [ score /50 ] x 100 = score * 2
        if (avg > uiState.value.highest) _uiState.value.copy( highest = avg)
        return avg
    }

    // returns the guessLebel i.e. far , close, accurate according to guessed number
    private fun getGuessMatchLebel( guessedNum : Int) : GuessedLebel{
        val guessedLebel : GuessedLebel = when(guessedNum){
            uiState.value.randomNumber -> {
                GuessedLebel.ACCURATE
            }
            uiState.value.randomNumber + 1 , uiState.value.randomNumber - 1 -> {
                GuessedLebel.CLOSE
            }uiState.value.randomNumber + 2 , uiState.value.randomNumber - 2 -> {
                GuessedLebel.CLOSE
            }
            else -> {
                GuessedLebel.FAR
            }
        }
        return guessedLebel
    }

    // returns the score according to guessLebel ie. far : 0, close : 3, accurate : 5
    private fun getScoreByGuessedLebel(guess : GuessedLebel): Int{
        return  when(guess){
            GuessedLebel.FAR -> 0
            GuessedLebel.CLOSE -> 3
            GuessedLebel.ACCURATE -> 5
        }
    }

}