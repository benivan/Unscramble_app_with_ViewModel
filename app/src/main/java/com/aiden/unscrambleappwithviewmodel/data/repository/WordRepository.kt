package com.aiden.unscrambleappwithviewmodel.data.repository

import com.aiden.unscrambleappwithviewmodel.data.api.Words

class WordRepository {

    private val wordsService = Words.createService()

    suspend fun getWords(
        offset: Int,
        wordsLength: Int
    ) = wordsService.getWords(offset,wordsLength)
}