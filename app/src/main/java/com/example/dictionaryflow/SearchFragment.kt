package com.example.dictionaryflow

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_fragment.view.*


class SearchFragment : Fragment() {

    private var disposable: Disposable? = null

    private val wordsApiService by lazy {
        WordsApiService.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentsView: View = inflater.inflate(R.layout.search_fragment, container, false)

        fragmentsView.button.setOnClickListener {
            if (editText.text.toString().isNotEmpty()) {
                beginSearch(editText.text.toString())
            }
        }

        return fragmentsView
    }

    companion object {

        fun newInstance(): SearchFragment {
            return newInstance()
        }
    }

    private fun beginSearch(searchWord: String) {
        disposable = wordsApiService.getWordsInformation(searchWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> textView.text = "${result.mpronunciation} ${result}" },
                { error -> Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show() }
            )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}