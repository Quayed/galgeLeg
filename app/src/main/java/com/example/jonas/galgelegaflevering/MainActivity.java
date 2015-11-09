package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{
    Button gameButton;
    Button rulesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        gameButton = (Button) findViewById(R.id.gameButton);
        rulesButton = (Button) findViewById(R.id.rulesButton);

        gameButton.setOnClickListener(this);
        rulesButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == gameButton){
            Intent i = new Intent(this, GameActivity.class);
            startActivity(i);
        }
        else if(v == rulesButton){
            Intent i = new Intent(this, RulesActivity.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Denne knap er ikke implementeret endnu", Toast.LENGTH_LONG).show();
        }
    }
}
