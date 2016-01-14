package com.example.jonas.galgelegaflevering;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mathias on 14/01/16.
 */
public class WordsDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "wordsDB";

    public static final int ID = 0;
    public static final int WORD = 1;
    public static final int TIMESUSED = 2;
    public static final int WORDLENGTH = 3;

    public WordsDB(Context context){
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE words (id INTEGER PRIMARY KEY, word TEXT NOT NULL, timesUsed INTEGER, wordLength INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
