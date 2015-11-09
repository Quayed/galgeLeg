package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOverActivity extends Activity implements View.OnClickListener{

    TextView gameOverText;
    Button newGameButton;
    Button endGameButton;
    ImageView gameOverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover_activity);

        gameOverText = (TextView) findViewById(R.id.gameOverText);
        newGameButton = (Button) findViewById(R.id.newGameButton);
        endGameButton = (Button) findViewById(R.id.endGameButton);
        gameOverImage = (ImageView) findViewById(R.id.gameOverImage);

        Intent i = getIntent();
        boolean isGameWon = MainActivity.galgeLogik.erSpilletVundet();
        String word = MainActivity.galgeLogik.getOrdet();

        if(isGameWon){
            gameOverText.setText("Tillyke du vandt! Ordet var " + word);
        }
        else{
            gameOverText.setText("Øv du tabte. Ordet var " + word);
            gameOverImage.setImageResource(R.drawable.forkert6);
        }

        newGameButton.setOnClickListener(this);
        endGameButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();

        if(v == newGameButton){
            i.putExtra("playAgain", true);
        }
        else if(v == endGameButton){
            i.putExtra("playAgain", false);
        }

        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
