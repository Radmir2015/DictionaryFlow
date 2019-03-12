package com.example.dictionaryflow

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface WordsApiService {

    @Headers("X-RapidAPI-Key: r4DOlfcsetmshSpL9OKXFC6ZV255p1LxemOjsnQ9JS6drNuVD7")
    @GET("words/{word}/")
    fun getWordsInformation(@Path("word") word: String): Observable<Model.Result>

    companion object {
        fun create(): WordsApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://wordsapiv1.p.rapidapi.com/")
                .build()

            return retrofit.create(WordsApiService::class.java)
        }
    }

}