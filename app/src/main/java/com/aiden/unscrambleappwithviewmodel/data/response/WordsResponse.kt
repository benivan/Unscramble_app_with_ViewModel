package com.aiden.unscrambleappwithviewmodel.data.response

import com.google.gson.annotations.SerializedName


data class WordResponse (
    val status: Long,
    val message: Any? = null,
    val errors: Any? = null,
    val data: Data
) 
data class Data (
    @SerializedName( "_meta")
    val meta: Meta,

    @SerializedName("_scores")
    val scores: List<Long>,

    @SerializedName("_items")
    val items: List<String>,
)

data class Meta (
    val dictionary: String,
    val total: Long,
    val limit: Long,
    val offset: Long,

    @SerializedName("word_length")
    val wordLength: Long,

    @SerializedName("order_by")
    val orderBy: String,

    @SerializedName("group_by")
    val groupBy: String
)

