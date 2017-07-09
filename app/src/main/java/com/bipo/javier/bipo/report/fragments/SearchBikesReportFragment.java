package com.bipo.javier.bipo.report.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bipo.javier.bipo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBikesReportFragment extends Fragment implements View.OnClickListener {


    public SearchBikesReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_bikes_report, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BtnSearchBikes:
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                FindBikesFragment findBikesFragment = new FindBikesFragment();
                ft.replace(R.id.RlyEvents,findBikesFragment).commit();
                break;
        }
    }
}
