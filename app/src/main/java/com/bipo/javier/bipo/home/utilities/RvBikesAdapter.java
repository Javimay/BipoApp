package com.bipo.javier.bipo.home.utilities;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.models.Bike;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dmayorga on 6/04/2017.
 */

public class RvBikesAdapter extends RecyclerView.Adapter<RvBikesAdapter.ViewHolder> {


    private ArrayList<Bike> bikeList;
    private android.content.res.Resources resources;
    private Context context;
    private int itemLayout;
    private Animation anim;
    private static final String BIPO_URL = "http://www.bipoapp.com/";

    public RvBikesAdapter(Context context, ArrayList<Bike> list, int itemLayout) {

        this.itemLayout = itemLayout;
        bikeList = list;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBikeBrand, tvBikeType, tvBikeColor, tvBikeName, tvBikeStatus;
        ImageView ivBike, ivDefBike;
        ImageButton imgBtnCheckBike;
        RelativeLayout rlytCheckBike;

        ViewHolder(View bikeView, int layout) {
            super(bikeView);
            if(layout == R.layout.item_bikes_report){
                rlytCheckBike = (RelativeLayout)bikeView.findViewById(R.id.RlytCheckBike);
                imgBtnCheckBike = (ImageButton)bikeView.findViewById(R.id.ImgBtnCheckBike);
            }else{
                ivDefBike = (ImageView)bikeView.findViewById(R.id.ImgVDefBike);
            }

            ivBike = (ImageView)bikeView.findViewById(R.id.ImgVAccountBike);
            tvBikeBrand = (TextView)bikeView.findViewById(R.id.TvBikeBrand);
            tvBikeType = (TextView)bikeView.findViewById(R.id.TvBikeType);
            tvBikeColor = (TextView)bikeView.findViewById(R.id.TvBikeColor);
            tvBikeName = (TextView)bikeView.findViewById(R.id.TvBikeName);
            tvBikeStatus = (TextView)bikeView.findViewById(R.id.TvBikeStatus);
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

        Bike bikes = bikeList.get(position);

        if (bikes.getBikePhotos() != null){
            if (bikes.getBikePhotos().size() != 0){
                getBikePhotos(holder,position);
            }else{
                holder.ivBike.setImageResource(R.mipmap.ic_no_image);
            }
        }else{
            holder.ivBike.setImageResource(R.mipmap.ic_no_image);
        }
        if (bikes.getIsDefault() == 1){
            holder.ivDefBike.setImageResource(R.mipmap.ic_default_bike);
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
        TextView status = holder.tvBikeStatus;
        if (bikes.getBikestate().equals("ROBADA")){

            status.setText(bikes.getBikestate());
            status.setTextColor(ContextCompat.getColor(getContext(),R.color.stolenBikeColor));
        }else if (bikes.getBikestate().equals("RECUPERADA")){

            status.setText(bikes.getBikestate());
            status.setTextColor(ContextCompat.getColor(getContext(),R.color.recoveredBikeColor));
        }
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
        if (bikes.getBikePhotos() != null){
            if (bikes.getBikePhotos().size() > 0){
                getBikePhotos(holder,position);
            }else{
                holder.ivBike.setImageResource(R.mipmap.ic_no_image);
            }
        }else{
            holder.ivBike.setImageResource(R.mipmap.ic_no_image);
        }

        if(!bikeList.get(position).isChecked()){
            holder.imgBtnCheckBike.setVisibility(View.INVISIBLE);
            holder.rlytCheckBike.setBackgroundColor(Color.TRANSPARENT);
        }
        //OnClickListener para el layout contenedor del icono de bicicleta por default.
        holder.rlytCheckBike.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(!bikeList.get(position).isChecked()){
                    showMessage("Has elegido: "+ bikeList.get(position).getBikename());
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
            }
        });

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

    private void getBikePhotos(RvBikesAdapter.ViewHolder holder, int position){

        final ImageView bike = holder.ivBike;
        String imageUrl = BIPO_URL + bikeList.get(position).getBikePhotos().get(0).getUrl();
        Picasso.with(context).load(imageUrl).into(bike);
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
