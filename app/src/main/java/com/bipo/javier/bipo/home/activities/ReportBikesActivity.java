package com.bipo.javier.bipo.home.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.fragments.EventItemsFragment;
import com.bipo.javier.bipo.home.fragments.ReportBikesFragment;
import com.bipo.javier.bipo.home.fragments.TabRecovered;
import com.bipo.javier.bipo.home.fragments.TabStolen;
import com.bipo.javier.bipo.home.fragments.TabViews;
import com.bipo.javier.bipo.login.activities.LoginActivity;

public class ReportBikesActivity extends AppCompatActivity implements View.OnClickListener,
                                                        PopupMenu.OnMenuItemClickListener{

    private ImageButton ibtnLeftButton;
    private ImageButton ibtnRigthButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bikes);
        Toolbar bipoActionBar = (Toolbar)findViewById(R.id.bipoActionBar);
        ibtnRigthButton = (ImageButton)findViewById(R.id.ImbRight);
        ibtnRigthButton.setImageResource(R.mipmap.ic_settings); //TODO: Icono lupa.
        ibtnLeftButton = (ImageButton)findViewById(R.id.ImbLeft);
        setSupportActionBar(bipoActionBar);
        ibtnLeftButton.setOnClickListener(this);
        ibtnRigthButton.setOnClickListener(this);

        initComponents();

    }

    private void initComponents() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ReportBikesFragment itemsFragment = new ReportBikesFragment();
        ft.add(R.id.RlyBikesReports,itemsFragment).commit();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){

            case R.id.homeItem:
                goToHomeActivity();
                return true;

            case R.id.panicButtonItem:
                goToButtonPanicActivity();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ImbLeft){
            openMenu();
        }
        if (v.getId() == R.id.ImbRight){
            //openSettings();
        }
    }

    private void openMenu() {

        PopupMenu popupMenu = new PopupMenu(this, ibtnLeftButton);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.getMenuInflater().inflate(R.menu.menu_items,popupMenu.getMenu());
        popupMenu.show();
    }

    private void goToButtonPanicActivity() {
        Intent intent = new Intent(this, PanicButtonActivity.class);
        startActivity(intent);
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
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
