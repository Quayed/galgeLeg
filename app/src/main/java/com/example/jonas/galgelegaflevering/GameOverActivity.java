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

public class GameOverActivity extends Activity implements View.OnClickListener, HighscoreSubscriber {

    TextView gameOverText;
    Button newGameButton;
    Button endGameButton;
    ImageView gameOverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        if(Galgelogik.getInstance().erSpilletVundet() && Galgelogik.getInstance().getScore() > Galgelogik.getInstance().getMinHighscore()){
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new AddToHighscoreFragment())
                    .commit();
        }

        /*gameOverText = (TextView) findViewById(R.id.gameOverText);
        newGameButton = (Button) findViewById(R.id.newGameButton);
        endGameButton = (Button) findViewById(R.id.endGameButton);
        gameOverImage = (ImageView) findViewById(R.id.gameOverImage);
*/


        /*boolean isGameWon = Galgelogik.getInstance().erSpilletVundet();
        String word = Galgelogik.getInstance().getOrdet();

        if (isGameWon) {

            gameOverText.setText("Tillyke du vandt! Din score var: " + getScore());
            gameOverText.setVisibility(View.VISIBLE);
        } else {
            gameOverText.setText("Øv du tabte. Ordet var " + word);
            gameOverText.setVisibility(View.VISIBLE);
            gameOverImage.setImageResource(R.drawable.forkert6);
            gameOverImage.setVisibility(View.VISIBLE);
        }

        newGameButton.setOnClickListener(this);
        endGameButton.setOnClickListener(this);*/
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();

        if (v == newGameButton) {
            i.putExtra("playAgain", true);
            gameOverText.setVisibility(View.GONE);
            gameOverImage.setVisibility(View.GONE);
        } else if (v == endGameButton) {
            i.putExtra("playAgain", false);
            gameOverText.setVisibility(View.GONE);
            gameOverImage.setVisibility(View.GONE);
        }

        setResult(Activity.RESULT_OK, i);
        finish();
    }


    @Override
    public void onHighscoreUpdate(List<ParseObject> highscore) {

    }
}
