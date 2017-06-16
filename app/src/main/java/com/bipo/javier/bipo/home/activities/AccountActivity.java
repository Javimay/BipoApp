package com.bipo.javier.bipo.home.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.RVItemTouchListener;
import com.bipo.javier.bipo.home.fragments.AccountInfoFragment;
import com.bipo.javier.bipo.home.models.Bike;
import com.bipo.javier.bipo.home.models.GetBikesResponse;
import com.bipo.javier.bipo.home.models.GetReportResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.home.models.Report;
import com.bipo.javier.bipo.home.RvBikesAdapter;
import com.bipo.javier.bipo.login.activities.LoginActivity;
import com.bipo.javier.bipo.login.activities.PassRestaurationActivity;
import com.bipo.javier.bipo.login.register.fragments.BikeFragment;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener,
                                                     PopupMenu.OnMenuItemClickListener {

    private ImageButton ibtnLeftButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar bipoActionBar = (Toolbar)findViewById(R.id.bipoActionBar);

        ibtnLeftButton = (ImageButton)findViewById(R.id.ImbLeft);
        setSupportActionBar(bipoActionBar);
        ibtnLeftButton.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AccountInfoFragment accountFragment = new AccountInfoFragment();

        ft.add(R.id.RlLytAccount,accountFragment).commit();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ImbLeft){
            openMenu();
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
