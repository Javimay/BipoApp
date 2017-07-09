package com.bipo.javier.bipo.utils;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeTypeSpinner implements AdapterView.OnItemSelectedListener{

    private ArrayAdapter adapter;
    private Long idType;

    public BikeTypeSpinner(Context context, List<String> listTypes) {

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context, listTypes);
        adapter = spinnerAdapter.getAdapter();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        idType = id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }

    public Long getIdType() {
        return idType;
    }

    public AdapterView.OnItemSelectedListener getListener() {
        return this;
    }
}
