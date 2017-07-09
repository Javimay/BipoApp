package com.bipo.javier.bipo.account.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.fragments.AccountInfoFragment;
import com.bipo.javier.bipo.home.activities.HomeActivity;
import com.bipo.javier.bipo.info.activities.InfoBipoActivity;
import com.bipo.javier.bipo.InsecureArea.InsecureAreaActivity;
import com.bipo.javier.bipo.login.activities.LoginActivity;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar bipoActionBar = (Toolbar)findViewById(R.id.bipoActionBar);

        setSupportActionBar(bipoActionBar);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AccountInfoFragment accountFragment = new AccountInfoFragment();

        ft.add(R.id.RlLytAccount,accountFragment).commit();
    }

    @Override
    public void onClick(View v) {

    }

   /* @Override
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
    }*/

    public void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
