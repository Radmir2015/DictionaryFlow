package com.example.dictionaryflow

object Model {
    data class Result(
        val results: ArrayList<Results>?,
        val frequency: Number
    )

    data class Results(
        val definition: String,
        val partOfSpeech: String,
        val typeOf: ArrayList<String>
    )
}