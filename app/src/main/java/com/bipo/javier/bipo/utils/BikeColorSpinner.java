package com.bipo.javier.bipo.utils;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeColorSpinner implements AdapterView.OnItemSelectedListener {

    private ArrayAdapter adapter;
    private Long idColor;
    private List<String> colorList;

    public BikeColorSpinner(Context context, ArrayList colorList) {

        /*SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context, colorList);
        adapter = spinnerAdapter.getAdapter();*/
        adapter = new ColorsSpinnerAdapter(context,colorList);
    }

    public BikeColorSpinner(Context context, List<String> colorList) {
        this.colorList = colorList;
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context, colorList);
        adapter = spinnerAdapter.getAdapter();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        idColor = id;
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

    public Long getIdColor() {
        return idColor;
    }
}
