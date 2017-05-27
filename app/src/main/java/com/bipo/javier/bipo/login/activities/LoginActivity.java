package com.bipo.javier.bipo.login.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.activities.HomeActivity;
import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.models.User;
import com.bipo.javier.bipo.login.models.LoginResponse;
import com.bipo.javier.bipo.login.register.RegisterActivity;
import com.bipo.javier.bipo.login.utilities.Teclado;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private String email, password;

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

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        showMessage("Verificando...");
        getInHome(email, password);
    }

    private void getInHome(String user, String password) {

        AccountRepository repo = new AccountRepository(this);
        Call<LoginResponse> call = repo.login(user, password);
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
                    String name = "", lastName = "", email = "", birthday = "", phone = "", documentid = "", token = "";
                    ArrayList<User> userList = response.body().getUser();
                    if (!response.body().getUser().isEmpty()) {

                        for (User user : userList) {
                            name = user.getName();
                            lastName = user.getLastname();
                            email = user.getEmail();
                            birthday = user.getBirthdate();
                            phone = user.getCellphone();
                            documentid = user.getDocumentid();
                            token = user.getToken();

                            System.out.println("\nNombre: " + user.getName() +
                                    "\nApellido: " + user.getLastname() +
                                    "\nCorreo: " + user.getEmail() +
                                    "\nNacimiento: " + user.getBirthdate() +
                                    "\nCelular: " + user.getCellphone() +
                                    "\nCedula: " + user.getDocumentid() +
                                    "\nNickname: " + user.getNickname() +
                                    "\nToken: " + user.getToken());
                        }
                        goToHomeActivity(name, lastName, email, birthday, phone, documentid, token);
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

    private void goToHomeActivity(String name, String lastName, String email, String birthday, String phone,
                                  String documentId, String token) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("lastName", lastName);
        intent.putExtra("email", email);
        intent.putExtra("birthdate", birthday);
        intent.putExtra("phone", phone);
        intent.putExtra("documentId", documentId);
        intent.putExtra("token", token);

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

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
