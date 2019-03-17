package com.example.dictionaryflow

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    private var currentTabTag: String? = null
    private val fm: FragmentManager = supportFragmentManager
    private val fragmentsTags = arrayOf("search", "saved", "about")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (fm.findFragmentByTag("search") == null && fm.findFragmentByTag("saved") == null && fm.findFragmentByTag("about") == null) {
            fm.beginTransaction()
                .add(R.id.fl_content, SearchFragment(), "search")
                .add(R.id.fl_content, SavedFragment(), "saved")
                .add(R.id.fl_content, AboutFragment(), "about")
                .commit()

            fm.popBackStackImmediate()
        }

        val navigation: BottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        currentTabTag =
            if (savedInstanceState != null)
                savedInstanceState.getString("fragment_tag")
            else
                "search"

        showFragment(currentTabTag as String)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("fragment_tag", currentTabTag)
        super.onSaveInstanceState(outState)
    }

    private fun showFragment(showTag: String, frTags: Array<String> = fragmentsTags) {
        val tags = frTags.filter { it != showTag }
        val temp = fm.beginTransaction().show(fm.findFragmentByTag(showTag) as Fragment)

        for (tag in tags)
            temp.hide(fm.findFragmentByTag(tag) as Fragment)

        temp.commit()

        currentTabTag = showTag
    }

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.navigation_saved -> showFragment("saved")
                R.id.navigation_search -> showFragment("search")
                R.id.navigation_about -> showFragment("about")
            }
            return true
        }
    }

//    private fun showFragment(fragment: Fragment) {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.fl_content, fragment)
//        ft.addToBackStack(null)
//        ft.commit()
//    }
}
