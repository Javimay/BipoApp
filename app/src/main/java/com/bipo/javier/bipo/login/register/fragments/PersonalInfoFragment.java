package com.bipo.javier.bipo.login.register.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.models.BikeBrand;
import com.bipo.javier.bipo.login.models.BikeBrandsResponse;
import com.bipo.javier.bipo.login.models.BikeColor;
import com.bipo.javier.bipo.login.models.BikeColorsResponse;
import com.bipo.javier.bipo.login.models.BikeType;
import com.bipo.javier.bipo.login.models.BikeTypesResponse;
import com.bipo.javier.bipo.login.models.EmailResponse;
import com.bipo.javier.bipo.login.utilities.Teclado;
import com.squareup.okhttp.ResponseBody;

import java.lang.annotation.Annotation;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfoFragment extends Fragment implements View.OnClickListener,
        View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private EditText etNombres, etApellidos, etNumeroCel, etCedula, etCorreo, etContraseña, etConfirmación;
    private TextView tvFecha;
    private String message;
    private static SimpleDateFormat simpleDateFormat;
    private boolean date, validateDate, okEmail;
    private int year, month, day;
    private Calendar calendar;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        initComponets(view);

        return view;
    }

    private void initComponets(View v) {

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Button BtNext = (Button) v.findViewById(R.id.BtNext);
        BtNext.setOnClickListener(this);
        Button BtCancel = (Button) v.findViewById(R.id.BtnCancel);
        BtCancel.setOnClickListener(this);
        tvFecha = (TextView) v.findViewById(R.id.TvFecha);
        tvFecha.setOnClickListener(this);
        etNombres = (EditText) v.findViewById(R.id.EtNombres);
        etApellidos = (EditText) v.findViewById(R.id.EtApellidos);
        etNumeroCel = (EditText) v.findViewById(R.id.EtNumCelular);
        etCedula = (EditText) v.findViewById(R.id.EtCedula);
        etCorreo = (EditText) v.findViewById(R.id.EtCorreo);
        etContraseña = (EditText) v.findViewById(R.id.EtContraseña);
        etConfirmación = (EditText) v.findViewById(R.id.EtConfContraseña);
        etConfirmación.setOnFocusChangeListener(this);
        etCorreo.setOnFocusChangeListener(this);
        validateDate = false;
        date = false;
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.BtnCancel) {
            this.getActivity().finish();
        } else if (v.getId() == R.id.BtNext) {
            //Todo: volver a activar la validacion y quitar el metodo de ir al otro fragment
            validatePersonalFields();
            //ToBikeFragment();
        } else if (v.getId() == R.id.TvFecha) {

            showDatePicker();
        }
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.getTime();
        datePickerDialog.updateDate(year, month, day);
        datePickerDialog.show();
    }

    private void validatePersonalFields() {
        Teclado.ocultarTeclado(getActivity());
        if (TextUtils.isEmpty(etNombres.getText().toString())) {
            etNombres.setError("Escribe tus nombres por favor.");
            return;
        }
        if (TextUtils.isEmpty(etApellidos.getText().toString())) {
            etApellidos.setError("Escribe tus apellidos por favor.");
            return;
        }
        //Validacion de edad +12
        if (!date) {
            Toast.makeText(getContext(), "Escoge tu fecha de nacimiento.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!validateDate) {
            Toast.makeText(getContext(), "Debes ser mayor de 12 años para registrarte.", Toast.LENGTH_LONG).show();
            return;
        }
        //Validación del numero de celular.
        if (TextUtils.isEmpty(etNumeroCel.getText().toString())) {
            etNumeroCel.setError("Escribe tu número celular por favor..");
            return;
        }
        if (!validateCel()) {
            etNumeroCel.setError("Tu numero de celular debe contener como minimo 10 digitos.");
            return;
        }
        //Validacion del documento
        if (TextUtils.isEmpty(etCedula.getText().toString())) {
            etCedula.setError("Escribe tu cédula por favor.");
            return;
        }
        if (!validateDocument()) {
            etCedula.setError("El documento debe tener minimo 8 digitos.");
            return;
        }

        //Validación del correo

        if (TextUtils.isEmpty(etCorreo.getText().toString())) {
            etCorreo.setError("Escribe tu correo por favor.");
            return;
        }
        String email = etCorreo.getText().toString();
        //userExist(email);
        if (userExist(email)) {

            //etCorreo.setError("Este correo ya está registrado");
            return;
        }
        if (!passwordVerify()) {
            etContraseña.setError("La contraseña debe tener " + message);
            return;
        }
        String pass = etContraseña.getText().toString();
        String confirm = etConfirmación.getText().toString();
        if (!pass.equals(confirm)) {
            etConfirmación.setError("Las contraseñas no coinciden.");
            return;
        }

        ToBikeFragment();
    }

    private void validateDate(int userYear, int userMonth, int userDay) {
        int currentMonth = 0, currentDay = 0, userYears = 0;
        calendar = Calendar.getInstance();
        userYears = year - userYear;
        currentMonth = month + 1 - userMonth;
        currentDay = day - userDay;
        if (currentMonth < 0 || (currentMonth == 0 && currentDay < 0)) {
            userYears--;
        }
        if (userYears < 12) {
            validateDate = false;
        } else {
            validateDate = true;
        }
    }

    private boolean validateDocument() {
        boolean ok = false;
        String document = etCedula.getText().toString();
        if (!(document.length() < 8)) {
            ok = true;
        }
        return ok;
    }

    private boolean validateCel() {
        boolean ok = false;
        String num = etNumeroCel.getText().toString();
        if (!(num.length() < 10)) {
            ok = true;
        }
        return ok;
    }

    private boolean validateEmail() {

        String email = etCorreo.getText().toString();
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

        return ok;
    }

    private void ToBikeFragment() {
        BikeFragment fragment = new BikeFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.FlFragmentRegister, fragment);
        ft.commit();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

            if (!validateEmail()) {
                etCorreo.setError("Tu correo debe contener un \"@\"");
            }

            if ((validateEmail()) && !etCorreo.isFocused()){
                String email = etCorreo.getText().toString();
                userExist(email);
            }
            if ((validateEmail())&& etCorreo.isFocused() && !okEmail){
                String email = etCorreo.getText().toString();
                userExist(email);
            }
            if (!passwordVerify()) {

                etContraseña.setError("La contraseña debe tener " + message);
            }
    }


    private boolean passwordVerify() {
        String password = etContraseña.getText().toString();
        boolean ok = false;
        char key;
        boolean upercase = false;
        boolean lowercase = false;
        boolean number = false;
        boolean size = false;
        String letter = null;
        message = "";

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
            message += " mínimo 6 caracteres.";
        }
        if (!upercase) {
            message += " una mayuscula.";
        }
        if (!lowercase) {
            message += " una minuscula.";
        }
        if (!number) {
            message += " un número.";
        }

        if ((upercase) && (lowercase) && (number) && (size)) {
            ok = true;
        }

        return ok;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = true;
        tvFecha.setText(getDateFormat(year, month + 1, dayOfMonth));
        validateDate(year, month + 1, dayOfMonth);
    }

    private String getDateFormat(int year, int month, int dayOfMonth) {

        String monthFormat = String.valueOf(month);
        String dayFormat = String.valueOf(dayOfMonth);
        if (month < 10) {
            monthFormat = "0" + month;
        }
        if (dayOfMonth < 10) {
            dayFormat = "0" + dayOfMonth;
        }
        return year + "-" + monthFormat + "-" + dayFormat;
    }

    private boolean userExist(String email) {

        AccountRepository repo = new AccountRepository(getContext());
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
                    if (response.body().userExist){
                        etCorreo.setError("Este correo ya está registrado.");
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


    public void showMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
