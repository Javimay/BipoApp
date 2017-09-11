package com.bipo.javier.bipo.login.activities;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.models.BikesResponse;
import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.models.EmailResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class PassRestaurationActivity extends AppCompatActivity {

    private EditText etEmail;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_restauration);

        initComponents();
    }

    private void initComponents() {

        etEmail = (EditText) findViewById(R.id.EtConfirmCorreo);
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
        //recoveryPass(email);
        /*if(!okEmail){
            return;
        }*/

    }

    private void userExist(final String email) {

        AccountRepository repo = new AccountRepository(this);
        final EmailResponse emailResponse = new EmailResponse();
        Call<EmailResponse> call = repo.verifyEmail(email);
        call.enqueue(new Callback<EmailResponse>() {
            @Override
            public void onResponse(retrofit.Response<EmailResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {

                    emailResponse.setMessage(response.message());
                    showMessage("Ocurrió un error en la red.");
                    System.out.println(emailResponse.getMessage());
                }
                if (response != null && response.isSuccess() && response.message() != null) {

                    System.out.println(response.isSuccess());
                    System.out.println(response.message());

                    if (response.body().getUserExist().size() == 0){
                        etEmail.setError("Este correo no está registrado aún en bipo.");
                    }else{
                        recoveryPass(email);
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                emailResponse.setMessage(t.getMessage());
                showMessage("No se pudo establecer la conexión de la red. " +
                        "Verifica que tengas conexión a internet.");
            }
        });
    }

    private void recoveryPass(String email) {

        AccountRepository repo = new AccountRepository(this);
        Call<BikesResponse> call = repo.recoveryPass(email);
        final BikesResponse bikesResponse = new BikesResponse();
        call.enqueue(new Callback<BikesResponse>() {
            @Override
            public void onResponse(retrofit.Response<BikesResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {

                    bikesResponse.setMessage(response.message());
                    showMessage("Ocurrió un error en la red.");
                    System.out.println(bikesResponse.getMessage());
                }
                if (response != null && response.isSuccess() && response.message() != null) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Restaurando tu contraseña.");
                    alert.setMessage("Acabamos de enviarte un correo electrónico con las intrucciones" +
                            " para restaurar tu contraseña." +
                                "\nCuando termines de restaurar tu contraseña podrás iniciar sesión.")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                        }
                    });
                    alert.show();
                }
            }
            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                bikesResponse.setMessage(t.getMessage());
                showMessage("No se pudo establecer la conexión de la red. " +
                        "Verifica que tengas conexión a internet.");
            }
        });
    }


    public void passRestauration(View view) {
        validateEmail();
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
