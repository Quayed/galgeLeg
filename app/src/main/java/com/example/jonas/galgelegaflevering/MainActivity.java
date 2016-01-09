package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

            new AsyncTask() {
                protected Object doInBackground(Object... arg0) {
                    try {
                        MainActivity.galgeLogik.hentOrdFraDr();
                        return "Ordene blev korrekt hentet fra DR's server";
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Ordene blev ikke hentet korrekt: " + e;
                    }
                }

                @Override
                protected void onPostExecute(Object resultat) {

                    // info.setText("resultat: \n" + resultat);
                }
            }.execute(this);
        }
    }
}
