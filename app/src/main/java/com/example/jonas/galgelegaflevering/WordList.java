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

        updateList();


        //info.setText("Henter ord fra DRs server...."); Muligvis en toast

        new AsyncTask() {
            protected Object doInBackground(Object... arg0) {
                try {
                    MainActivity.galgeLogik.hentOrdFraDr();
                    return "Ordene blev korrekt hentet fra DR's server";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Ordene blev ikke hentet korrekt: "+e;
                }
            }

            @Override
            protected void onPostExecute(Object resultat) {
                updateList();
               // info.setText("resultat: \n" + resultat);
            }
        }.execute();
    }

    protected void updateList(){
        String[] lande = MainActivity.galgeLogik.getMuligeOrd();

        wordList.setAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, lande));
    }
}
