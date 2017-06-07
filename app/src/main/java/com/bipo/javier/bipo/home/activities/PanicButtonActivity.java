package com.bipo.javier.bipo.home.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.login.activities.LoginActivity;

public class PanicButtonActivity extends AppCompatActivity implements View.OnClickListener,
                                                            PopupMenu.OnMenuItemClickListener{

    private ImageButton ibtnLeftButton;
    private ImageButton ibtnRigthButton;
    private ProgressBar progressBar;
    private ImageButton imgBtnPanicButton;
    private Runnable rotationRunnable = null;
    private ObjectAnimator anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_button);
        Toolbar bipoActionBar = (Toolbar)findViewById(R.id.bipoActionBar);
        ibtnRigthButton = (ImageButton)findViewById(R.id.ImbRight);
        ibtnRigthButton.setImageResource(R.mipmap.ic_settings);
        ibtnLeftButton = (ImageButton)findViewById(R.id.ImbLeft);
        setSupportActionBar(bipoActionBar);
        ibtnLeftButton.setOnClickListener(this);
        ibtnRigthButton.setOnClickListener(this);
        initComponets();

    }

    private void initComponets() {

        imgBtnPanicButton = (ImageButton) findViewById(R.id.ImgBtnPanicButton);
        progressBar = (ProgressBar) findViewById(R.id.PgBarPanicButton);
        imgBtnPanicButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
                    anim.setDuration(4000);
                    anim.setInterpolator(new DecelerateInterpolator());
                    anim.start();

                    v.post(rotationRunnable);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (progressBar.getProgress() == 100) {

                        progressBar.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.ok_circular_progress_bar));
                        anim = ObjectAnimator.ofInt(progressBar, "progress", 99, 100);
                        anim.start();

                        sendReport();
                    } else {
                        anim.cancel();
                        anim = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), 0);
                        anim.setDuration(2000);
                        anim.setInterpolator(new DecelerateInterpolator());
                        anim.start();
                    }
                }
                return true;
            }
        });
    }

    private void sendReport() {

        showMessage("Creando Reporte.");
        //TODO: Des comentar para cambiar el color de la ProgressBar al finalizar el servicio.
        /*progressBar.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.circular_progress_bar));*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ImbLeft){
            openMenu();
        }
        if (v.getId() == R.id.ImbRight){
            openSettings();
        }
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openMenu() {

        PopupMenu popupMenu = new PopupMenu(this, ibtnLeftButton);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.getMenuInflater().inflate(R.menu.menu_items,popupMenu.getMenu());
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()){

            case R.id.homeItem:
                goToHomeActivity();
                return true;

            case R.id.reportBikesItem:
                goToReportBikesActivity();
                return true;

            case R.id.insecureAreasItem:
                goToInsecureAreasActivity();
                return true;

            case R.id.accountItem:
                goToAccountActivity();
                return true;

            case R.id.infoBipoItem:
                goToInfoBipoActivity();
                return true;

            case R.id.logoutItem:
                logoutAccount();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void goToReportBikesActivity() {
        Intent intent = new Intent(this, ReportBikesActivity.class);
        startActivity(intent);
    }

    private void goToInsecureAreasActivity() {
        Intent intent = new Intent(this, InsecureAreaActivity.class);
        startActivity(intent);
    }

    private void goToAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    private void goToInfoBipoActivity() {
        Intent intent = new Intent(this, InfoBipoActivity.class);
        startActivity(intent);
    }

    private void logoutAccount() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
