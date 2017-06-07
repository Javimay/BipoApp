package com.bipo.javier.bipo.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bipo.javier.bipo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportBikesFragment extends Fragment {

    private FragmentTabHost tabHost;

    public ReportBikesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_bikes, container, false);

        tabHost = (FragmentTabHost)view.findViewById(R.id.TabHBikes);
        tabHost.setup(getContext(), getFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tabStolen").setIndicator("Robadas"),TabStolen.class,null);
        tabHost.addTab(tabHost.newTabSpec("tabRecovered").setIndicator("Recuperadas"),TabRecovered.class,null);
        tabHost.addTab(tabHost.newTabSpec("tabViews").setIndicator("Vistas"),TabViews.class,null);
        return view;
    }

}
