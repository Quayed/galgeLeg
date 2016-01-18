package com.example.jonas.galgelegaflevering;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GameOverFragment extends Fragment implements View.OnClickListener{

    TextView gameOverStatus;
    Button restartGameBtn;
    Button changeWordLengthBtn;
    Button toMainMenuBtn;

    public GameOverFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_game_over, container, false);

        gameOverStatus = (TextView) layout.findViewById(R.id.gameOverStatus);
        restartGameBtn = (Button) layout.findViewById(R.id.newGameButton);
        changeWordLengthBtn = (Button) layout.findViewById(R.id.changeWordLengthBtn);
        toMainMenuBtn = (Button) layout.findViewById(R.id.toMainMenu);

        if(Galgelogik.getInstance().erSpilletVundet()){
            gameOverStatus.setText("Du vandt spillet!");
        }else{
            gameOverStatus.setText("Du tabte spillet - prøv igen!");
        }

        return layout;
    }


    @Override
    public void onClick(View v) {
        if(v == restartGameBtn){

        }else if(v == changeWordLengthBtn){

        }else if(v == toMainMenuBtn){
            
        }
    }
}
