package com.bipo.javier.bipo.home.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.fragments.EventsFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar bipoActionBar = (Toolbar)findViewById(R.id.bipoActionBar);
        ImageView ivRigthButton = (ImageView)findViewById(R.id.ImbRight);
        ivRigthButton.setImageResource(R.mipmap.ic_settings);
        setSupportActionBar(bipoActionBar);

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
}
