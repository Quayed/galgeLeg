package com.example.jonas.galgelegaflevering;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class HighscoreFragment extends Fragment {

    ListView highscoreList;
    TextView header;
    ProgressBar loading;
    ArrayList<String> highscore;

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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("highscore");
        query.addDescendingOrder("score");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    highscore = new ArrayList<String>();
                    for (int i = 0; i < objects.size(); i++) {
                        highscore.add("" + (i+1) + ". " + objects.get(i).getString("name") + ": " + objects.get(i).getInt("score"));
                    }
                    highscoreList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.custom_list, highscore));

                    highscoreList.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                } else {
                    e.printStackTrace();
                }
            }
        });

        return  layout;
    }


}
