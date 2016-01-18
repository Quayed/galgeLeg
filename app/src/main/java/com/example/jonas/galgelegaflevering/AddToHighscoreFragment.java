package com.example.jonas.galgelegaflevering;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddToHighscoreFragment extends Fragment implements View.OnClickListener{

    Button saveBtn;
    Button dontSaveBtn;
    TextView message;
    EditText nameField;

    public AddToHighscoreFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_to_highscore, container, false);

        saveBtn = (Button) layout.findViewById(R.id.highscoreSave);
        dontSaveBtn = (Button) layout.findViewById(R.id.highscoreDontSave);
        nameField = (EditText) layout.findViewById(R.id.highscoreName);
        message = (TextView) layout.findViewById(R.id.highscoreMessage);

        int highscoreNumber = Galgelogik.getInstance().getNumberOnHighscore();
        if (highscoreNumber != 0){
            message.setText("Tillykke du er nummer " + highscoreNumber + " på highscoren");
        } else{
            ((GameOverActivity)getActivity()).goToGameOver();
        }

        saveBtn.setOnClickListener(this);
        dontSaveBtn.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View v) {
        if(v == saveBtn){
            if(nameField.getText().toString().length() > 1){
                Galgelogik.getInstance().uploadToHighscore(nameField.getText().toString(), Galgelogik.getInstance().getScore(), Galgelogik.getInstance().getWordLength());
                ((GameOverActivity)getActivity()).goToGameOver();
            }
        } else if(v == dontSaveBtn){
            ((GameOverActivity)getActivity()).goToGameOver();
        }
    }
}
