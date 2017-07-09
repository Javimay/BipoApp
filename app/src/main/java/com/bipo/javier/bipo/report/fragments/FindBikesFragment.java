package com.bipo.javier.bipo.report.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bipo.javier.bipo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindBikesFragment extends Fragment {


    public FindBikesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_bikes, container, false);
    }

}
