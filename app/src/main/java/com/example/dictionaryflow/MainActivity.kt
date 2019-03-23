package com.example.dictionaryflow

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {

    private var currentTabTag: String? = null
    private val fm: FragmentManager = supportFragmentManager
    private val fragmentsTags = arrayOf("search", "saved", "about")
    private var menu: Menu? = null
    private var addableToFavorites: Boolean = false
    private var favoriteWasShown: Boolean = false
    private var favorited: Boolean = false
    private var dbHelper = FavoritesDbHelper(this)
    private var displayedWord: String? = null
    private var displayedObj: Model.Result? = null

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

        if (savedInstanceState != null) {
            favoriteWasShown = savedInstanceState.getBoolean("favoriteWasShown")
            addableToFavorites = savedInstanceState.getBoolean("addableToFavorites")
            favorited = savedInstanceState.getBoolean("favorited")
            displayedWord = savedInstanceState.getString("displayedWord")
            displayedObj = Gson().fromJson<Model.Result>(
                savedInstanceState.getString("displayedObj"),
                object : TypeToken<Model.Result>() {}.type)

            currentTabTag = savedInstanceState.getString("fragment_tag")
        } else {
            currentTabTag = "search"

            unsetIcon()
        }

        showFragment(currentTabTag as String)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("fragment_tag", currentTabTag)
        outState?.putBoolean("favoriteWasShown", favoriteWasShown)
        outState?.putBoolean("addableToFavorites", addableToFavorites)
        outState?.putBoolean("favorited", favorited)
        outState?.putString("displayedWord", displayedWord)
        outState?.putString("displayedObj", Gson().toJson(displayedObj))
//        outState?.putString("dbHelper", Gson().toJson(dbHelper))
        dbHelper = FavoritesDbHelper(this)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        this.menu = menu

        if (addableToFavorites) {
            if (favoriteWasShown)
                if (favorited)
                    setFavorited()
                else
                    setUnfavorited()
        } else
            unsetIcon()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.favorite) {
            if (addableToFavorites) {
                if (!favorited) {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp)

                    dbHelper.addWord(displayedWord as String, displayedObj as Model.Result)

                    EventBus.getDefault().post(MessageEvent("Added the word '$displayedWord' to the database"))
                } else {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp)

                    dbHelper.deleteByWord(displayedWord as String)

                    EventBus.getDefault().post(MessageEvent("Deleted the word '$displayedWord' from the database"))
                }
                favorited = !favorited
            }
            true // send to database
        } else super.onOptionsItemSelected(item)

    }

    private fun setFavorited() {
        menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp)
    }

    private fun setUnfavorited() {
        menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp)
    }

    private fun unsetIcon() {
        menu?.getItem(0)?.icon = ColorDrawable(Color.TRANSPARENT)
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

            if (item.itemId == R.id.navigation_saved || item.itemId == R.id.navigation_about) {
                unsetIcon()
                addableToFavorites = false
            } else {
                if (favoriteWasShown)
                    if (favorited) setFavorited()
                    else setUnfavorited()
                addableToFavorites = true
            }

            return true
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWordAndObjectEvent(event: WordAndObjectEvent) {
        displayedWord = event.word
        displayedObj = event.obj

        addableToFavorites = true
        favoriteWasShown = true

        checkWordInDatabase(displayedWord)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateDatabase(event: UpdateDatabase) {
        checkWordInDatabase(event.word)
    }

    private fun checkWordInDatabase(word: String?) {
        val result = dbHelper.searchByWord(word as String)
        if (result.isNotEmpty()) {
            favorited = true
            if (addableToFavorites)
                setFavorited()
        } else {
            favorited = false
            if (addableToFavorites)
                setUnfavorited()
        }
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}
