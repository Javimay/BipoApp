package com.bipo.javier.bipo.home.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.fragments.EventsFragment;
import com.bipo.javier.bipo.login.activities.LoginActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener,
                                                    PopupMenu.OnMenuItemClickListener {

    private ImageButton ibtnLeftButton;
    private ImageButton ibtnRigthButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar bipoActionBar = (Toolbar)findViewById(R.id.bipoActionBar);
        ibtnRigthButton = (ImageButton)findViewById(R.id.ImbRight);
        ibtnRigthButton.setImageResource(R.mipmap.ic_settings);
        ibtnLeftButton = (ImageButton)findViewById(R.id.ImbLeft);
        setSupportActionBar(bipoActionBar);
        ibtnLeftButton.setOnClickListener(this);
        ibtnRigthButton.setOnClickListener(this);
        wellcomeUser();
        initRvEvents();

    }

    private void wellcomeUser() {
        String name = getIntent().getExtras().getString("name");
        String lastName = getIntent().getExtras().getString("lastName");
        showMessage("Bienvenido " + name + " " + lastName);
    }

    /**Inicia el fragmento con el RecyclerView de los eventos del home*/
    private void initRvEvents() {
        Bundle bundle = getIntent().getExtras();
        /*String name = bundle.getString("name");
        String lastName = bundle.getString("lastName");
        String email = bundle.getString("email");
        String birthdate = bundle.getString("birthdate");
        String phone = bundle.getString("phone");
        String documentId = bundle.getString("documentId");
        String token = bundle.getString("token");*/
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EventsFragment eventsFragment = new EventsFragment();
        eventsFragment.setArguments(bundle);
        ft.add(R.id.RlyEvents, eventsFragment).commit();
    }

    public void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
