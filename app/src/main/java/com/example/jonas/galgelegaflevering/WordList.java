package com.example.jonas.galgelegaflevering;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WordList extends AppCompatActivity {

    ListView wordList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        wordList = (ListView) findViewById(R.id.listOfWords);

        String[] lande = MainActivity.galgeLogik.getMuligeOrd();

        wordList.setAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, lande));
    }

}
