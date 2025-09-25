package com.example.guessthenumber.Data

enum class Levels {
 EASY, MEDIUM, HARD;

 override fun toString(): String {
  val sentenceCase = super.toString()
  return sentenceCase[0].uppercaseChar() + sentenceCase.substring(1).lowercase()
 }
}