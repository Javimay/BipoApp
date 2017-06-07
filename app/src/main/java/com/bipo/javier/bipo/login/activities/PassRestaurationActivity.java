package com.bipo.javier.bipo.login.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.models.EmailResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class PassRestaurationActivity extends AppCompatActivity {

    private EditText etEmail;
    private boolean okEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_restauration);

        initComponents();
    }

    private void initComponents() {

        etEmail = (EditText) findViewById(R.id.EtConfirmCorreo);
        okEmail = false;
    }

    private void validateEmail() {
        String email = etEmail.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Introduce tu correo por favor.");
            return;
        }
        char key;
        String letter = null;
        boolean ok = false;
        for (int i = 0; i < email.length(); i++) {

            key = email.charAt(i);
            letter = String.valueOf(key);
            if ((letter.matches("@")) && (!ok)) {
                ok = true;
            }
        }
        if(!ok){
            etEmail.setError("Tu correo debe contener un \"@\"");
            return;
        }
        userExist(email);
        if(!okEmail){
            return;
        }

    }

    private boolean userExist(String email) {

        AccountRepository repo = new AccountRepository(this);
        final EmailResponse emailResponse = new EmailResponse();
        Call<EmailResponse> call = repo.verifyEmail(email);
        call.enqueue(new Callback<EmailResponse>() {
            @Override
            public void onResponse(retrofit.Response<EmailResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {

                    okEmail = false;
                    emailResponse.setMessage(response.message());
                    showMessage("Ocurrió un error en la red.");
                    System.out.println(emailResponse.getMessage());
                }
                if (response != null && response.isSuccess() && response.message() != null) {

                    System.out.println(response.isSuccess());
                    System.out.println(response.message());
                    okEmail = response.body().userExist;
                    if (!response.body().userExist){
                        etEmail.setError("Este correo no está registrado aún en bipo.");
                    }else{
                        //TODO: consumir servicio para restauración de contraseña.
                        showMessage("Validando...");
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {

                okEmail = false;
                System.out.println("onFailure!: " + t);
                emailResponse.setMessage(t.getMessage());
                showMessage("No se pudo establecer la conexión de la red. " +
                        "Verifica que tengas conexión a internet.");
            }
        });
        return okEmail;
    }


    public void passRestauration(View view) {
        validateEmail();
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
