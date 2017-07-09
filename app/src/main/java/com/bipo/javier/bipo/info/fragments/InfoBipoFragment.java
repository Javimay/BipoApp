package com.bipo.javier.bipo.info.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bipo.javier.bipo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoBipoFragment extends Fragment implements View.OnClickListener{

    FragmentManager fm ;
    FragmentTransaction ft;

    public InfoBipoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_bipo, container, false);
        TextView tvInfo = (TextView)view.findViewById(R.id.TvBipoInfo);
        TextView tvTutorial = (TextView)view.findViewById(R.id.TvTutorial);
        tvInfo.setOnClickListener(this);
        tvTutorial.setOnClickListener(this);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.TvBipoInfo){

            BipoInfoFragment infoFragment = new BipoInfoFragment();
            ft.replace(R.id.RlyEvents,infoFragment).commit();
        }
        if(v.getId() == R.id.TvTutorial){

            BipoTutorialFragment tutorialFragment = new BipoTutorialFragment();
            ft.replace(R.id.RlyEvents,tutorialFragment).commit();
        }
    }

}
