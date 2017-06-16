package com.bipo.javier.bipo.home.models;

import android.content.Context;

import com.bipo.javier.bipo.data.IRestClient;
import com.bipo.javier.bipo.data.RestClient;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.Map;

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

    public Call<GetBikesResponse> getAccountBikes(String token) {
        return apiService.getAccountBikes(token);
    }

    public Call<BikesResponse> bikeRegister(String bikeName, int idBrand,int idColor,String idFrame,
                                            int idType, String bikeFeatures, int idBikeState, String token){
        return apiService.registerBike(bikeName, idBrand, idColor, idFrame, idType, bikeFeatures,
                idBikeState, token);
    }

    public Call<BikesResponse> registerBikePhoto(String bikeName, String token,
                                                 RequestBody bikePhoto){
        return apiService.registerBikePhoto(bikeName, token, bikePhoto);
    }
}
