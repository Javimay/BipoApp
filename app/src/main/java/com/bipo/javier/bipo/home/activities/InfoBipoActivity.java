package com.bipo.javier.bipo.home.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.fragments.BipoInfoFragment;
import com.bipo.javier.bipo.home.fragments.BipoTutorialFragment;

public class InfoBipoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_bipo);

        TextView tvInfo = (TextView)findViewById(R.id.TvBipoInfo);
        TextView tvTutorial = (TextView)findViewById(R.id.TvTutorial);
        tvInfo.setOnClickListener(this);
        tvTutorial.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.TvBipoInfo){

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            BipoInfoFragment infoFragment = new BipoInfoFragment();
            ft.replace(R.id.RlyInfoContainer,infoFragment).commit();
        }
        if(v.getId() == R.id.TvTutorial){

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            BipoTutorialFragment tutorialFragment = new BipoTutorialFragment();
            ft.add(R.id.RlyInfoContainer,tutorialFragment).commit();
        }
    }
}
