package com.aiden.unscrambleappwithviewmodel.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden.unscrambleappwithviewmodel.data.repository.WordRepository
import com.aiden.unscrambleappwithviewmodel.model.MAX_NO_OF_WORDS
import kotlinx.coroutines.launch

class WordViewModel : ViewModel() {

    private val wordsRepository = WordRepository()

    private val _words = MutableLiveData<List<String>>(emptyList())

    val words: LiveData<List<String>>
        get() = _words

    private var round: Int = 1
    private var offset: Int = round*100+1
    private var limit: Int = 100

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String


    private var _score: MutableLiveData<Int> = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private var _wordCount: MutableLiveData<Int> = MutableLiveData(0)
    val wordCount: LiveData<Int>
        get() = _wordCount

    private val _currentScrambledWord: MutableLiveData<String> = MutableLiveData("test")
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    fun getWords(
        wordsLength: Int
    ) {
        viewModelScope.launch {
            try {
                val words1 = wordsRepository.getWords(offset, limit, wordsLength)
                _words.value = words1.data.items
            } catch (e: Exception) {
                _words.value = emptyList()
            }
        }
    }

    fun nextWord(): Boolean {
        return if (wordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    fun getNextWord() {
        if (words.value!!.isNotEmpty()) {
            currentWord = words.value!!.random()

            val tempWord = currentWord.toCharArray()
            tempWord.shuffle()

            while (String(tempWord) == currentWord) {
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

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    private fun increaseScore() {
        _score.value = _score.value!! + 1
    }

    fun reset() {
        _score.value = 0
        _wordCount.value = 0
        round += 1
        wordsList = mutableListOf()
        println(round)
    }


}