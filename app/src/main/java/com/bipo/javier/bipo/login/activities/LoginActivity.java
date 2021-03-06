package com.bipo.javier.bipo.login.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.activities.HomeActivity;
import com.bipo.javier.bipo.home.fragments.SettingsFragment;
import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.models.User;
import com.bipo.javier.bipo.login.models.LoginResponse;
import com.bipo.javier.bipo.login.register.activities.RegisterActivity;
import com.bipo.javier.bipo.login.utilities.Teclado;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private final static String TAG = SettingsFragment.class.getName();
    public final static String USER_APP_SETTINGS = TAG + ".USER_APP_SETTINGS_";
    private final int loggedApp = 1;
    private final int loggedWeb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
    }

    private void initComponents() {
        etEmail = (EditText) findViewById(R.id.EtEmail);
        etPassword = (EditText) findViewById(R.id.EtPassword);
    }

    public void userRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void validateFields() {

        Teclado.ocultarTeclado(this);
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Escribe tu correo por favor.");
            return;
        }
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Escribe una contraseña por favor.");
            return;
        }

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        getInHome(email, password);
    }

    private void getInHome(String user, String password) {

        AccountRepository repo = new AccountRepository(this);
        Call<LoginResponse> call = repo.login(user, password,loggedWeb,loggedApp);
        final LoginResponse loginResponse = new LoginResponse();
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(retrofit.Response<LoginResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    if (response.code() == 400) {
                        showMessage("Correo o contraseña incorrecta.");
                        System.out.println(response.isSuccess());
                        System.out.println(response.message());
                        System.out.println(response.code());
                        loginResponse.setMessage(response.message());
                    } else {
                        showMessage("Ocurrió un error en la red.");
                        System.out.println(loginResponse.getMessage());
                    }
                }
                if (response != null && response.isSuccess() && response.message() != null) {
                    String name = "", lastName = "", email = "", birthday = "", phone = "",
                            documentid = "", userName = "", token = "", nickName = "";
                    loginResponse.setUser(response.body().getUser());
                    ArrayList<User> userList = loginResponse.getUser();
                    int emailReceiver = 0, photoPublication = 0,
                            enableReportUbication = 0, enableLocationUbication = 0;
                    if (userList != null) {

                        for (User user : userList) {
                            name = user.getName();
                            lastName = user.getLastname();
                            email = user.getEmail();
                            nickName = user.getNickname();
                            birthday = user.getBirthdate();
                            phone = user.getCellphone();
                            documentid = user.getDocumentid();
                            userName = user.getNickname();
                            token = user.getToken();
                            emailReceiver = user.getEmailReceiver();
                            photoPublication = user.getPhotoPublication();
                            enableReportUbication = user.getEnableReportUbication();
                            enableLocationUbication = user.getEnableLocationUbication();

                            System.out.println("\nNombre: " + user.getName() +
                                    "\nApellido: " + user.getLastname() +
                                    "\nCorreo: " + user.getEmail() +
                                    "\nNacimiento: " + user.getBirthdate() +
                                    "\nCelular: " + user.getCellphone() +
                                    "\nCedula: " + user.getDocumentid() +
                                    "\nNickname: " + user.getNickname() +
                                    "\nToken: " + user.getToken());
                        }
                        SharedPreferences preferences = getSharedPreferences("UserInfo", 0);
                        SharedPreferences settingsPreferences = getSharedPreferences(USER_APP_SETTINGS +
                                userName, 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("name", name);
                        editor.putString("lastName", lastName);
                        editor.putString("email", email);
                        editor.putString("nickName", nickName);
                        editor.putString("birthdate", birthday);
                        editor.putString("phone", phone);
                        editor.putString("documentId", documentid);
                        editor.putString("userName", userName);
                        editor.putString("token", token);
                        editor.apply();
                        SharedPreferences.Editor editorSettings = settingsPreferences.edit();
                        editorSettings.putInt("Email Notif", emailReceiver);
                        editorSettings.putInt("Photo Public", photoPublication);
                        editorSettings.putInt("Report Ubic", enableReportUbication);
                        editorSettings.putInt("Location Ubic", enableLocationUbication);
                        editorSettings.apply();
                        goToHomeActivity();
                        wellcomeUser(name, lastName);
                    }else{
                        Log.e("getInHome", "Response.body empty...");
                        showMessage("Problemas en el servidor, "+
                                "\ncontácte al administrador.");

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                loginResponse.setMessage(t.getMessage());
                showMessage("No se pudo establecer la conexión de la red. " +
                        "Verifica que tengas conexión a internet.");
            }
        });

    }

    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    /**
     * Método que ejecuta el servicio de logeo
     */
    public void getIn(View view) {
        validateFields();
    }

    public void goToPassRestauration(View view) {
        Intent intent = new Intent(this, PassRestaurationActivity.class);
        startActivity(intent);
    }

    private void wellcomeUser(String name, String lastName) {

        showMessage("Bienvenido " + name + " " + lastName);
    }
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
