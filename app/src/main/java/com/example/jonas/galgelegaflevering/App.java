package com.example.jonas.galgelegaflevering;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.parse.Parse;

/**
 * Created by mathias on 20/01/16.
 */
public class App extends Application {
    @Override
    public void onCreate(){
        Parse.initialize(getApplicationContext());
        super.onCreate();
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
