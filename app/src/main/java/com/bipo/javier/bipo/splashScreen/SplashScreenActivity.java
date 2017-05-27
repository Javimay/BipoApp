package com.bipo.javier.bipo.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.login.activities.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ImageView logo = (ImageView) findViewById(R.id.IvLogo);
        //logo.setImageResource(R.drawable.);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_presentacion);
        anim.setFillAfter(true);
        logo.startAnimation(anim);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {


                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_zoomforward_in, R.anim.anim_zoomforward_out);
                finish();
            }

        };
        Timer timer = new Timer();
        long splashTime = 4000;
        timer.schedule(task, splashTime);

    }
}
