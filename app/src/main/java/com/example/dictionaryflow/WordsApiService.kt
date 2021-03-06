package com.example.dictionaryflow

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface WordsApiService {

    @GET("words/{word}/")
    fun getWordsInformation(@Header("X-RapidAPI-Key") apiKey: String, @Path("word") word: String): Observable<Model.Result>

    companion object {
        fun create(): WordsApiService {

            val gson: Gson = GsonBuilder()
                .registerTypeAdapter(Model.Result::class.java, Model.Result.ResultsDeserializer())
                .create()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://wordsapiv1.p.rapidapi.com/")
                .build()

            return retrofit.create(WordsApiService::class.java)
        }
    }

}