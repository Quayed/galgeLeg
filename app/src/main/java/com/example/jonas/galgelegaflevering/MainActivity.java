package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {
    static Galgelogik galgeLogik;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        
        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new MainMenuFragment())
                .commit();

        if (galgeLogik == null) {
            galgeLogik = new Galgelogik(this);
            if (isConnected()) {
                new AsyncTask<Activity, Void, Integer>() {
                    protected Integer doInBackground(Activity... arg0) {
                        try {
                            MainActivity.galgeLogik.hentNyeOrd();
                            return -1;
                        } catch (IOException e) {
                            e.printStackTrace();
                            MainActivity.galgeLogik.setWordsUpdated(false);
                            return 1;
                        }
                    }

                    @Override
                    protected void onPostExecute(Integer resultat) {
                        if (resultat == -1){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Ordene er blevet opdateret fra databasen", Toast.LENGTH_SHORT).show();
                        } else if (resultat == 1) {
                            Toast.makeText(MainActivity.this, "Ordene kunne ikke hentes fra databasen.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute(this);
            } else {
                Toast.makeText(MainActivity.this, "Ordene kunne ikke hentes fra databasen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
