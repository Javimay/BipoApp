package com.bipo.javier.bipo.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.models.Bike;

import java.util.ArrayList;

/**
 * Created by dmayorga on 6/04/2017.
 */

public class RvBikesAdapter extends RecyclerView.Adapter<RvBikesAdapter.ViewHolder> {


    private ArrayList<Bike> bikeList;
    private android.content.res.Resources resources;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBikeStatus, tvBikeBrand, tvBikeType, tvBikeColor, tvBikeName;
        ImageView ivBike, ibtnDefBike, ibtnDelBike;


        ViewHolder(View bikeView) {
            super(bikeView);
            ibtnDefBike = (ImageButton)bikeView.findViewById(R.id.ImgBtnDefBike);
            ibtnDelBike = (ImageButton)bikeView.findViewById(R.id.ImgBtnDelBike);
            ivBike = (ImageView)bikeView.findViewById(R.id.ImgVAccountBike);
            tvBikeStatus = (TextView)bikeView.findViewById(R.id.TvBikeStatus);
            tvBikeBrand = (TextView)bikeView.findViewById(R.id.TvBikeBrand);
            tvBikeType = (TextView)bikeView.findViewById(R.id.TvBikeType);
            tvBikeColor = (TextView)bikeView.findViewById(R.id.TvBikeColor);
            tvBikeName = (TextView)bikeView.findViewById(R.id.TvBikeName);
        }
    }

    public RvBikesAdapter(Context context, ArrayList<Bike> list) {

        bikeList = list;
        this.context = context;
    }

    @Override
    public RvBikesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View bikesView = inflater.inflate(R.layout.item_bikes,parent,false);
        resources = getContext().getResources();
        return new ViewHolder(bikesView);

    }

    @Override
    public void onBindViewHolder(RvBikesAdapter.ViewHolder holder, int position) {

        Bike bikes = bikeList.get(position);
        ImageView bike = holder.ivBike;
        //Todo:Poner Información de la bicicleta.
        bike.setImageResource(R.drawable.wheel);

        if (!bikes.isDefaultBike()){
            holder.ibtnDefBike.setImageResource(R.mipmap.ic_default_bike_des);
        }
        TextView brand = holder.tvBikeBrand;
        String bikeBrand = String.format(resources.getString(R.string.sr_itmtxt_bike_brand),
                bikes.getBikeBrand());
        brand.setText(bikeBrand);
        TextView type = holder.tvBikeType;
        String bikeType = String.format(resources.getString(R.string.sr_itmtxt_bike_type),
                bikes.getBikeType());
        type.setText(bikeType);
        TextView color = holder.tvBikeColor;
        String bikeColor = String.format(resources.getString(R.string.sr_itmtxt_bike_color),
                bikes.getBikeColor());
        color.setText(bikeColor);
        TextView idBike = holder.tvBikeName;
        idBike.setText(bikes.getBikeName());
    }

    @Override
    public int getItemCount() {
        return bikeList.size();
    }

    public Context getContext() {
        return context;
    }
}
