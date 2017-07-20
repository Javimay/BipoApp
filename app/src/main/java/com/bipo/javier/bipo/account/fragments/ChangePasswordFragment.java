package com.bipo.javier.bipo.account.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.models.BikesResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.models.LoginResponse;
import com.bipo.javier.bipo.login.models.User;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener,
                                                        View.OnFocusChangeListener{

    private EditText etOldPass, etNewPass, etConfNewPass;
    private Button btnChangePass;
    private SharedPreferences preferences;
    private String passMessage;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        etOldPass = (EditText) view.findViewById(R.id.EtOldPass);
        etNewPass = (EditText) view.findViewById(R.id.EtNewPass);
        etNewPass.setOnFocusChangeListener(this);
        etConfNewPass = (EditText) view.findViewById(R.id.EtConfNewPass);
        btnChangePass = (Button) view.findViewById(R.id.BtnChangePass);
        btnChangePass.setOnClickListener(this);
        preferences = getActivity().getSharedPreferences("UserInfo", 0);
        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.BtnChangePass) {
            validateFields();
        }
    }

    private void validateFields() {

        String oldPass = etOldPass.getText().toString();
        String newPass = etNewPass.getText().toString();
        String email = preferences.getString("email", "");
        if (TextUtils.isEmpty(etOldPass.getText().toString())){
            etOldPass.setError("Escribe tu contraseña actual.");
            return;
        }
        //TODO:Validar especificacaciones de contraseña.
        if (TextUtils.isEmpty(etNewPass.getText().toString())){
            etOldPass.setError("Escribe tu nueva contraseña.");
            return;
        }
        if (!passwordVerify()) {
            etNewPass.setError("La contraseña debe tener " + passMessage);
            return;
        }
        if (!newPass.equals(etConfNewPass.getText().toString())) {
            etConfNewPass.setError("La contraseña no coincide con la nueva.");
            return;
        }
        changePassword(email, oldPass, newPass);

    }

    private void changePassword(String email, String oldPass, String newPass) {

        HomeRepository repo = new HomeRepository(getContext());
        Call<BikesResponse> call = repo.changePass(email, oldPass, newPass);
        final BikesResponse bikesResponse = new BikesResponse();
        call.enqueue(new Callback<BikesResponse>() {
            @Override
            public void onResponse(retrofit.Response<BikesResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    if (response.code() == 400) {
                        showMessage("Contraseña actual incorrecta.");
                        System.out.println(response.isSuccess());
                        System.out.println(response.message());
                        System.out.println(response.code());
                        bikesResponse.setMessage(response.message());
                    } else {
                        showMessage("Ocurrió un error en la red.");
                        System.out.println(bikesResponse.getMessage());
                    }
                }
                if (response != null && response.isSuccess() && response.message() != null) {

                    if (response.body().getError() == "false") {

                        showMessage("La contraseña se ha cambiado exitosamente!");
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
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

    private boolean passwordVerify() {
        String password = etNewPass.getText().toString();
        boolean ok = false;
        char key;
        boolean upercase = false;
        boolean lowercase = false;
        boolean number = false;
        boolean size = false;
        String letter = null;
        passMessage = "";

        if (!(password.equals("")) && !(password.length() < 6)) {
            size = true;
        }
        for (int i = 0; i < password.length(); i++) {

            key = password.charAt(i);
            letter = String.valueOf(key);
            if ((letter.matches("[A-Z]")) && (!upercase)) {
                upercase = true;
            } else if ((letter.matches("[a-z]")) && (!lowercase)) {
                lowercase = true;
            } else if ((letter.matches("[0-9]")) && (!number)) {
                number = true;
            }
        }
        if (!size) {
            passMessage += " mínimo 6 caracteres.";
        }
        if (!upercase) {
            passMessage += " una mayuscula.";
        }
        if (!lowercase) {
            passMessage += " una minuscula.";
        }
        if (!number) {
            passMessage += " un número.";
        }

        if ((upercase) && (lowercase) && (number) && (size)) {
            ok = true;
        }

        return ok;

    }

    private void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!passwordVerify()) {

            etNewPass.setError("La contraseña debe tener " + passMessage);
        }
    }
}
