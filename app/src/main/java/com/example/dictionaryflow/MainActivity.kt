package com.example.dictionaryflow

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    val searchTab = SearchFragment()
    val savedTab = SavedFragment()
    val aboutTab = AboutFragment()
    var currentTab: Fragment = searchTab
    var currentTabTag: String? = null
    val fm = supportFragmentManager
    val fragmentsTags = arrayOf("search", "saved", "about")

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

        ShowFragment(currentTabTag as String)
//        showFragment(currentTab)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("fragment_tag", currentTabTag)
        super.onSaveInstanceState(outState)
    }

    private fun ShowFragment(showTag: String, frTags: Array<String> = fragmentsTags) {
        val tags = frTags.filter { it != showTag }

        val temp = fm.beginTransaction().show(fm.findFragmentByTag(showTag) as Fragment)

        for (tag in tags)
            temp.hide(fm.findFragmentByTag(tag) as Fragment)

        temp.commit()

        currentTabTag = showTag
    }

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            //val selectedFragment: Fragment? =
            //currentTab =
                when (item.itemId) {
                    R.id.navigation_saved -> ShowFragment("saved") //savedTab //SavedFragment()
                    R.id.navigation_search -> ShowFragment("search") //searchTab //SearchFragment()
                    R.id.navigation_about -> ShowFragment("about") // aboutTab //AboutFragment()
                    //else -> null
                }

            //showFragment(currentTab)

//            return if (selectedFragment != null) {
//                showFragment(selectedFragment)
//                true
//            } else
//                false
            return true
        }
    }

    private fun showFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_content, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}
