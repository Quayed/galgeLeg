package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {
    static Galgelogik galgeLogik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new MainMenuFragment())
                .commit();

        if (galgeLogik == null) {
            galgeLogik = new Galgelogik(this);

            new AsyncTask<Activity, Void, Integer>() {
                protected Integer doInBackground(Activity... arg0) {
                    try {
                        MainActivity.galgeLogik.hentOrdFraDr();
                        return -1;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return 1;
                    }
                }

                @Override
                protected void onPostExecute(Integer resultat) {
                    if(resultat == 1){
                        Toast.makeText(MainActivity.this, "Ordene kunne ikke hentes fra DR", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(this);
        }
    }
}
