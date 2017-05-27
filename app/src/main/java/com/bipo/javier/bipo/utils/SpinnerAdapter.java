package com.bipo.javier.bipo.utils;

import android.widget.ArrayAdapter;
import android.content.Context;

import java.util.List;

/**
 * Created by Javier on 11/05/2017.
 */

public class SpinnerAdapter {
    private ArrayAdapter<String> SpAdapter;

    public SpinnerAdapter(Context context, List<String> list){

        SpAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                list);
        SpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    public ArrayAdapter getAdapter (){return SpAdapter;}

}
