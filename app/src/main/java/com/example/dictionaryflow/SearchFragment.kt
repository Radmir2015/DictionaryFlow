package com.example.dictionaryflow

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        fragmentsView.textView.movementMethod = ScrollingMovementMethod()

//        if (savedInstanceState != null) {
//            fragmentsView.editText.setText(savedInstanceState.getString("search_word"))
//            fragmentsView.textView.text = savedInstanceState.getString("result")
//        }

        return fragmentsView
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putString("search_word", editText.text.toString())
//        outState.putString("result", textView.text.toString())
//        super.onSaveInstanceState(outState)
//    }

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
                { result -> run {
                    val order = DescriptionModel.CallOrder(result).getOrder()
                    val results = DescriptionModel.CallOrder(result).getResultsOrder()
                    var total = ""

                    order.forEach {
                        if (it.value != null)
                            total += "${it.title}\n${it.description}\n\n"
                    }

                    results.forEach {
                        it.forEach { elem ->
                            if (elem.value != null)
                                total += "${elem.title}\n" +
                                        "${elem.description}\n" +
                                        "\n"
                        }
                        total += "\n"
                    }

                    textView.text = total
                    }
                },
                { error -> Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show() }
            )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}