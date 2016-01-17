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
    Button wordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.menu_layout, container, false);

        gameButton = (Button) result.findViewById(R.id.gameButton);
        rulesButton = (Button) result.findViewById(R.id.rulesButton);
        wordButton = (Button) result.findViewById(R.id.wordButton);

        gameButton.setOnClickListener(this);
        rulesButton.setOnClickListener(this);
        wordButton.setOnClickListener(this);

        return result;
    }

    @Override
    public void onClick(View v) {
        if (v == gameButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Vælg den ønskede ord længde");
            // TODO HANDLE NULL POINTER
            if (MainActivity.galgeLogik.getPossibleLengths() != null) {
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


        } else if (v == rulesButton) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new RulesFragment())
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
