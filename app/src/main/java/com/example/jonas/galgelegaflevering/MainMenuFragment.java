package com.example.jonas.galgelegaflevering;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by jonas on 11/16/15.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {
    Button gameButton;
    Button rulesButton;
    Button highscoreButton;
    Button wordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.menu_layout, container, false);

        gameButton = (Button) layout.findViewById(R.id.gameButton);
        rulesButton = (Button) layout.findViewById(R.id.rulesButton);
        highscoreButton = (Button) layout.findViewById(R.id.highscore);
        wordButton = (Button) layout.findViewById(R.id.wordButton);

        gameButton.setOnClickListener(this);
        rulesButton.setOnClickListener(this);
        highscoreButton.setOnClickListener(this);
        wordButton.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View v) {
        if (v == gameButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if(MainActivity.galgeLogik.isWordsUpdated()) {
                if (MainActivity.galgeLogik.getPossibleLengths() != null) {
                    builder.setTitle("Vælg den ønskede ord længde");
                    builder.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, MainActivity.galgeLogik.getPossibleLengths()),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.galgeLogik.setWordLength(MainActivity.galgeLogik.getPossibleLengths().get(which));
                                    Intent i = new Intent(getActivity(), GameActivity.class);
                                    startActivity(i);
                                }
                            });
                    builder.create().show();
                } else {
                    Intent i = new Intent(getActivity(), GameActivity.class);
                    startActivity(i);
                }
            } else{
                builder.setTitle("Ord ikke opdateret");
                builder.setMessage("Ordene er ikke blevet opdateret - er du sikker på at du vil spille med de lokale ord?");
                builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(), GameActivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("Nej", null);
                builder.create().show();
            }


        } else if (v == rulesButton) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new RulesFragment())
                    .addToBackStack(null)
                    .commit();
        } else if(v == highscoreButton){
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new HighscoreFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (v == wordButton) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new WordListFragment())
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getActivity(), "Denne knap er ikke implementeret endnu", Toast.LENGTH_LONG).show();
        }
    }
}
