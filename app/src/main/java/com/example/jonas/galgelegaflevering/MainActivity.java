package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {
    private RelativeLayout loadingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Galgelogik.getInstance().initDB(this);
        Galgelogik.getInstance().nulstil();

        setContentView(R.layout.activity_main);
        
        loadingStatus = (RelativeLayout) findViewById(R.id.loading);

        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new MainMenuFragment())
                .commit();

        if (isConnected()) {
            loadingStatus.setVisibility(View.VISIBLE);
            new AsyncTask<Activity, Void, Integer>() {
                protected Integer doInBackground(Activity... arg0) {
                    try {
                        Galgelogik.getInstance().hentNyeOrd();
                        return -1;
                    } catch (IOException e) {
                        e.printStackTrace();
                        Galgelogik.getInstance().setWordsUpdated(false);
                        return 1;
                    }
                }

                @Override
                protected void onPostExecute(Integer resultat) {
                    if (resultat == -1){
                        if(loadingStatus != null){
                            loadingStatus.setVisibility(View.GONE);
                        }
                        Toast.makeText(MainActivity.this, "Ordene er blevet opdateret fra databasen", Toast.LENGTH_SHORT).show();
                    } else if (resultat == 1) {
                        Toast.makeText(MainActivity.this, "Ordene kunne ikke hentes fra databasen.", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(this);
        } else {
            Toast.makeText(MainActivity.this, "Ordene kunne ikke hentes fra databasen", Toast.LENGTH_SHORT).show();
        }


        Parse.initialize(getApplicationContext());
        Galgelogik.getInstance().updateHighscore();
    }

    private boolean isConnected() {
        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
