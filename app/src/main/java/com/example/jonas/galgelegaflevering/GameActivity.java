package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameActivity extends Activity implements View.OnClickListener {
    final int gameOverRequestCode = 1;

    TextView visibleWord;
    ImageView galge;
    ArrayList<Button> keyboard = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_activity);

        visibleWord = (TextView) findViewById(R.id.visibleWord);
        galge = (ImageView) findViewById(R.id.imageView);

        keyboard.add((Button) findViewById(R.id.btn1));
        keyboard.add((Button) findViewById(R.id.btn1));
        keyboard.add((Button) findViewById(R.id.btn2));
        keyboard.add((Button) findViewById(R.id.btn3));
        keyboard.add((Button) findViewById(R.id.btn4));
        keyboard.add((Button) findViewById(R.id.btn5));
        keyboard.add((Button) findViewById(R.id.btn6));
        keyboard.add((Button) findViewById(R.id.btn7));
        keyboard.add((Button) findViewById(R.id.btn8));
        keyboard.add((Button) findViewById(R.id.btn9));
        keyboard.add((Button) findViewById(R.id.btn10));
        keyboard.add((Button) findViewById(R.id.btn11));
        keyboard.add((Button) findViewById(R.id.btn12));
        keyboard.add((Button) findViewById(R.id.btn13));
        keyboard.add((Button) findViewById(R.id.btn14));
        keyboard.add((Button) findViewById(R.id.btn15));
        keyboard.add((Button) findViewById(R.id.btn16));
        keyboard.add((Button) findViewById(R.id.btn17));
        keyboard.add((Button) findViewById(R.id.btn18));
        keyboard.add((Button) findViewById(R.id.btn19));
        keyboard.add((Button) findViewById(R.id.btn20));
        keyboard.add((Button) findViewById(R.id.btn21));
        keyboard.add((Button) findViewById(R.id.btn22));
        keyboard.add((Button) findViewById(R.id.btn23));
        keyboard.add((Button) findViewById(R.id.btn24));
        keyboard.add((Button) findViewById(R.id.btn25));
        keyboard.add((Button) findViewById(R.id.btn26));
        keyboard.add((Button) findViewById(R.id.btn27));
        keyboard.add((Button) findViewById(R.id.btn28));
        keyboard.add((Button) findViewById(R.id.btn29));

        Intent i = getIntent();

        if(i.hasExtra("wordLength")){
            MainActivity.galgeLogik.hentNytOrd(i.getIntExtra("wordLength", 0));
        }

        for (Button btn : keyboard) {
            btn.setOnClickListener(this);
        }

        updateViews();
        MainActivity.galgeLogik.logStatus();
    }

    private void updateViews() {
        if (MainActivity.galgeLogik.erSpilletSlut()) {
            Intent i = new Intent(this, GameOverActivity.class);
            startActivityForResult(i, gameOverRequestCode);
        }

        visibleWord.setText(MainActivity.galgeLogik.getSynligtOrd());

        switch (MainActivity.galgeLogik.getAntalForkerteBogstaver()) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gameOverRequestCode) {
            if (resultCode != RESULT_CANCELED || data != null) {

                boolean playAgain = data.getBooleanExtra("playAgain", false);

                if (!playAgain) {
                    finish(); //player does not want to play again
                } else {
                    MainActivity.galgeLogik.nulstil();
                    updateViews();
                    for (Button btn : keyboard) {
                        btn.getBackground().clearColorFilter();
                        btn.setClickable(true);
                    }
                }
            } else {
                finish(); //back button was pressed on game over screen
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (keyboard.contains(v)) {
            Button btn = (Button) v;
            System.out.println(btn.getText());
            MainActivity.galgeLogik.gætBogstav(btn.getText().toString().toLowerCase());
            if (MainActivity.galgeLogik.erSidsteBogstavKorrekt()) {
                btn.getBackground().setColorFilter(new LightingColorFilter(0, 0x4CAF50));
                btn.setClickable(false);
            } else {
                btn.getBackground().setColorFilter(new LightingColorFilter(0, 0xF44336));
                btn.setClickable(false);
            }
            updateViews();
        }
    }
}
