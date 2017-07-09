package com.bipo.javier.bipo.home.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.models.Bike;

import java.util.ArrayList;

/**
 * Created by dmayorga on 6/04/2017.
 */

public class RvBikesAdapter extends RecyclerView.Adapter<RvBikesAdapter.ViewHolder> {


    private ArrayList<Bike> bikeList;
    private android.content.res.Resources resources;
    private Context context;
    private int itemLayout;

    public RvBikesAdapter(Context context, ArrayList<Bike> list, int itemLayout) {

        this.itemLayout = itemLayout;
        bikeList = list;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBikeBrand, tvBikeType, tvBikeColor, tvBikeName;
        ImageView ivBike, ibtnDefBike, ibtnDelBike;
        ImageButton imgBtnCheckBike;
        RelativeLayout rlytCheckBike;

        ViewHolder(View bikeView, int layout) {
            super(bikeView);
            if(layout == R.layout.item_bikes_report){
                rlytCheckBike = (RelativeLayout)bikeView.findViewById(R.id.RlytCheckBike);
                imgBtnCheckBike = (ImageButton)bikeView.findViewById(R.id.ImgBtnCheckBike);
            }else{
                ibtnDefBike = (ImageButton)bikeView.findViewById(R.id.ImgBtnDefBike);
                ibtnDelBike = (ImageButton)bikeView.findViewById(R.id.ImgBtnDelBike);
            }

            ivBike = (ImageView)bikeView.findViewById(R.id.ImgVAccountBike);
            tvBikeBrand = (TextView)bikeView.findViewById(R.id.TvBikeBrand);
            tvBikeType = (TextView)bikeView.findViewById(R.id.TvBikeType);
            tvBikeColor = (TextView)bikeView.findViewById(R.id.TvBikeColor);
            tvBikeName = (TextView)bikeView.findViewById(R.id.TvBikeName);
        }
    }

    @Override
    public void onBindViewHolder(final RvBikesAdapter.ViewHolder holder, final int position) {

        if(this.itemLayout == R.layout.item_bikes){
            accountItems(holder, position);
        }else{
            reportItems(holder, position);
        }
    }

    public void accountItems(final RvBikesAdapter.ViewHolder holder, final int position){

        if(bikeList.get(position).isDefaultbike()){

            holder.ibtnDefBike.setImageResource(R.mipmap.ic_default_bike);
        }
        //OnClickListener para el botón de borrar bicicleta.
        holder.ibtnDelBike.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showMessage("Borrar: "+ bikeList.get(position).getBikename());
            }
        });

        //OnClicListener para el botón default de la bicicleta.
        holder.ibtnDefBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!bikeList.get(position).isDefaultbike()){

                    showMessage(bikeList.get(position).getBikename() + " Es tu bicicleta principal.");
                    for( Bike bike:bikeList){
                        bike.setDefaultbike(false);
                        holder.ibtnDefBike.setImageResource(R.mipmap.ic_default_bike_des);
                    }
                    bikeList.get(position).setDefaultbike(true);
                    // holder.ibtnDefBike.setImageResource(R.mipmap.ic_default_bike);
                    notifyDataSetChanged(); //Refresca la lista y la actualiza.
                    //TODO: Guardar default en la base de datos
                }else{

                    showMessage(bikeList.get(position).getBikename() + " ya es tu bicicleta principal.");
                }
            }
        });

        Bike bikes = bikeList.get(position);
        ImageView bike = holder.ivBike;
        //Todo:Poner imagen de la bicicleta.
        bike.setImageResource(R.drawable.wheel);

        if (!bikes.isDefaultbike()){
            holder.ibtnDefBike.setImageResource(R.mipmap.ic_default_bike_des);
        }
        TextView brand = holder.tvBikeBrand;
        String bikeBrand = String.format(resources.getString(R.string.sr_itmtxt_bike_brand),
                bikes.getBrand());
        brand.setText(bikeBrand);
        TextView type = holder.tvBikeType;
        String bikeType = String.format(resources.getString(R.string.sr_itmtxt_bike_type),
                bikes.getType());
        type.setText(bikeType);
        TextView color = holder.tvBikeColor;
        String bikeColor = String.format(resources.getString(R.string.sr_itmtxt_bike_color),
                bikes.getColor());
        color.setText(bikeColor);
        TextView bikeName = holder.tvBikeName;
        bikeName.setText(bikes.getBikename());
    }

    @Override
    public RvBikesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View bikesView = inflater.inflate(this.itemLayout,parent,false);
        resources = getContext().getResources();
        return new ViewHolder(bikesView, this.itemLayout);
    }

    public void reportItems(final RvBikesAdapter.ViewHolder holder, final int position){

        final Bike bikes = bikeList.get(position);
        if(!bikeList.get(position).isChecked()){
            holder.imgBtnCheckBike.setVisibility(View.INVISIBLE);
            holder.rlytCheckBike.setBackgroundColor(Color.TRANSPARENT);
        }
        //OnClickListener para el layout contenedor del check.
        holder.rlytCheckBike.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(!bikeList.get(position).isChecked()){
                    showMessage("Check: "+ bikeList.get(position).getBikename());
                    for( Bike bike:bikeList){
                        bike.setChecked(false);
                        holder.imgBtnCheckBike.setVisibility(View.INVISIBLE);
                        holder.rlytCheckBike.setBackgroundColor(Color.TRANSPARENT);
                    }
                    bikeList.get(position).setChecked(true);
                    holder.imgBtnCheckBike.setVisibility(View.VISIBLE);
                    holder.rlytCheckBike.setBackgroundColor(ContextCompat.getColor(getContext(),
                            R.color.itemChecked));
                    notifyDataSetChanged(); //Refresca la lista y la actualiza.
                }else{

                    showMessage(bikeList.get(position).getBikename());
                }

                //holder.imgBtnCheckBike.setVisibility(View.VISIBLE);
                //notifyDataSetChanged();
            }
        });

        ImageView bike = holder.ivBike;
        bike.setImageResource(R.drawable.wheel); //Todo:Poner imagen de la bicicleta.

        TextView brand = holder.tvBikeBrand;
        String bikeBrand = String.format(resources.getString(R.string.sr_itmtxt_bike_brand),
                bikes.getBrand());
        brand.setText(bikeBrand);
        TextView type = holder.tvBikeType;
        String bikeType = String.format(resources.getString(R.string.sr_itmtxt_bike_type),
                bikes.getType());
        type.setText(bikeType);
        TextView color = holder.tvBikeColor;
        String bikeColor = String.format(resources.getString(R.string.sr_itmtxt_bike_color),
                bikes.getColor());
        color.setText(bikeColor);
        TextView bikeName = holder.tvBikeName;
        bikeName.setText(bikes.getBikename());
    }

    @Override
    public int getItemCount() {
        return bikeList.size();
    }

    public Context getContext() {
        return context;
    }

    private void showMessage(String message){

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
