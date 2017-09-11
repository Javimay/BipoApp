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
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.activities.HomeActivity;
import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.models.UserResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private Switch swHomePanic, swEmailNotif;
    private final static String TAG = SettingsFragment.class.getName();
    private SharedPreferences preferences;
    private String userName;
    public final static String USER_APP_SETTINGS = TAG + ".USER_APP_SETTINGS_";
    private Menu menu;
    private final int PHOTO_PUBLICATE = 1;
    private SharedPreferences settingsPreferences;

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
        swEmailNotif = (Switch) view.findViewById(R.id.SwEmailNotif);
        settingsPreferences = getActivity().getSharedPreferences(USER_APP_SETTINGS +
                userName, 0);
        if (!settingsPreferences.getAll().isEmpty()) {

            swHomePanic.setChecked(settingsPreferences.getBoolean("Home Panic", false));
            if (settingsPreferences.getInt("Email Notif", 0) == 0){
                swEmailNotif.setChecked(false);
            }else{
                swEmailNotif.setChecked(true);
            }
        }
        setHasOptionsMenu(true);

        return view;
    }

    private void saveAccountSettings() {

        SharedPreferences.Editor editorSettings = settingsPreferences.edit();
        editorSettings.putBoolean("Home Panic", swHomePanic.isChecked());
        if (swEmailNotif.isChecked()){
            editorSettings.putInt("Email Notif", 1);
        }else{
            editorSettings.putInt("Email Notif", 0);
        }
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
            savePreferences();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void savePreferences() {

        String token = preferences.getString("token", "");
        int emailReceiver = 0;
        if (swEmailNotif.isChecked()){
            emailReceiver = 1;
        }
        AccountRepository repo = new AccountRepository(getContext());
        Call<UserResponse> call = repo.savePreferences(token,emailReceiver,PHOTO_PUBLICATE,0,0);
        final UserResponse userResponse = new UserResponse();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(retrofit.Response<UserResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    if (response.code() == 400) {
                        showMessage("Correo o contraseña incorrecta.");
                        System.out.println(response.isSuccess());
                        System.out.println(response.message());
                        System.out.println(response.code());
                        userResponse.setMessage(response.message());
                    } else {
                        showMessage("Ocurrió un error en la red.");
                        System.out.println(userResponse.getMessage());
                    }
                }
                if (response != null && response.isSuccess() && response.message() != null) {
                    System.out.println("Preferencias guardadas.");
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                userResponse.setMessage(t.getMessage());
                showMessage("No se pudo establecer la conexión de la red. " +
                        "Verifica que tengas conexión a internet.");
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
