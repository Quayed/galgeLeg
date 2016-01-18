package com.example.jonas.galgelegaflevering;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by mathias on 18/01/16.
 */
public interface HighscoreSubscriber {
    void onHighscoreUpdate(List<ParseObject> highscore);
}
