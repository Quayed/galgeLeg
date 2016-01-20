package com.example.jonas.galgelegaflevering;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class HighscoreFragment extends Fragment implements HighscoreSubscriber {

    ListView highscoreList;
    TextView header;
    ProgressBar loading;

    public HighscoreFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_high_score, container, false);

        header = (TextView) layout.findViewById(R.id.header);
        highscoreList = (ListView) layout.findViewById(R.id.highscoreList);
        loading = (ProgressBar) layout.findViewById(R.id.loading);

        Galgelogik.getInstance().subscribeToHighscore(this);
        Galgelogik.getInstance().updateHighscore();

        return  layout;
    }


    @Override
    public void onHighscoreUpdate(List<ParseObject> highscore) {
        ArrayList<String> highscores = new ArrayList<String>();
        for (int i = 0; i < highscore.size(); i++) {
            highscores.add("" + (i+1) + ". " + highscore.get(i).getString("name") + ": " + highscore.get(i).getInt("score"));
        }
        if(getActivity() != null){
            highscoreList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.custom_list, highscores));

            highscoreList.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        }

    }
}
