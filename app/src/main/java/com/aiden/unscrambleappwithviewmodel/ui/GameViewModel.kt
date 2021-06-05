package com.aiden.unscrambleappwithviewmodel.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aiden.unscrambleappwithviewmodel.model.MAX_NO_OF_WORDS
import com.aiden.unscrambleappwithviewmodel.model.allWordsList

class GameViewModel: ViewModel() {

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String


    private var _score = 0
    val score: String
    get() = _score.toString()

    private var  _wordCount: MutableLiveData<Int> = MutableLiveData(0)
    val wordCount :LiveData<Int>
    get() =  _wordCount

    private val _currentScrambledWord: MutableLiveData<String> = MutableLiveData("test")
    val currentScrambledWord : LiveData<String>
    get() = _currentScrambledWord


    fun nextWord():Boolean{
        return if (wordCount.value!! < MAX_NO_OF_WORDS){
            getNextWord()
            true
        } else false
    }

     fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (tempWord.toString().equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _wordCount.value = _wordCount.value!! + 1
            wordsList.add(currentWord)
        }
    }

}

