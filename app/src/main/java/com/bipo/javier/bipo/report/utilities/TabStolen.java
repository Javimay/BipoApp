package com.bipo.javier.bipo.report.utilities;


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
import com.bipo.javier.bipo.home.utilities.RVItemTouchListener;
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
public class TabStolen extends Fragment {

    private RecyclerView rvStolenBikes;
    private TextView tvNoItem;
    private String fhInicio = "";
    private String fhFin = "";
    private final int STOLEN_BIKES_TYPE = 1;
    private ImageView imgCharge, imgReload;
    private Animation anim;
    private TextView tvRedError;
    private static final String BASE_URL = "http://www.bipoapp.com/";

    public TabStolen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_stolen, container, false);
        rvStolenBikes = (RecyclerView)view.findViewById(R.id.RvStolenBikes);
        tvNoItem = (TextView)view.findViewById(R.id.TvNoStolen);
        tvRedError = (TextView)view.findViewById(R.id.TvRedError);
        imgReload = (ImageView)view.findViewById(R.id.ImgVReload);
        imgCharge = (ImageView)view.findViewById(R.id.ImgVChargeEditB);
        imgCharge.setImageResource(R.mipmap.ic_charge);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_charge_rotation);
        anim.setDuration(2000);
        imgCharge.startAnimation(anim);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        stolenList();
    }

    private void stolenList() {

        initDates();
        HomeRepository repo = new HomeRepository(getContext());
        Call<GetReportResponse> call = repo.getReports(STOLEN_BIKES_TYPE, fhInicio, fhFin);
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
                        if (imgCharge.getAnimation() != null) {
                            imgCharge.getAnimation().cancel();
                            imgCharge.setImageResource(0);
                        }
                    }else{
                        reportResponse.setMessage(response.body().getMessage());
                        rvStolenBikes.setVisibility(View.INVISIBLE);
                        tvNoItem.setVisibility(View.VISIBLE);
                        tvNoItem.setText("No hay bicicletas robadas.");
                        if (imgCharge.getAnimation() != null) {
                            imgCharge.getAnimation().cancel();
                            imgCharge.setImageResource(0);
                        }
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
        calendar.add(Calendar.MONTH, -3); //Resta 3 meses de la fecha actual.
        calendar.getTime();
        int inYear = calendar.get(Calendar.YEAR);
        int inMonth = calendar.get(Calendar.MONTH);
        int inDay = calendar.get(Calendar.DAY_OF_MONTH);
        fhInicio = dateFormat(inYear, inMonth, inDay);
        System.out.println("Fecha Inicio: " + fhInicio);
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
        rvStolenBikes.setAdapter(rvAdapter);
        rvStolenBikes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStolenBikes.addOnItemTouchListener(
                new RVItemTouchListener(getContext(), new RVItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int textColor;
                        int colorArea;
                        String imageUrl = "";
                        int idReport = reportList.get(position).getIdreportType();
                        String status = reportList.get(position).getReportType();
                        String bikeOwner = reportList.get(position).getBike_owner();
                        String bikeFeatures = reportList.get(position).getBikeFeatures();
                        String brand = reportList.get(position).getBrand();
                        int idBike = reportList.get(position).getIdBike();
                        String type = reportList.get(position).getType();
                        String color = reportList.get(position).getColor();
                        String coordinates = reportList.get(position).getGooglemapscoordinate();
                        String details = reportList.get(position).getReportDetails();
                        textColor = ContextCompat.getColor(getContext(),R.color.stolenBikeColor);
                        colorArea = ContextCompat.getColor(getContext(),R.color.stolen_bike_area);


                        if (reportList.get(position).getReportPhotos().size() !=0) {
                            imageUrl = BASE_URL + reportList.get(position).getReportPhotos()
                                    .get(0).getUrl();
                        }else if (reportList.get(position).getBikePhotos().size() !=0) {
                            imageUrl = BASE_URL + reportList.get(position).getBikePhotos()
                                    .get(0).getUrl();
                        }

                        //Argumentos del Bundle
                        Bundle arguments = new Bundle();
                        arguments.putString("imageUrl", imageUrl);
                        arguments.putInt("idReport", idReport);
                        arguments.putString("status", status);
                        arguments.putString("bikeOwner", bikeOwner);
                        arguments.putString("bikeFeatures", bikeFeatures);
                        arguments.putString("brand", brand);
                        arguments.putString("type", type);
                        arguments.putString("color", color);
                        arguments.putString("details", details);
                        arguments.putInt("textColor", textColor);
                        arguments.putInt("colorArea", colorArea);
                        arguments.putInt("idBike", idBike);
                        arguments.putString("coordinates", coordinates);
                        goToItemEventFragment(arguments);
                    }
                })
        );
    }

    private void goToItemEventFragment(Bundle arguments) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EventItemsFragment itemsFragment = new EventItemsFragment();
        itemsFragment.setArguments(arguments);
        ft.replace(R.id.RlyEvents,itemsFragment).addToBackStack(null).commit();
    }

    private void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
