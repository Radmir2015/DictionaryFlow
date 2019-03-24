package com.example.dictionaryflow

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SavedFragment : Fragment() {

    private var dbHelper: FavoritesDbHelper? = null
    private var words: ArrayList<String?>? = ArrayList()
    private var titles: ArrayList<ArrayList<String?>?> = ArrayList()
    private var descriptions: ArrayList<ArrayList<String?>?> = ArrayList()
    private var favAdapter: FavoritesAdapter? = null
    private var favoritesListView: ExpandableListView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentsView = inflater.inflate(R.layout.saved_fragment, container, false)

        dbHelper = FavoritesDbHelper(activity as Context)
        val dbRow = dbHelper?.getAllWords()

        words = dbRow?.map { it?.word } as ArrayList

        dbRow.forEach {
            val result = it?.obj

            val titlesAndDescriptionsData = DescriptionModel.getTitlesAndDescriptions(result)

            titles.add(titlesAndDescriptionsData.titles)
            descriptions.add(titlesAndDescriptionsData.descriptions)
        }

        favoritesListView = fragmentsView?.findViewById(R.id.favoritesExpListView) as ExpandableListView

        favAdapter = FavoritesAdapter(activity, words, titles, descriptions)
        favoritesListView?.setAdapter(favAdapter)

        return fragmentsView
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        Toast.makeText(activity, event.message, Toast.LENGTH_SHORT).show()

        words?.clear(); titles.clear(); descriptions.clear()

        val dbRow = dbHelper?.getAllWords()
        dbRow?.forEach {
            val result = it?.obj

            val titlesAndDescriptionsData = DescriptionModel.getTitlesAndDescriptions(result)
            words = dbRow.map { it?.word } as ArrayList

            titles.add(titlesAndDescriptionsData.titles)
            descriptions.add(titlesAndDescriptionsData.descriptions)
        }

        favAdapter?.setWords(words); favAdapter?.setTitles(titles); favAdapter?.setDescriptions(descriptions)
        favAdapter?.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    companion object {

        fun newInstance(): SavedFragment {
            return newInstance()
        }
    }
}