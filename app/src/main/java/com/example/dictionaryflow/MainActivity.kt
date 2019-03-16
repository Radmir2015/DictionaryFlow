package com.example.dictionaryflow

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation: BottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val firstTab = SearchFragment()
        loadFragment(firstTab)
    }

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val selectedFragment: Fragment? =
                when (item.itemId) {
                    R.id.navigation_saved -> SavedFragment()
                    R.id.navigation_search -> SearchFragment()
                    R.id.navigation_about -> AboutFragment()
                    else -> null
                }

            return if (selectedFragment != null) {
                loadFragment(selectedFragment)
                true
            } else
                false
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_content, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}
