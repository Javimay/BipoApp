package com.bipo.javier.bipo.report.models;

import com.bipo.javier.bipo.data.IRestClient;
import com.bipo.javier.bipo.data.RestClient;

import android.content.Context;

import retrofit.Call;

/**
 * Created by Javier on 12/05/2017.
 */

public class ReportRepository {

    private final IRestClient apiService;
    private static final String token = "650E01A1B8F9A4DA4A2040FF86E699B7";

    public ReportRepository(Context context) {
        apiService = new RestClient(context, token).getApiService();
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

    public Call<ReportTypesResponse> getReportTypes() {
        return apiService.getReportTypes();
    }

}
