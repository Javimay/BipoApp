package com.bipo.javier.bipo.info.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bipo.javier.bipo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class BipoInfoFragment extends Fragment {

    private long tiempoPrimerClick;
    private static final int INTERVALO = 7000; //7 segundos
    private int clicks = 0;
    private ImageView bipoLogo, jarcidcoLogo;
    private Animation animIn, animOut, animFull;
    private TextView tvInfoBipo;

    public BipoInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bipo_info, container, false);
        TextView tvVersion = (TextView)view.findViewById(R.id.TvBipoVersion);
        bipoLogo = (ImageView)view.findViewById(R.id.ImgVBipoLogo);
        tvInfoBipo = (TextView)view.findViewById(R.id.TvInfoBipo);
        tvInfoBipo.setMovementMethod(new ScrollingMovementMethod());
        tvInfoBipo.setMovementMethod(LinkMovementMethod.getInstance());
        //TODO: Cambiar link de los terminos y condiciones en el archivo strings.
        jarcidcoLogo = (ImageView)view.findViewById(R.id.ImgVJarcidco);
        animIn = AnimationUtils.loadAnimation(getContext(),R.anim.anim_turn_in);
        animIn.setFillAfter(true);
        animIn.setDuration(2000);
        animOut = AnimationUtils.loadAnimation(getContext(),R.anim.anim_turn_out);
        animOut.setFillAfter(true);
        animOut.setDuration(2000);
        animFull = AnimationUtils.loadAnimation(getContext(),R.anim.anim_turn_full);
        animFull.setFillAfter(true);
        bipoLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks+=1;
                if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
                    if (clicks == 9){

                            bipoLogo.startAnimation(animIn);
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    jarcidcoGo();
                                }
                             };
                            Timer timer = new Timer();
                            long splashTime = 2000;
                            timer.schedule(task, splashTime);
                    }
                    return;
                }else {
                    clicks = 0;
                }
                tiempoPrimerClick = System.currentTimeMillis();
            }
        });
        android.content.res.Resources resources = getResources();
        String bipoVersion = String.format(resources.getString(R.string.sr_txt_bipo_version),
                "1.01");
        tvVersion.setText(bipoVersion);
        return view;
    }

    private void jarcidcoGo() {

        bipoLogo.getAnimation().cancel();
        jarcidcoLogo.setAnimation(animFull);
        jarcidcoLogo.getAnimation().start();

        bipoLogo.setAnimation(animOut);
        animOut.setStartOffset(5000);
        bipoLogo.getAnimation().start();
        clicks = 0;
    }
}
