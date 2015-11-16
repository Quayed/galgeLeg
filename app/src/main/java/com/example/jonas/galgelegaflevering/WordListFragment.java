package com.example.jonas.galgelegaflevering;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by jonas on 11/16/15.
 */
public class WordListFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View result = inflater.inflate(R.layout.wordlist_fragment, container, false);

        String[] words = MainActivity.galgeLogik.getMuligeOrd();
        ListView list = (ListView) result.findViewById(R.id.listOfWords);
        list.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, words));

        return result;
    }
}
