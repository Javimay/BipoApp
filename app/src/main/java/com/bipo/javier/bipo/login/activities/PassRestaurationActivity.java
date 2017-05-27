package com.bipo.javier.bipo.login.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bipo.javier.bipo.R;

public class PassRestaurationActivity extends AppCompatActivity {

    private EditText etEmail;

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
    }

    public void passRestauration(View view) {
        validateEmail();
    }
}
