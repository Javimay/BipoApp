package com.bipo.javier.bipo.utils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.List;
import android.content.Context;

/**
 * Created by Javier on 11/05/2017.
 */

public class ReportTypesSpinner implements AdapterView.OnItemSelectedListener{

    private Context context;
    private ArrayAdapter adapter;
    private Long idReportType;

    public ReportTypesSpinner(Context context, List<String> listReports) {
        this.context = context;
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context, listReports);
        adapter = spinnerAdapter.getAdapter();
    }



    public ArrayAdapter getAdapter() {
        return adapter;
    }

    public AdapterView.OnItemSelectedListener getListener() {
        return this;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        idReportType = id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public Long getIdReportType() {
        return idReportType;
    }

}
