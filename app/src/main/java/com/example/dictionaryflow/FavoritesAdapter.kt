package com.example.dictionaryflow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.TextView
import org.greenrobot.eventbus.EventBus


class FavoritesAdapter(private val mContext: Context?, private var words: ArrayList<String?>?, private var titles: ArrayList<ArrayList<String?>?>, private var descriptions: ArrayList<ArrayList<String?>?>) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return words?.size as Int
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return titles[groupPosition]?.size as Int
    }

    override fun getGroup(groupPosition: Int): Any? {
        return titles[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return descriptions[groupPosition]?.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?,
        parent: ViewGroup
    ): View {
        var view = convertView

        if (view == null) {
            val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.word_view, null)
        }

        if (isExpanded) {

        } else {

        }

        val textGroup = view!!.findViewById(R.id.wordTextView) as TextView
        textGroup.text = words?.get(groupPosition)

        view.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            val dbHelper = FavoritesDbHelper(mContext as Context)
            dbHelper.deleteByWord(words?.get(groupPosition) as String)
            //dbHelper.deleteByWord(((it.parent as View).findViewById(R.id.wordPartsItemText) as TextView).text.toString())
            EventBus.getDefault().post(UpdateDatabase(words?.get(groupPosition)))
            EventBus.getDefault().post(MessageEvent("Updating database"))
        }

        return view

    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        var view = convertView
        if (view == null) {
            val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.word_parts_item, null)
        }

        val boldTextChild = view?.findViewById(R.id.titleWordPartsItemText) as TextView
        val textChild = view.findViewById(R.id.wordPartsItemText) as TextView

        boldTextChild.text = titles[groupPosition]?.get(childPosition)
        textChild.text = descriptions[groupPosition]?.get(childPosition)

        return view
    }

    fun setWords(words: ArrayList<String?>?) {
        this.words = words
    }

    fun setTitles(titles: ArrayList<ArrayList<String?>?>) {
        this.titles = titles
    }

    fun setDescriptions(descriptions: ArrayList<ArrayList<String?>?>) {
        this.descriptions = descriptions
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}