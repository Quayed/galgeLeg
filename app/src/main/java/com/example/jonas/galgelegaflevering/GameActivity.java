package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameActivity extends Activity implements View.OnClickListener{
        final int gameOverRequestCode = 1;


        EditText inputField;
        Button guessButton;
        TextView visibleWord;
        TextView usedLetters;
        ImageView galge;


        @Override
        protected void onCreate(Bundle savedInstanceState){
                super.onCreate(savedInstanceState);

                setContentView(R.layout.game_activity);

                inputField = (EditText) findViewById(R.id.editText);
                guessButton = (Button) findViewById(R.id.button);
                visibleWord = (TextView) findViewById(R.id.visibleWord);
                usedLetters = (TextView) findViewById(R.id.usedLetters);
                galge = (ImageView) findViewById(R.id.imageView);

                guessButton.setOnClickListener(this);

                updateViews();
        }

        private void updateViews() {
                if(MainActivity.galgeLogik.erSpilletSlut()){
                        Intent i = new Intent(this, GameOverActivity.class);
                        i.putExtra("isGameWon", MainActivity.galgeLogik.erSpilletVundet());
                        i.putExtra("word", MainActivity.galgeLogik.getOrdet());
                        startActivityForResult(i, gameOverRequestCode);
                }

                inputField.setText("");
                inputField.setError(null);

                visibleWord.setText("Ordet du skal gætte: " + MainActivity.galgeLogik.getSynligtOrd());

                usedLetters.setText("Bogstaver du har brugt: " + MainActivity.galgeLogik.getBrugteBogstaver());

                switch (MainActivity.galgeLogik.getAntalForkerteBogstaver()){
                        case 0:
                                galge.setImageResource(R.drawable.galge);
                                break;
                        case 1:
                                galge.setImageResource(R.drawable.forkert1);
                                break;
                        case 2:
                                galge.setImageResource(R.drawable.forkert2);
                                break;
                        case 3:
                                galge.setImageResource(R.drawable.forkert3);
                                break;
                        case 4:
                                galge.setImageResource(R.drawable.forkert4);
                                break;
                        case 5:
                                galge.setImageResource(R.drawable.forkert5);
                                break;
                        case 6:
                                galge.setImageResource(R.drawable.forkert6);
                                break;
                }

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
                super.onActivityResult(requestCode, resultCode, data);

                if(requestCode == gameOverRequestCode){
                        if(resultCode != RESULT_CANCELED || data != null) {

                                boolean playAgain = data.getBooleanExtra("playAgain", false);

                                if (!playAgain) {
                                        finish(); //player does not want to play again
                                }else {

                                        MainActivity.galgeLogik.nulstil();
                                        updateViews();
                                }
                        }
                        else {
                                finish(); //back button was pressed on game over screen
                        }
                }
        }

        @Override
        public void onClick(View v) {
                if(v == guessButton){
                        String guess = inputField.getText().toString();

                        if(!guess.isEmpty()) {
                                MainActivity.galgeLogik.gætBogstav(guess.substring(0, 1));
                                updateViews();
                        }
                        else {
                                inputField.setError("Indtast bogstav");
                        }
                }
        }
}
