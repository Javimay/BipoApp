package com.bipo.javier.bipo.home.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.login.activities.LoginActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {

    private ImageButton ibtnLeftButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar bipoActionBar = (Toolbar)findViewById(R.id.bipoActionBar);

        ImageButton ibtnRigthButton = (ImageButton)findViewById(R.id.ImbRight);
        ibtnRigthButton.setImageResource(R.mipmap.ic_back_arrow);
        //ibtnRigthButton.setImageResource(R.mipmap.ic_settings);
        ibtnLeftButton = (ImageButton)findViewById(R.id.ImbLeft);
        setSupportActionBar(bipoActionBar);
        ibtnLeftButton.setOnClickListener(this);
        ibtnRigthButton.setOnClickListener(this);
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Toolbar bipoActionBar = (Toolbar)findViewById(R.id.bipoActionBar);

        ImageButton ibtnRigthButton = (ImageButton)findViewById(R.id.ImbRight);
        //ibtnRigthButton.setImageResource(R.mipmap.ic_left_arrow);
        //ibtnRigthButton.setImageResource(R.mipmap.ic_settings);
        ibtnLeftButton = (ImageButton)findViewById(R.id.ImbLeft);
        setSupportActionBar(bipoActionBar);
        //ibtnLeftButton.setOnClickListener(this);
        //ibtnRigthButton.setOnClickListener(this);

        return super.onPrepareOptionsMenu(menu);
    }*/


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ImbLeft){
            openMenu();
        }
        if (v.getId() == R.id.ImbRight){
            this.onBackPressed();
        }
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
            case R.id.panicButtonItem:
                goToButtonPanicActivity();
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

    private void goToButtonPanicActivity() {
        Intent intent = new Intent(this, PanicButtonActivity.class);
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
}
