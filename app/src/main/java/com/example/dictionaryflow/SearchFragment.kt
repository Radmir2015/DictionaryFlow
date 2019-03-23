package com.example.dictionaryflow

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_fragment.view.*
import org.greenrobot.eventbus.EventBus


class SearchFragment : Fragment() {

    private var disposable: Disposable? = null

    private var wordParts: ArrayList<String?>? = ArrayList()
    private var titleWordParts: ArrayList<String?>? = ArrayList()
    private var wordAdapter: WordPartsAdapter? = null

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

        val wordListView = fragmentsView.findViewById<ListView>(R.id.wordListView)

        if (savedInstanceState != null) {

            wordParts = savedInstanceState.getStringArrayList("word_parts")
            titleWordParts = savedInstanceState.getStringArrayList("title_word_parts")

            if (wordParts != null && titleWordParts != null)
                wordAdapter = WordPartsAdapter(activity, wordParts, titleWordParts)

        } else
            wordAdapter = WordPartsAdapter(activity, wordParts, titleWordParts)

        wordListView.adapter = wordAdapter

        return fragmentsView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArrayList("word_parts", wordParts)
        outState.putStringArrayList("title_word_parts", titleWordParts)
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
                { result -> run {
                    wordParts?.clear()
                    titleWordParts?.clear()

                    wordAdapter?.notifyDataSetChanged()

                    EventBus.getDefault().post(WordAndObjectEvent(result.word, result))

                    val titlesAndDescriptionData = DescriptionModel.getTitlesAndDescriptions(result)

                    titleWordParts = titlesAndDescriptionData.titles
                    wordParts = titlesAndDescriptionData.descriptions

                    wordAdapter?.setTitleItems(titleWordParts)
                    wordAdapter?.setItems(wordParts)

                    wordAdapter?.notifyDataSetChanged()

                    wordListView.setSelectionAfterHeaderView()
                    }
                },
                { error -> run {
                    Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}