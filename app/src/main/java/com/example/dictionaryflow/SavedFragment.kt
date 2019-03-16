package com.example.dictionaryflow

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SavedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.saved_fragment, container, false)
    }

    companion object {

        fun newInstance(): SavedFragment {
            return newInstance()
        }
    }
}