package com.example.dictionaryflow

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private val wordsApiService by lazy {
        WordsApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if (editText.text.toString().isNotEmpty()) {
                beginSearch(editText.text.toString())
            }
        }
    }

    private fun beginSearch(searchWord: String) {
        disposable = wordsApiService.getWordsInformation(searchWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> textView.text = "Definition: ${result.results}\n" +
                        "Frequency: ${result.frequency} ${result}" },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )
    }
}
