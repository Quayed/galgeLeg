package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RulesActivity extends Activity implements View.OnClickListener{
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.rules_activity);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == backButton){
            finish();
        }
    }
}
