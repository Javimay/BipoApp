package com.bipo.javier.bipo.home.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.activities.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private Switch swHomePanic, swPhotos, swEmailNotif;
    private final static String TAG = SettingsFragment.class.getName();
    private SharedPreferences preferences;
    private String userName;
    public final static String USER_APP_SETTINGS = TAG + ".USER_APP_SETTINGS_";
    private Menu menu;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        preferences = getActivity().getSharedPreferences("UserInfo", 0);
        userName = preferences.getString("userName", "");
        swHomePanic = (Switch) view.findViewById(R.id.SwHomePanic);
        swPhotos = (Switch) view.findViewById(R.id.SwPhotos);
        swEmailNotif = (Switch) view.findViewById(R.id.SwEmailNotif);
        SharedPreferences settingsPreferences = getActivity().getSharedPreferences(USER_APP_SETTINGS +
                userName, 0);
        if (!settingsPreferences.getAll().isEmpty()) {

            swHomePanic.setChecked(settingsPreferences.getBoolean("Home Panic", false));
            swPhotos.setChecked(settingsPreferences.getBoolean("Photos", false));
            swEmailNotif.setChecked(settingsPreferences.getBoolean("Email Notif", false));
        }
        setHasOptionsMenu(true);

        return view;
    }

    private void saveAccountSettings() {

        SharedPreferences settingsPreferences = getActivity().getSharedPreferences(USER_APP_SETTINGS +
                userName, 0);
        SharedPreferences.Editor editorSettings = settingsPreferences.edit();
        editorSettings.putBoolean("Home Panic", swHomePanic.isChecked());
        editorSettings.putBoolean("Photos", swPhotos.isChecked());
        editorSettings.putBoolean("Email Notif", swEmailNotif.isChecked());
        editorSettings.apply();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save_settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_settings) {

            saveAccountSettings();
            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.onDestroy();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
