package com.bipo.javier.bipo.utils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.models.BikeState;
import com.bipo.javier.bipo.login.models.BikeStatesResponse;
import com.bipo.javier.bipo.report.ReportRepository;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * Created by Javier on 11/05/2017.
 */

public class BikeStatusSpinner implements AdapterView.OnItemSelectedListener{

    private Context context;
    private ArrayAdapter adapter;
    private Long idState;

    public BikeStatusSpinner(Context context) {
        this.context = context;
        getBikeStates();
    }

    private void getBikeStates() {

        List<String> listStates = new ArrayList<>();
        ReportRepository repo = new ReportRepository(context);
        Call<BikeStatesResponse> call = repo.getBikeStates();
        call.enqueue(new Callback<BikeStatesResponse>() {
            @Override
            public void onResponse(retrofit.Response<BikeStatesResponse> response, Retrofit retrofit) {
                //dialogFragment.dismiss();
                //String error = "Error desconocido, verifique su conexión a internet o contacte al administrador";
                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    System.out.println(response.isSuccess());
                    System.out.println(response.message());
                    System.out.println(response.code());
                }
                if (response != null && response.isSuccess() && response.message() != null) {

                    List<BikeState> bikes = response.body().bikeStates;
                    BikeStatesResponse bikeResponse = new BikeStatesResponse();
                    bikeResponse.setBikeStates(response.body().bikeStates);
                    List<String> listStates = new ArrayList<>();
                    listStates.add(0, "Escoge un estado de la bicicleta.");
                    for (BikeState bike : bikes) {

                        listStates.add(bike.getId(), bike.getBikeState());
                        System.out.println("id: " + bike.getId() + "\nstate: " + bike.getBikeState());
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
            }
        });
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context, listStates);
        adapter = spinnerAdapter.getAdapter();
        System.out.println(adapter);
    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }

    public AdapterView.OnItemSelectedListener getListener() {
        return this;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        idState = id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public Long getIdState() {
        return idState;
    }

}
