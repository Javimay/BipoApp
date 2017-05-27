package com.bipo.javier.bipo.home.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bipo.javier.bipo.R;
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

import javax.annotation.Resources;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventItemsFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private TextView tvStatus, tvBrand, tvType, tvColor;
    private ImageView ivBike;
    private MapView mvPlace;
    private Button btnSawBike;
    private GoogleMap googleMap;
    private android.content.res.Resources resources;

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

    //TODO:Método para obtener los permisos de GPS del usuario.
    /*private void getMapLocation() {

        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        *//*if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);

        }

        MapsInitializer.initialize(this.getActivity());*//*

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        googleMap.animateCamera(cameraUpdate);
    }*/

    private void initComponents() {

        resources = getResources();
        tvStatus.setText(getArguments().getString("status"));
        tvStatus.setTextColor(getArguments().getInt("textColor"));
        ivBike.setImageResource(getArguments().getInt("image"));
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

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.BtnSawBike){
            //TODO:Recolectar datos y enviarlos al fragmento de Bicicleta vista
            goToSawBikeFragment();
        }
    }

    private void goToSawBikeFragment() {

    }

    @Override
    public void onResume() {
        mvPlace.onResume();
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(4.682344, -74.108904),17);
        googleMap.addCircle(new CircleOptions()
                .center(new LatLng(4.682344, -74.108904))
                .radius(50)
                .fillColor(getArguments().getInt("colorArea")));
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.animateCamera(cameraUpdate);

        this.googleMap = googleMap;
    }


}
