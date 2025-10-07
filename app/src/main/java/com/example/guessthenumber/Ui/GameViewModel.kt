package com.example.guessthenumber.Ui

import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import com.example.guessthenumber.Data.GuessedLebel
import com.example.guessthenumber.Data.Levels
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

    // how "check handler" works :
    // user's valid number input to check the accuracy
    // if the difference is
    //          when level : easy (input range 1 - 10)
    //          0 update guessedLebel as accurate, rewarded score +5
    //          +- 1 guessedLebel as close, rewarded score +3
    //          else far, rewarded score 0
    //          when level : medium (input range 1 - 20)
    //          0 update guessedLebel as accurate, rewarded score +5
    //          +- 2 guessedLebel as close, rewarded score +3
    //           else far, rewarded score 0
    //          when level : Hard
    //          0 update guessedLebel as accurate, rewarded score +5
    //          +- 3 guessedLebel as close, rewarded score +3
    //           else far, rewarded score 0
    // update the score
    //          if guessedLebel is accurate score+= 5
    //          if guessedLebel is close score += 3 else score += 0
    // update the highscore
    //          if at any stage the highscore < score update highscore = score
    // when count is 10 click button must disabled, and final result must be display after the score pop up

    // summary
    // 1. update guessedLebel
    // 2. update score.
    // 3. update highscore.
    // 4. count.
    // 5. randomNmber

    fun checkHandler(guessedNumber : Int) : GuessedLebel {
//        if ( uiState.value.count == 10) resetGame()
        val guessedLebel = getGuessMatchLebel(guessedNumber)
        val score = getScoreByGuessedLebel(guessedLebel)
        countUpdator()
        scoreUpdator(score)
        highestUpdator()
        randomNumberUpdator()

        return guessedLebel
    }

    // this function updates score and returns the updated score
    private fun scoreUpdator ( score : Int) : Int{
        _uiState.update {
            current -> current.copy(
                score = current.score + score
            )
        }
        return uiState.value.score
    }

    // this function updates the highest score
    // *call this function only after updating score*
    private fun highestUpdator() : Int {
        _uiState.update {
            current -> current.copy(
                highest = if( current.score > current.highest) current.score else current.highest
            )
        }
        return uiState.value.highest
    }

    // this function increment the count
    private fun countUpdator(){
        _uiState.update {
            current -> current.copy(
                count = current.count + 1
            )
        }
    }

    // this function generate random number
    private fun randomNumberUpdator() : Int {
        _uiState.update {
            current -> current.copy(
                 randomNumber = Random.Default.nextInt(1,10)
            )
        }

        return uiState.value.randomNumber
    }

    // This function returns lebels Far, Close, Accurate base on the number input by user
    private fun getGuessMatchLebel( guessedNum : Int) : GuessedLebel{
        val guessedLebel : GuessedLebel = when(guessedNum){
            uiState.value.randomNumber -> GuessedLebel.ACCURATE
            uiState.value.randomNumber + 1 , uiState.value.randomNumber - 1 -> GuessedLebel.CLOSE
            uiState.value.randomNumber + 2 , uiState.value.randomNumber - 2 -> GuessedLebel.CLOSE
            else -> GuessedLebel.FAR
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

    // this function changes Game's Level
    private fun levelChanger(levelValue : Int){
        _uiState.update {
            current -> current.copy(
                level = when(levelValue){
                    1 -> Levels.MEDIUM
                    2 -> Levels.HARD
                    else -> Levels.EASY
                }
            )
        }
    }


}