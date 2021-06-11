package com.aiden.unscrambleappwithviewmodel.data

import android.util.Log
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
    private var offset: Int = 0

    private var wordsList: MutableList<String> = mutableListOf()

    private var _currentWord: MutableLiveData<String> = MutableLiveData("")
    val currentWord: LiveData<String>
        get() = _currentWord


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
                val words1 = wordsRepository.getWords(offset, wordsLength)
                _words.value = words1.data.items
//                Log.d("WordViewModel", words1.data.items.toString())
            } catch (e: Exception) {
                e.printStackTrace()
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

    private fun getNextWord() {
        if (words.value!!.isNotEmpty()) {
            _currentWord.value = words.value!!.random()

            val tempWord = _currentWord.value!!.toCharArray()
            tempWord.shuffle()

            while (String(tempWord) == _currentWord.value) {
                tempWord.shuffle()
            }
            if (wordsList.contains(_currentWord.value)) {
                getNextWord()
            } else {
                _currentScrambledWord.value = String(tempWord)
                _wordCount.value = _wordCount.value!! + 1
                wordsList.add(_currentWord.value!!)
            }
        }
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(_currentWord.value, true)) {
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
        _currentScrambledWord.value = "test"
        offset = round * 100 + 1
        round += 1
        _words.value = emptyList()
    }


    companion object {
        private const val TAG = "WordViewModel"
    }


}