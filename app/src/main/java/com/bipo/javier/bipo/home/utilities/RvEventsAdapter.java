package com.bipo.javier.bipo.home.utilities;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.report.models.Report;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dmayorga on 6/04/2017.
 */

public class RvEventsAdapter extends RecyclerView.Adapter<RvEventsAdapter.ViewHolder> {


    private ArrayList<Report> reportList;
    private android.content.res.Resources resources;
    private Context context;
    private Animation anim;
    private static final String BASE_URL = "http://www.bipoapp.com/";

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBikeStatus, tvBikeBrand, tvBikeType, tvBikeColor ;
        ImageView ivBike;


        ViewHolder(View itemView) {
            super(itemView);
            ivBike = (ImageView)itemView.findViewById(R.id.IvBike);
            tvBikeStatus = (TextView)itemView.findViewById(R.id.TvBikeStatus);
            tvBikeBrand = (TextView)itemView.findViewById(R.id.TvBikeBrand);
            tvBikeType = (TextView)itemView.findViewById(R.id.TvBikeType);
            tvBikeColor = (TextView)itemView.findViewById(R.id.TvBikeColor);
        }
    }

    public RvEventsAdapter(Context context, ArrayList<Report> list) {

        reportList = list;
        this.context = context;
    }

    @Override
    public RvEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eventsView = inflater.inflate(R.layout.item_events,parent,false);
        resources = getContext().getResources();
        return new ViewHolder(eventsView);

    }

    @Override
    public void onBindViewHolder(RvEventsAdapter.ViewHolder holder, int position) {

        Report report = reportList.get(position);
        if (report.getReportPhotos() != null){
            if ((report.getReportPhotos().size() != 0)||(report.getBikePhotos().size() != 0)){
                getReportPhotos(holder, position);
            }else{
                ImageView bike = holder.ivBike;
                bike.setImageResource(R.mipmap.ic_no_image);

            }
        }else{
            ImageView bike = holder.ivBike;
            bike.setImageResource(R.mipmap.ic_no_image);
        }

        TextView status = holder.tvBikeStatus;
        status.setText(report.getReportType());
        if (report.getIdreportType() == 1){

            status.setTextColor(ContextCompat.getColor(getContext(),R.color.stolenBikeColor));
        }else if (report.getIdreportType() == 2){

            status.setTextColor(ContextCompat.getColor(getContext(),R.color.recoveredBikeColor));
        }else{

            status.setTextColor(ContextCompat.getColor(getContext(),R.color.darkBlue));
        }

        TextView brand = holder.tvBikeBrand;
        String bikeBrand = String.format(resources.getString(R.string.sr_itmtxt_bike_brand),
                report.getBrand());
        brand.setText(bikeBrand);
        TextView type = holder.tvBikeType;
        String bikeType = String.format(resources.getString(R.string.sr_itmtxt_bike_type),
                report.getType());
        type.setText(bikeType);
        TextView color = holder.tvBikeColor;
        String bikeColor = String.format(resources.getString(R.string.sr_itmtxt_bike_color),
                report.getColor());
        color.setText(bikeColor);
    }

    private void getReportPhotos(RvEventsAdapter.ViewHolder holder, int position){

        final ImageView bike = holder.ivBike;
        String imageUrl = "";
        if (reportList.get(position).getReportPhotos().size() != 0){
            imageUrl = BASE_URL + reportList.get(position).getReportPhotos().get(0).getUrl();
        }else{
            imageUrl = BASE_URL + reportList.get(position).getBikePhotos().get(0).getUrl();
        }
        Picasso.with(context).load(imageUrl).into(bike);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public Context getContext() {
        return context;
    }
}
