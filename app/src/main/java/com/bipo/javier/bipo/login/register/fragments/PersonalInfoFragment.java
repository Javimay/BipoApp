package com.bipo.javier.bipo.login.register.fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.activities.HomeActivity;
import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.models.EmailResponse;
import com.bipo.javier.bipo.login.models.LoginResponse;
import com.bipo.javier.bipo.login.models.User;
import com.bipo.javier.bipo.login.models.UserResponse;
import com.bipo.javier.bipo.login.utilities.Teclado;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfoFragment extends Fragment implements View.OnClickListener,
        View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private EditText etNombres, etApellidos, etNumeroCel, etCedula, etCorreo, etContraseña, etConfirmación;
    private TextView tvFecha;
    private String message, birthdate;
    private static SimpleDateFormat simpleDateFormat;
    private boolean date, validateDate, okEmail;
    private int year, month, day;
    private Calendar calendar;
    private CheckBox chbxFotos, chbxTerminos;
    private final int loggedWeb = 0;
    private final int loggedApp = 1;

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
        etContraseña.setOnFocusChangeListener(this);
        etConfirmación = (EditText) v.findViewById(R.id.EtConfContraseña);
        etConfirmación.setOnFocusChangeListener(this);
        etCorreo.setOnFocusChangeListener(this);
        chbxFotos = (CheckBox) v.findViewById(R.id.ChbxPoliticaFotos);
        chbxTerminos = (CheckBox) v.findViewById(R.id.ChbxPoliticaTerminos);
        validateDate = false;
        date = false;
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.BtnCancel) {
            this.getActivity().finish();
        } else if (v.getId() == R.id.BtNext) {
            validatePersonalFields();
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
        if(!okText(etNombres.getText().toString())){
            etNombres.setError("Los nombres no deben contener numeros.");
            return;
        }
        if (!okText(etApellidos.getText().toString())){
            etApellidos.setError("Los apellidos no deben contener numeros");
            return;
        }
        //Validacion de edad +12
        if (!date) {
            showMessage("Escoge tu fecha de nacimiento.");
            return;
        }
        if (!validateDate) {
            showMessage("Debes ser mayor de 12 años para registrarte.");
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
        if (!chbxFotos.isChecked()) {
            showMessage("Debes aceptar la publicación de fotos.");
            return;
        }
        if (!chbxTerminos.isChecked()) {
            showMessage("Debes aceptar los terminos y condiciones.");
            return;
        }
        registerUser();
    }

    private boolean okText(String text) {
        String letter = null;
        char key;
        boolean ok = true;
        for (int i = 0; i < text.length(); i++){
        key = text.charAt(i);
        letter = String.valueOf(key);
            if (letter.matches("[0-9]")) {
                return ok = false;
            }
        }
        return ok;
    }

    private void registerUser() {
        String name = etNombres.getText().toString();
        String lastName = etApellidos.getText().toString();
        final String email = etCorreo.getText().toString();
        String cellphone = etNumeroCel.getText().toString();
        String document = etCedula.getText().toString();
        final String password = etContraseña.getText().toString();

        AccountRepository repo = new AccountRepository(getContext());
        Call<UserResponse> call = repo.userRegister(name, lastName, email, birthdate, cellphone,
                                                        document,password);
        final UserResponse userResponse = new UserResponse();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(retrofit.Response<UserResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    if (response.code() == 400) {
                        showMessage("No hay datos.");
                        System.out.println(response.isSuccess());
                        System.out.println(response.message());
                        System.out.println(response.code());
                        userResponse.setMessage(response.message());
                    } else {
                        showMessage("Ocurrió un error en la red.");
                        System.out.println(userResponse.getMessage());
                    }
                }

                if (response != null && response.isSuccess() && response.message() != null){

                    System.out.println(response.message());
                    showMessage("Te has registrado exitosamente. \n¡Bienvenido a bipo!");
                    getInHome(email, password);
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

    private void getInHome(String email, String password) {

        AccountRepository repo = new AccountRepository(getContext());
        Call<LoginResponse> call = repo.login(email, password,loggedWeb,loggedApp);
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
                    loginResponse.setUser(response.body().getUser());
                    ArrayList<User> userList = loginResponse.getUser();
                    if (userList != null) {

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
                        savePreferences(token);
                        goToHomeActivity(name, lastName, email, birthday, phone, documentid, token);
                    }else{
                        Log.e("Register/GetInHome", "Response.body empty...");
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

    private void savePreferences(String token) {

        int photoPublication = 0;
        if (chbxFotos.isChecked()){
            photoPublication = 1;
        }
        AccountRepository repo = new AccountRepository(getContext());
        Call<UserResponse> call = repo.savePreferences(token,0,photoPublication,0,0);
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
                    Log.e("UserRegisterPreferences","Preferences saved");
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
            birthdate = dateFormat(userYear, userMonth, userDay);
            validateDate = true;
        }
    }

    public String dateFormat(int year, int month, int day) {

        this.year = year;
        this.month = month;
        this.day = day;
        String monthFormat = String.valueOf(month);
        String dayFormat = String.valueOf(day);
        String date;
        if (month < 10) {
            monthFormat = "0" + month;
        }
        if (day < 10) {
            dayFormat = "0" + day;
        }
        date = year + "-" + monthFormat + "-" + dayFormat;
        return date;
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

    private void goToHomeActivity(String name, String lastName, String email, String birthday, String phone,
                                  String documentId, String token) {

        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("lastName", lastName);
        intent.putExtra("email", email);
        intent.putExtra("birthdate", birthday);
        intent.putExtra("phone", phone);
        intent.putExtra("documentId", documentId);
        intent.putExtra("token", token);
        showMessage("Bienvenido " + name + " " + lastName);
        startActivity(intent);
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

                    emailResponse.setUserExist(response.body().getUserExist());
                    if (emailResponse.getUserExist().size() != 0){
                        etCorreo.setError("Este correo ya está registrado.");
                        okEmail = true;
                    }else{
                        okEmail = false;
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
