package com.bipo.javier.bipo.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.report.models.BikeColor;

import java.util.ArrayList;

/**
 * Created by Javier on 13/05/2017.
 */

public class ColorsSpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
   /* private String[] colorNames;
    private String[] colorArray;*/
    private ArrayList colors;
    private LayoutInflater inflater;
    private BikeColor colorsModel;

    public ColorsSpinnerAdapter(Context context, ArrayList objects) {
        super(context, R.layout.activity_item_color,objects);
        this.context = context;
        this.colors = objects;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.activity_item_color, parent, false);
        colorsModel = null;
        colorsModel = (BikeColor)colors.get(position);
        TextView tvColor = (TextView) row.findViewById(R.id.TvColorText);
        ImageView ivColor = (ImageView)row.findViewById(R.id.IvColorImage);

        if (position == 0){
            tvColor.setText(colorsModel.getColor());
            ivColor.setVisibility(View.INVISIBLE);
            ivColor.getLayoutParams().width = 1;
        }else{
            ivColor.setVisibility(View.VISIBLE);
            tvColor.setText(colorsModel.getColor());
            ivColor.setBackgroundColor(Color.parseColor("#" + colorsModel.getHexColor()));
        }

        return row;
    }
}
