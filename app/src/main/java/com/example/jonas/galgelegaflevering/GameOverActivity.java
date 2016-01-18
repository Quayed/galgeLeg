package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

public class GameOverActivity extends Activity implements HighscoreSubscriber {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        if(Galgelogik.getInstance().erSpilletVundet() && Galgelogik.getInstance().getScore() > Galgelogik.getInstance().getMinHighscore()){
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new AddToHighscoreFragment())
                    .commit();
        } else{
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new AddToHighscoreFragment())
                    .commit();
        }
    }

    public void returnToGame(boolean playAgain){
        Intent i = new Intent();
        i.putExtra("playAgain", playAgain);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    public void onHighscoreUpdate(List<ParseObject> highscore) {

    }
}
