package com.bipo.javier.bipo.login.models;

import android.content.Context;

import com.bipo.javier.bipo.account.models.BikesResponse;
import com.bipo.javier.bipo.data.IRestClient;
import com.bipo.javier.bipo.data.RestClient;
import com.bipo.javier.bipo.report.models.BikeBrandsResponse;
import com.bipo.javier.bipo.report.models.BikeColorsResponse;
import com.bipo.javier.bipo.report.models.BikeTypesResponse;

import retrofit.Call;


public class AccountRepository {

    private final IRestClient apiService;
    private static final String token = "650E01A1B8F9A4DA4A2040FF86E699B7";

    public AccountRepository(Context context) {
        apiService = new RestClient(context, token).getApiService();
    }

    public Call<LoginResponse> login(String email, String pwd) {
        return apiService.login(email, pwd);
    }

    public Call<EmailResponse> verifyEmail(String email) {
        return apiService.emailVerify(email);
    }

    public Call<BikesResponse> recoveryPass(String email) {
        return apiService.recoveryPass(email);
    }

    public Call<UserResponse> userRegister(String name, String lastName, String email, String birthDate,
                                           String cellphone, String document, String password){
        return apiService.registerUser(name, lastName, email, birthDate, cellphone, document, password);
    }

    public Call<UserResponse> savePreferences(String token,int emailRec,int photoPublic,
                                               int reportUbicartion, int location) {
        return apiService.savePreferences(token, emailRec,photoPublic,reportUbicartion,location);
    }
}