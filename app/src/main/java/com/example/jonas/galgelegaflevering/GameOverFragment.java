package com.example.jonas.galgelegaflevering;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        if(!Galgelogik.getInstance().isWordsUpdated())
            changeWordLengthBtn.setVisibility(View.GONE);

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
            ((GameOverActivity) getActivity()).returnToGame(true);
        }else if(v == changeWordLengthBtn){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if (Galgelogik.getInstance().getPossibleLengths() != null) {
                    builder.setTitle("Vælg den ønskede ord længde");
                    builder.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Galgelogik.getInstance().getPossibleLengths()),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Galgelogik.getInstance().setWordLength(Galgelogik.getInstance().getPossibleLengths().get(which));
                                    ((GameOverActivity) getActivity()).returnToGame(true);
                                }
                            });
                    builder.create().show();
                }
        }else if(v == toMainMenuBtn){
            ((GameOverActivity) getActivity()).returnToGame(false);
        }
    }
}
