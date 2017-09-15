package com.bipo.javier.bipo.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.utils.RVItemTouchListener;
import com.bipo.javier.bipo.home.utils.RvEventsAdapter;
import com.bipo.javier.bipo.home.models.GetReportResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.report.models.Report;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private RecyclerView rvEvents;
    private String fhInicio = "";
    private String fhFin = "";
    private ImageView imgCharge, imgReload;
    private Animation anim;
    private TextView tvRedError;
    private static final String BASE_URL = "http://www.bipoapp.com/";

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        rvEvents = (RecyclerView)view.findViewById(R.id.RvEvents);
        tvRedError = (TextView)view.findViewById(R.id.TvRedError);
        imgReload = (ImageView)view.findViewById(R.id.ImgVReload);
        imgCharge = (ImageView)view.findViewById(R.id.ImgVCharge);
        imgCharge.setImageResource(R.mipmap.ic_charge);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_charge_rotation);
        anim.setDuration(2000);
        imgCharge.startAnimation(anim);
        reportList();

        return view;
    }

    private void goToItemEventFragment(Bundle arguments) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EventItemsFragment itemsFragment = new EventItemsFragment();
        itemsFragment.setArguments(arguments);
        ft.replace(R.id.RlyEvents,itemsFragment).addToBackStack(null).commit();
    }

    private void reportList() {

        HomeRepository repo = new HomeRepository(getContext());
        Call<GetReportResponse> call = repo.getLastReports();
        final GetReportResponse reportResponse = new GetReportResponse();
        call.enqueue(new Callback<GetReportResponse>() {
            @Override
            public void onResponse(retrofit.Response<GetReportResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    if (response.code() == 400) {
                        showMessage("No hay datos.");
                        System.out.println(response.isSuccess());
                        System.out.println(response.message());
                        System.out.println(response.code());
                        reportResponse.setMessage(response.message());
                    } else {
                        showMessage("Ocurrió un error en la red.");
                        System.out.println(reportResponse.getMessage());
                    }
                }
                if (response != null && response.isSuccess() && response.message() != null) {


                    if (response.body().getReports() != null) {
                        ArrayList<Report> reportList = response.body().getReports();
                        initEvents(reportList);
                        imgCharge.getAnimation().cancel();
                        imgCharge.setImageResource(0);
                    }else{
                        reportResponse.setMessage(response.body().getMessage());
                        tvRedError.setVisibility(View.VISIBLE);
                        tvRedError.setText("No hay bicicletas reportadas.");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                reportResponse.setMessage(t.getMessage());
                showRedError();

                //showMessage("No se pudo establecer la conexión de la red. " +
                  //      "Verifica que tengas conexión a internet.");
            }
        });
        //imgCharge.getAnimation().cancel();
        //imgCharge.setVisibility(View.INVISIBLE);
    }

    private void showRedError() {

        imgCharge.getAnimation().cancel();
        imgCharge.setImageResource(R.mipmap.ic_red_error);
        tvRedError.setVisibility(View.VISIBLE);
        tvRedError.setText("No se pudo establecer la conexión de la red. " +
                "Verifica que tengas conexión a internet.");
        imgReload.setVisibility(View.VISIBLE);
        imgReload.setImageResource(R.mipmap.ic_reload);
        imgReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                EventsFragment eventsFragment = new EventsFragment();
                ft.replace(R.id.RlyEvents, eventsFragment).commitAllowingStateLoss();
            }
        });
    }

    private void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void initEvents(final ArrayList<Report> reportList) {

        RvEventsAdapter rvAdapter = new RvEventsAdapter(getActivity(), reportList);
        rvEvents.setAdapter(rvAdapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEvents.addOnItemTouchListener(
                new RVItemTouchListener(getContext(), new RVItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int textColor;
                        int colorArea;
                        String imageUrl = "";
                        int idReport = reportList.get(position).getIdreportType();
                        String status = reportList.get(position).getReportType();
                        String brand = reportList.get(position).getBrand();
                        int idBike = reportList.get(position).getIdBike();
                        String type = reportList.get(position).getType();
                        String color = reportList.get(position).getColor();
                        String coordinates = reportList.get(position).getGooglemapscoordinate();


                        if (idReport == 1){

                            textColor = ContextCompat.getColor(getContext(),R.color.stolenBikeColor);
                            colorArea = ContextCompat.getColor(getContext(),R.color.stolen_bike_area);
                        }else if (idReport == 2){

                            textColor = ContextCompat.getColor(getContext(),R.color.recoveredBikeColor);
                            colorArea = ContextCompat.getColor(getContext(),R.color.recovered_bike_area);
                        }else{

                            textColor = ContextCompat.getColor(getContext(),R.color.darkBlue);
                            colorArea = ContextCompat.getColor(getContext(),R.color.darkBlue);
                        }

                        if (reportList.get(position).getReportPhotos().size() !=0) {
                            imageUrl = BASE_URL + reportList.get(position).getReportPhotos()
                                        .get(0).getUrl();
                        }else if (reportList.get(position).getBikePhotos().size() !=0) {
                            imageUrl = BASE_URL + reportList.get(position).getBikePhotos()
                                        .get(0).getUrl();
                        }




                        if(idReport == 1) {
                            //Argumentos del Bundle
                            Bundle arguments = new Bundle();
                            arguments.putString("imageUrl", imageUrl);
                            arguments.putString("status", status);
                            arguments.putString("brand", brand);
                            arguments.putString("type", type);
                            arguments.putString("color", color);
                            arguments.putInt("textColor", textColor);
                            arguments.putInt("colorArea", colorArea);
                            arguments.putInt("idBike",idBike);
                            arguments.putString("coordinates",coordinates);
                            goToItemEventFragment(arguments);
                        }
                    }
                })
        );
    }
}
