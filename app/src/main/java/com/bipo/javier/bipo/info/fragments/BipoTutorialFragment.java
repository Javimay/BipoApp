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

    private ImageView t1,t2,t3,t4,t5,t6,t7,t8,t9,t9_1,t10,t11,t12,t13;
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
        t3 = new ImageView(getContext());
        t3.setImageResource(R.drawable.tuto_3);
        t4 = new ImageView(getContext());
        t4.setImageResource(R.drawable.tuto_4);
        t5 = new ImageView(getContext());
        t5.setImageResource(R.drawable.tuto_5);
        t6 = new ImageView(getContext());
        t6.setImageResource(R.drawable.tuto_6);
        t7 = new ImageView(getContext());
        t7.setImageResource(R.drawable.tuto_7);
        t8 = new ImageView(getContext());
        t8.setImageResource(R.drawable.tuto_8);
        t9 = new ImageView(getContext());
        t9.setImageResource(R.drawable.tuto_9);
        t9_1 = new ImageView(getContext());
        t9_1.setImageResource(R.drawable.tuto_91);
        t10 = new ImageView(getContext());
        t10.setImageResource(R.drawable.tuto_10);
        t11 = new ImageView(getContext());
        t11.setImageResource(R.drawable.tuto_11);
        t12 = new ImageView(getContext());
        t12.setImageResource(R.drawable.tuto_12);
        t13 = new ImageView(getContext());
        t13.setImageResource(R.drawable.tuto_13);
        vfTuto.addView(t1);
        vfTuto.addView(t2);
        vfTuto.addView(t3);
        vfTuto.addView(t4);
        vfTuto.addView(t5);
        vfTuto.addView(t6);
        vfTuto.addView(t7);
        vfTuto.addView(t8);
        vfTuto.addView(t9);
        vfTuto.addView(t9_1);
        vfTuto.addView(t10);
        vfTuto.addView(t11);
        vfTuto.addView(t12);
        vfTuto.addView(t13);
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
