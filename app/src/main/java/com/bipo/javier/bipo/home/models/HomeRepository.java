package com.bipo.javier.bipo.home.models;

import android.content.Context;

import com.bipo.javier.bipo.data.IRestClient;
import com.bipo.javier.bipo.data.RestClient;
import com.bipo.javier.bipo.account.models.BikesResponse;
import com.bipo.javier.bipo.report.models.Report;
import com.bipo.javier.bipo.report.models.ReportResponse;
import com.squareup.okhttp.RequestBody;

import okhttp3.MultipartBody;
import retrofit.Call;

/**
 * Created by Javier on 25/05/2017.
 */

public class HomeRepository {

    private final IRestClient apiService;
    private static final String token = "650E01A1B8F9A4DA4A2040FF86E699B7";

    public HomeRepository(Context context) {
        apiService = new RestClient(context, token).getApiService();
    }

    public Call<GetReportResponse> getReports(int reportType, String fhInicio, String fhFin) {
        return apiService.getReports(reportType, fhInicio, fhFin);
    }

    public Call<GetReportResponse> getLastReports() {
        return apiService.getLastReports();
    }

    public Call<GetBikesResponse> getAccountBikes(String token) {
        return apiService.getAccountBikes(token);
    }

    public Call<BikesResponse> bikeRegister(String bikeName, int idBrand, int idColor, String idFrame,
                                            int idType, String bikeFeatures, int idBikeState, String token){
        return apiService.registerBike(bikeName, idBrand, idColor, idFrame, idType, bikeFeatures,
                idBikeState, token);
    }

    public Call<BikesResponse> registerBikePhoto(String bikeName,RequestBody bikePhoto, String token){
        return apiService.registerBikePhoto(bikeName, bikePhoto, token);
    }

    public Call<ReportResponse> registerReport(String token, String reportName, int reporType, String coordinates,
                                               int idBike, String reportDetails){
        return apiService.registerReport(token, reportName, reporType, coordinates, idBike, reportDetails);
    }

    public Call<BikesResponse> changePass(String email, String password, String newPassword){
        return apiService.changePass(email, password, newPassword);
    }

    public Call<BikesResponse> registerReportPhoto(String reportName,String token, RequestBody bikePhoto){
        return apiService.registerReportPhoto(reportName, token, bikePhoto);
    }

    public Call<BikesResponse> deleteBike(int bikeId,String token){
        return apiService.deleteBike(bikeId, token);
    }

    public Call<BikesResponse> defaultBike(int bikeId,String token){
        return apiService.defaultBike(bikeId, token);
    }

    public Call<GetBikesResponse> getBikeByName(String token, String bikeName) {
        return apiService.getBikeByName(token, bikeName);
    }

    public Call<BikesResponse> updateBike(int bikeId, String token, int idColor, String BikeFeatures){
        return apiService.updateBike(bikeId, token, idColor, BikeFeatures);
    }
}
