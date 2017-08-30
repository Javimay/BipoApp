package com.bipo.javier.bipo.home.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.utils.RvEventsAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import javax.annotation.Resources;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventItemsFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private TextView tvStatus, tvBrand, tvType, tvColor;
    private ImageView ivBike;
    private MapView mvPlace;
    private Button btnSawBike;
    private android.content.res.Resources resources;
    private Animation anim;

    public EventItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_items, container, false);
        tvStatus = (TextView) view.findViewById(R.id.TvItemStatus);
        tvBrand = (TextView) view.findViewById(R.id.TvItemBrand);
        tvType = (TextView) view.findViewById(R.id.TvItemType);
        tvColor = (TextView) view.findViewById(R.id.TvItemColor);
        ivBike = (ImageView) view.findViewById(R.id.IvItemBike);
        mvPlace = (MapView)view.findViewById(R.id.MvItemPlace);
        mvPlace.onCreate(savedInstanceState);
        mvPlace.getMapAsync(this);
        btnSawBike = (Button)view.findViewById(R.id.BtnSawBike);
        btnSawBike.setOnClickListener(this);
        initComponents();

        return view;
    }

    private void initComponents() {

        resources = getResources();
        tvStatus.setText(getArguments().getString("status"));
        tvStatus.setTextColor(getArguments().getInt("textColor"));
        if (getArguments().getString("imageUrl").equals("")){
            ivBike.setImageResource(R.mipmap.ic_no_image);
        }else {
            getBikePhotos();
        }

        String bikeBrand = String.format(resources.getString(R.string.sr_itmtxt_bike_brand),
                getArguments().getString("brand"));
        tvBrand.setText(bikeBrand);
        String bikeType = String.format(resources.getString(R.string.sr_itmtxt_bike_type),
                getArguments().getString("type"));
        tvType.setText(bikeType);
        String bikeColor = String.format(resources.getString(R.string.sr_itmtxt_bike_color),
                getArguments().getString("color"));
        tvColor.setText(bikeColor);
    }

    private void getBikePhotos(){

        anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_charge_rotation);
        anim.setDuration(2000);
        ivBike.setAnimation(anim);
        String imageUrl = getArguments().getString("imageUrl");
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ivBike.setImageBitmap(bitmap);
                ivBike.getAnimation().cancel();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                ivBike.setImageResource(R.mipmap.ic_no_image);
                ivBike.getAnimation().cancel();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                ivBike.setImageResource(R.mipmap.ic_charge);
                ivBike.getAnimation().start();
            }
        };
        Picasso.with(getContext()).load(imageUrl).into(target);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.BtnSawBike){
            goToSawBikeFragment();
        }
    }

    private void goToSawBikeFragment() {

        FragmentManager fr = getFragmentManager();
        FragmentTransaction ft = fr.beginTransaction();
        ViewBikeFragment viewBikeFragment = new ViewBikeFragment();
        viewBikeFragment.setArguments(getArguments());
        ft.replace(R.id.RlyEvents, viewBikeFragment).addToBackStack(null).commit();
    }

    @Override
    public void onResume() {
        mvPlace.onResume();
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        String coordinates = getArguments().get("coordinates").toString();
        double latitude = Double.parseDouble(coordinates.substring(0,coordinates.indexOf(",")));
        double longittud = Double.parseDouble(coordinates.substring(coordinates.indexOf(",")+1,coordinates.length()));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longittud),17);
        googleMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longittud))
                .radius(50)
                .fillColor(getArguments().getInt("colorArea")));
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.animateCamera(cameraUpdate);
    }


}
