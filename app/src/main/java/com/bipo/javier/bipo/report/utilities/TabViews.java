package com.bipo.javier.bipo.report.utilities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.bipo.javier.bipo.home.utilities.RvEventsAdapter;
import com.bipo.javier.bipo.home.fragments.EventItemsFragment;
import com.bipo.javier.bipo.home.models.GetReportResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.report.fragments.ReportBikesFragment;
import com.bipo.javier.bipo.report.models.Report;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabViews extends Fragment {

    private RecyclerView rvViewBikes;
    private TextView tvNoItem;
    private String fhInicio = "";
    private String fhFin = "";
    private final int VIEW_BIKES_TYPE = 4;
    private ImageView imgCharge, imgReload;
    private Animation anim;
    private TextView tvRedError;

    public TabViews() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_views, container, false);
        rvViewBikes = (RecyclerView)view.findViewById(R.id.RvViewBikes);
        tvNoItem = (TextView)view.findViewById(R.id.TvNoView);
        tvRedError = (TextView)view.findViewById(R.id.TvRedError);
        imgReload = (ImageView)view.findViewById(R.id.ImgVReload);
        imgCharge = (ImageView)view.findViewById(R.id.ImgVChargeEditB);
        imgCharge.setImageResource(R.mipmap.ic_charge);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_charge_rotation);
        anim.setDuration(2000);
        imgCharge.startAnimation(anim);
        stolenList();
        return view;
    }

    private void stolenList() {

        initDates();
        HomeRepository repo = new HomeRepository(getContext());
        Call<GetReportResponse> call = repo.getReports(VIEW_BIKES_TYPE, fhInicio, fhFin);
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
                        rvViewBikes.setVisibility(View.INVISIBLE);
                        tvNoItem.setVisibility(View.VISIBLE);
                        tvNoItem.setText("No hay bicicletas vistas.");
                        imgCharge.getAnimation().cancel();
                        imgCharge.setImageResource(0);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                reportResponse.setMessage(t.getMessage());
                showRedError();
            }
        });
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
                ReportBikesFragment reportBikesFragment = new ReportBikesFragment();
                ft.replace(R.id.RlyEvents, reportBikesFragment).addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
    }

    private void initDates() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year,month,day);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        calendar.getTime();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        fhFin = dateFormat(year, month, day);
        System.out.println("Fecha final: " + fhFin);

        calendar.set(year, month, day);
        calendar.add(Calendar.MONTH, -3); //Resta 3 Meses de la fecha actual.
        calendar.getTime();
        int inYear = calendar.get(Calendar.YEAR);
        int inMonth = calendar.get(Calendar.MONTH);
        int inDay = calendar.get(Calendar.DAY_OF_MONTH);
        fhInicio = dateFormat(inYear, inMonth, inDay);
        System.out.println(fhInicio);
    }

    public String dateFormat(int year, int month, int day) {

        month += 1;
        String monthFormat = String.valueOf(month);
        //String monthFormat = "";
        String dayFormat = String.valueOf(day);
        //String dayFormat = "";
        String date = "";
        if (month < 10) {
            monthFormat = "0" + month;
        }
        if (day < 10) {
            dayFormat = "0" + day;
        }
        date = "" + year + monthFormat + dayFormat;
        return date;
    }

    public void initEvents(final ArrayList<Report> reportList) {


        RvEventsAdapter rvAdapter = new RvEventsAdapter(getActivity(), reportList);
        rvViewBikes.setAdapter(rvAdapter);
        rvViewBikes.setLayoutManager(new LinearLayoutManager(getContext()));
        /*rvViewBikes.addOnItemTouchListener(
                new RVItemTouchListener(getContext(), new RVItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int textColor;
                        int colorArea;
                        int idReport = reportList.get(position).getIdreportType();
                        String status = reportList.get(position).getReportType();
                        String brand = reportList.get(position).getBrand();
                        String type = reportList.get(position).getType();
                        String color = reportList.get(position).getColor();
                        int image = R.drawable.wheel;
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
                        //String image = reportList.get(position).getReportPhotos().get(0);

                        //Argumentos del Bundle
                        Bundle arguments = new Bundle();
                        arguments.putString("activity", "reportBikes");
                        arguments.putInt("image", image);
                        arguments.putString("status", status);
                        arguments.putString("brand", brand);
                        arguments.putString("type", type);
                        arguments.putString("color", color);
                        arguments.putInt("textColor", textColor);
                        arguments.putInt("colorArea", colorArea);
                        goToItemEventFragment(arguments);
                    }
                })
        );*/
    }

    private void goToItemEventFragment(Bundle arguments) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EventItemsFragment itemsFragment = new EventItemsFragment();
        itemsFragment.setArguments(arguments);
        ft.replace(R.id.pager,itemsFragment).commit();
    }

    private void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
