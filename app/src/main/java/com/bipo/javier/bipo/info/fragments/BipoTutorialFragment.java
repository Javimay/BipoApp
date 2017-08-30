package com.bipo.javier.bipo.info.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bipo.javier.bipo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BipoTutorialFragment extends Fragment implements View.OnClickListener {

    private ImageView t1,t2;
    private ImageButton imgBtnPrevious, imgBtnNext;
    private ViewFlipper vfTuto;

    public BipoTutorialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bipo_tutorial, container, false);
        vfTuto = (ViewFlipper)view.findViewById(R.id.VfTutorial);
        imgBtnPrevious = (ImageButton)view.findViewById(R.id.ImgBtnPrevTuto);
        imgBtnPrevious.setOnClickListener(this);
        imgBtnNext = (ImageButton)view.findViewById(R.id.ImgBtnNextTuto);
        imgBtnNext.setOnClickListener(this);
        t1 = new ImageView(getContext());
        t1.setImageResource(R.drawable.tuto_1);
        t2 = new ImageView(getContext());
        t2.setImageResource(R.drawable.tuto_2);
        vfTuto.addView(t1);
        vfTuto.addView(t2);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ImgBtnPrevTuto){
            previousTuto();
        }

        if(v.getId() == R.id.ImgBtnNextTuto){
            nextTuto();
        }
    }

    private void nextTuto() {
        final Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        vfTuto.setInAnimation(in);
        vfTuto.showNext();
    }

    private void previousTuto() {
        final Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        vfTuto.setInAnimation(out);
        vfTuto.showPrevious();
    }


}
