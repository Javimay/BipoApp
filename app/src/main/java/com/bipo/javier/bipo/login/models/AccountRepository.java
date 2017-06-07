package com.bipo.javier.bipo.login.models;

import android.content.Context;

import com.bipo.javier.bipo.data.IRestClient;
import com.bipo.javier.bipo.data.RestClient;

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

    public Call<BikeBrandsResponse> getBikeBrands() {
        return apiService.getBikeBrands();
    }

    public Call<BikeColorsResponse> getBikeColors() {
        return apiService.getBikeColors();
    }

    public Call<BikeTypesResponse> getBikeTypes() {
        return apiService.getBikeTypes();
    }

    public Call<UserResponse> userRegister(String name, String lastName, String email, String birthDate,
                                           String cellphone, String document, String password){
        return apiService.registerUser(name, lastName, email, birthDate, cellphone, document, password);
    }

   /* public Call<RegisterResponse> RegisterUser(String email, String name, String lastName, String password,
                                               String confirmPass, String provider, String identifier,String docType,String Document) {
        return apiService.registerUser(email,name,lastName,password,
                confirmPass,provider,identifier,docType,Document);
    }
    public Call<DefaultResponse> ChangePassword(String email) {
        return apiService.ChangePassword(email);
    }
    public Call<DocumentResponse> PostDocumentVerification(String Document, String ExpDate, Integer DocType) {
        return apiService.PostDocumentVerification(Document, ExpDate, DocType);
    }
    public Call<DocumentResponse> GetByDocument(String Document, String ExpDate, Integer DocType) {
        return apiService.GetByDocument(Document, ExpDate, DocType);
    }*/

}