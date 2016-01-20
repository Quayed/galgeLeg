package com.example.jonas.galgelegaflevering;

import android.app.Application;

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
}
