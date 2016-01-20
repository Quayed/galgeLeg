package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends Activity implements View.OnClickListener, SensorEventListener{
    final int gameOverRequestCode = 1;

    TextView visibleWord;
    ImageView galge;
    Button newWordBtn;
    TextView timeLeft;
    ArrayList<Button> keyboard = new ArrayList<>();

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private static final double SHAKE_THRESHOLD_GRAVITY = 2.7;
    private static final int SHAKE_SLOP_TIME_MS = 50;

    private long shakeTimestamp;
    private boolean popUpActive;
    private CountDownTimer myCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        visibleWord = (TextView) findViewById(R.id.visibleWord);
        galge = (ImageView) findViewById(R.id.galgeView);
        newWordBtn = (Button) findViewById(R.id.newWordBtn);
        timeLeft = (TextView) findViewById(R.id.timeLeft);

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
        keyboard.add((Button) findViewById(R.id.btn12));
        keyboard.add((Button) findViewById(R.id.btn13));
        keyboard.add((Button) findViewById(R.id.btn14));
        keyboard.add((Button) findViewById(R.id.btn15));
        keyboard.add((Button) findViewById(R.id.btn16));
        keyboard.add((Button) findViewById(R.id.btn17));
        keyboard.add((Button) findViewById(R.id.btn18));
        keyboard.add((Button) findViewById(R.id.btn19));
        keyboard.add((Button) findViewById(R.id.btn20));
        keyboard.add((Button) findViewById(R.id.btn23));
        keyboard.add((Button) findViewById(R.id.btn24));
        keyboard.add((Button) findViewById(R.id.btn25));
        keyboard.add((Button) findViewById(R.id.btn26));
        keyboard.add((Button) findViewById(R.id.btn27));
        keyboard.add((Button) findViewById(R.id.btn28));
        keyboard.add((Button) findViewById(R.id.btn29));

        Intent i = getIntent();

        newWordBtn.setOnClickListener(this);
        for (Button btn : keyboard) {
            btn.setOnClickListener(this);
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        myCountDown = new CountDownTimer(100000, 1000){
            public void onTick(long millisUntilFinished){
                timeLeft.setText(Html.fromHtml("Tid tilbage: <b>" + millisUntilFinished/1000 + "s</b>"));

                Galgelogik.getInstance().setTimeLeft((int) millisUntilFinished / 1000);
            }

            public void onFinish(){
                Galgelogik.getInstance().setTimeLeft(0);
                gameOver();
            }
        };

        myCountDown.start();

        Galgelogik.getInstance().nulstil();
        updateViews();
        Galgelogik.getInstance().logStatus();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause(){
        super.onPause();
        myCountDown.cancel();
        sensorManager.unregisterListener(this);
    }

    private void gameOver(){
        Intent i = new Intent(this, GameOverActivity.class);
        startActivityForResult(i, gameOverRequestCode);
    }

    private void updateViews() {
        if (Galgelogik.getInstance().erSpilletSlut()) {
            myCountDown.cancel();
            gameOver();
        }

        visibleWord.setText(Galgelogik.getInstance().getSynligtOrd());

        switch (Galgelogik.getInstance().getAntalForkerteBogstaver()) {
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
                    Galgelogik.getInstance().nulstil();
                    updateViews();
                    clearKeyboard();
                    myCountDown.start();
                }
            } else {
                finish(); //back button was pressed on game over screen
            }
        }
    }

    private void clearKeyboard(){
        for (Button btn : keyboard) {
            btn.getBackground().clearColorFilter();
            btn.setClickable(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == newWordBtn){
            Galgelogik.getInstance().nulstil();
            updateViews();
            clearKeyboard();
            myCountDown.cancel();
            myCountDown.start();
            Toast.makeText(this, "Ord opdateret", Toast.LENGTH_SHORT).show();
        } else if (keyboard.contains(v)) {
            Button btn = (Button) v;
            System.out.println(btn.getText());
            Galgelogik.getInstance().gætBogstav(btn.getText().toString().toLowerCase());
            if (Galgelogik.getInstance().erSidsteBogstavKorrekt()) {
                btn.getBackground().setColorFilter(new LightingColorFilter(0, 0x4CAF50));
                btn.setClickable(false);
            } else {
                btn.getBackground().setColorFilter(new LightingColorFilter(0, 0xF44336));
                btn.setClickable(false);
            }
            updateViews();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        double gX = x / SensorManager.GRAVITY_EARTH;
        double gY = y / SensorManager.GRAVITY_EARTH;
        double gZ = z / SensorManager.GRAVITY_EARTH;

        double gForce = java.lang.Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY){
            final long now = System.currentTimeMillis();

            if(shakeTimestamp + SHAKE_SLOP_TIME_MS > now){
                return;
            }

            shakeTimestamp = now;

            onShake();
        }
    }

    private void onShake(){
        if (!popUpActive) {
            popUpActive = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Skift ord?");
            builder.setMessage("Er du sikker på at du vil gerne vil skifte ord?");
            builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Galgelogik.getInstance().nulstil();
                    updateViews();
                    clearKeyboard();
                    popUpActive = false;
                }
            });
            builder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    popUpActive = false;
                }
            });
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        popUpActive = false;
                        dialog.dismiss();
                    }
                    return true;
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
