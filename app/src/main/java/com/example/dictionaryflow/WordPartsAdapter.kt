package com.example.dictionaryflow

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class WordPartsAdapter(private var activity: FragmentActivity?, private var items: ArrayList<String?>?, private var titleItems: ArrayList<String?>?): BaseAdapter() {

    private class ViewHolder(row: View?) {
        var txtBold: TextView? = null
        var txtName: TextView? = null

        init {
            this.txtBold = row?.findViewById(R.id.titleWordPartsItemText)
            this.txtName = row?.findViewById(R.id.wordPartsItemText)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.word_parts_item, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.txtBold?.text = titleItems?.get(position)
        viewHolder.txtName?.text = items?.get(position)

        return view as View
    }

    fun setItems(items: ArrayList<String?>?) {
        this.items = items
    }

    fun setTitleItems(titleItems: ArrayList<String?>?) {
        this.titleItems = titleItems
    }

    override fun getItem(i: Int): String? {
        return items?.get(i)
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items?.size as Int
    }
}