package com.example.dictionaryflow

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class FavoritesDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private var db: SQLiteDatabase? = null

    override fun onCreate(db: SQLiteDatabase) {
        this.db = db

        val createTableQuery = "CREATE TABLE " +
                FavoritedContract.FavoritedTable.TABLE_NAME + " ( " +
                FavoritedContract.FavoritedTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoritedContract.FavoritedTable.COLUMN_WORD + " TEXT, " +
                FavoritedContract.FavoritedTable.COLUMN_OBJ_DATA + " TEXT)"

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritedContract.FavoritedTable.TABLE_NAME)
        onCreate(db)
    }

    fun addWord(word: String, obj: Model.Result) {
        val cv = ContentValues()

        cv.put(FavoritedContract.FavoritedTable.COLUMN_WORD, word)
        cv.put(FavoritedContract.FavoritedTable.COLUMN_OBJ_DATA, Gson().toJson(obj))
        db = readableDatabase
        db!!.insert(FavoritedContract.FavoritedTable.TABLE_NAME, null, cv)
    }

    fun getAllWords(word: String = ""): ArrayList<DbRow?> {
        val favoritesList = ArrayList<DbRow?>()
        db = readableDatabase
        val c = db?.rawQuery("SELECT * FROM " + FavoritedContract.FavoritedTable.TABLE_NAME + if (word.isNotBlank()) " WHERE word = '$word'" else "", null)

        if (c?.moveToFirst() as Boolean) {
            do {
                val dbRow = DbRow(
                    word = c.getString(c.getColumnIndex(FavoritedContract.FavoritedTable.COLUMN_WORD)),
                    obj = Gson().fromJson<Model.Result>(c.getString(c.getColumnIndex(FavoritedContract.FavoritedTable.COLUMN_OBJ_DATA)),
                        object : TypeToken<Model.Result>() {}.type))

                favoritesList.add(dbRow)
            } while (c.moveToNext())
        }

        c.close()
        return favoritesList
    }

    fun searchByWord(word: String): List<DbRow?> {
        return getAllWords(word)
}

    fun deleteByWord(word: String): List<DbRow?> {
        val deleted = searchByWord(word)
        db?.delete(FavoritedContract.FavoritedTable.TABLE_NAME, "word = '$word'", null)
        return deleted
    }

    companion object {
        private const val DATABASE_NAME = "favoritesdb"
        private const val DATABASE_VERSION = 1
    }
}

data class DbRow(var word: String, var obj: Model.Result)