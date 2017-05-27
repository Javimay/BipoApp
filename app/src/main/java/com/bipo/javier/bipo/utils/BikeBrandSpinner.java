package com.bipo.javier.bipo.utils;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.models.BikeBrand;
import com.bipo.javier.bipo.login.models.BikeBrandsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeBrandSpinner implements AdapterView.OnItemSelectedListener{

    private ArrayAdapter adapter;
    private Long idBrand;

    public BikeBrandSpinner(Context context, List<String> listBrands) {

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context, listBrands);
        adapter = spinnerAdapter.getAdapter();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        idBrand = id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public AdapterView.OnItemSelectedListener getListener() {
        return this;
    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }

    public Long getIdBrand() {
        return idBrand;
    }
}
