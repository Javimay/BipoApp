package com.bipo.javier.bipo.home.activities;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Intent;
import android.content.Context;

import com.bipo.javier.bipo.InsecureArea.InsecureAreaFragment;
import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.fragments.AccountInfoFragment;
import com.bipo.javier.bipo.home.fragments.EventsFragment;
import com.bipo.javier.bipo.home.fragments.SettingsFragment;
import com.bipo.javier.bipo.info.fragments.InfoMenuFragment;
import com.bipo.javier.bipo.login.activities.LoginActivity;
import com.bipo.javier.bipo.report.fragments.PanicButtonFragment;
import com.bipo.javier.bipo.report.fragments.ReportBikesFragment;


public class HomeActivity extends AppCompatActivity
                                        implements NavigationView.OnNavigationItemSelectedListener{

    private SharedPreferences preferences;
    private SharedPreferences settingsPreferences;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private DrawerLayout drawer;
    private Toolbar bipoActionBar;
    //private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        setContentView(R.layout.activity_home);
        preferences = this.getSharedPreferences("UserInfo",0);
        bipoActionBar = (Toolbar)findViewById(R.id.bipoActionBar);
        setSupportActionBar(bipoActionBar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, bipoActionBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkHomeActivity();

    }

    public void checkHomeActivity() {

        String userName = preferences.getString("userName", "");
        settingsPreferences = getSharedPreferences(SettingsFragment.USER_APP_SETTINGS + userName,
                Context.MODE_PRIVATE);
        if(settingsPreferences.getBoolean("Home Panic",false)){

            PanicButtonFragment panicFragment = new PanicButtonFragment();
            ft.replace(R.id.RlyEvents, panicFragment).commit();
        }else{
            initRvEvents();
        }
    }

    /**Inicia el fragmento con el RecyclerView de los eventos del home*/
    private void initRvEvents() {

        ft.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        EventsFragment eventsFragment = new EventsFragment();
        ft.replace(R.id.RlyEvents, eventsFragment).commitAllowingStateLoss();
    }

    public void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void openSettings() {
        SettingsFragment settingsFragment = new SettingsFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.RlyEvents, settingsFragment).addToBackStack(null).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.homeItem:
                goToHomeActivity();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.panicButtonItem:
                goToButtonPanicFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.reportBikesItem:
                goToReportBikesFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.insecureAreasItem:
                goToInsecureAreasFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.accountItem:
                goToAccountFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.infoBipoItem:
                goToInfoBipoFragment();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.logoutItem:
                logoutAccount();
                drawer.closeDrawer(GravityCompat.START);
                return true;

            default:
                //drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
                //return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        //this.menu = menu;
        menu.getItem(0).setIcon(R.mipmap.ic_settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToHomeActivity() {

        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
        intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goToButtonPanicFragment() {
        PanicButtonFragment panicFragment = new PanicButtonFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.RlyEvents, panicFragment).addToBackStack(null).commit();
    }

    private void goToReportBikesFragment() {
        ReportBikesFragment reportFragment = new ReportBikesFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.RlyEvents, reportFragment).addToBackStack(null).commit();
    }

    private void goToInsecureAreasFragment() {
        InsecureAreaFragment insecureFragment = new InsecureAreaFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.RlyEvents, insecureFragment).addToBackStack(null).commit();
    }

    private void goToAccountFragment() {

        AccountInfoFragment accountFragment = new AccountInfoFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.RlyEvents, accountFragment).addToBackStack(null).commit();
    }

    private void goToInfoBipoFragment() {
        InfoMenuFragment infoFragment = new InfoMenuFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.RlyEvents,infoFragment).addToBackStack(null).commit();
    }

    private void logoutAccount() {
        Intent intent = new Intent(this, LoginActivity.class);
        //TODO: Limpar el BackList y las preferencias
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
        intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
