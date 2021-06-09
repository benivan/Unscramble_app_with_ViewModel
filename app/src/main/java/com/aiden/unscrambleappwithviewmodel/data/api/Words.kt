package com.aiden.unscrambleappwithviewmodel.data.api

import com.aiden.unscrambleappwithviewmodel.data.response.WordResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Words {

    @GET("wordfinder/v1/wordlist")
    suspend fun getWords(
        @Query("offset") offset: Int,
        @Query("word_length") wordLength: Int,
        @Query("limit") limit: Int = 100,
        @Query("dictionary") dictionary: String = "WWF",
        @Query("order_by") orderBy: String = "score",
    ): WordResponse


    companion object {

        private const val baseUrl = "https://api.yourdictionary.com/"

        private val client = OkHttpClient.Builder()
            .build()

        fun createService(): Words {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(Words::class.java)
        }
    }

}