package com.bipo.javier.bipo.report.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.models.Bike;
import com.bipo.javier.bipo.account.models.BikesResponse;
import com.bipo.javier.bipo.home.utils.RVItemTouchListener;
import com.bipo.javier.bipo.home.utils.RvBikesAdapter;
import com.bipo.javier.bipo.home.models.GetBikesResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.login.utilities.Teclado;
import com.bipo.javier.bipo.report.models.ReportType;
import com.bipo.javier.bipo.report.models.ReportTypesResponse;
import com.bipo.javier.bipo.report.models.ReportRepository;
import com.bipo.javier.bipo.utils.ReportTypesSpinner;
import com.bipo.javier.bipo.utils.GpsConnection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeReportFragment extends Fragment implements View.OnClickListener{

    private RecyclerView rvUserBikes;
    private SharedPreferences preferences;
    private android.content.res.Resources resources;
    private TextView tvBikeName, tvBikeFrame;
    private EditText etBikeDetails, etAddresReport;
    private Spinner spReportType;
    private ReportTypesSpinner reportTypesSpinner;
    private Button btnCreateReport;
    private Animation anim;
    private Geocoder geocoder;
    private GpsConnection gpsConnection;
    private String bikeState;
    private ImageButton btnGetAddress;
    private final int PLACE_PICKER_REQUEST = 1;
    private double longitude, latitude;
    private int idBike;
    private Long idReportType;

    public MakeReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_report, container, false);
        rvUserBikes = (RecyclerView)view.findViewById(R.id.RcvUserBikes);
        tvBikeName = (TextView)view.findViewById(R.id.TvBikeNameReport);
        tvBikeFrame = (TextView)view.findViewById(R.id.TvBikeFrameReport);
        etAddresReport = (EditText)view.findViewById(R.id.EtAddressReport);
        btnCreateReport = (Button)view.findViewById(R.id.BtnCreateReport);
        btnCreateReport.setOnClickListener(this);
        etBikeDetails = (EditText)view.findViewById(R.id.EtBikeDetailsReport);
        spReportType = (Spinner)view.findViewById(R.id.SpReportType);
        preferences = getActivity().getSharedPreferences("UserInfo",0);
        resources = getContext().getResources();
        btnGetAddress = (ImageButton) view.findViewById(R.id.ImgBtnAdressRep);
        btnGetAddress.setOnClickListener(this);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_charge_rotation);
        anim.setDuration(2000);
        gpsConnection = new GpsConnection(getActivity(), getContext());
        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        gpsConnection.getGPSPermission();
        getReportTypes();
        getAccountBikes();
        return view;
    }

    public void reportTypesSpinner(List<String> reportTypes) {

        //Tipos de reporte
        reportTypesSpinner = new ReportTypesSpinner(getContext(), reportTypes);
        spReportType.setOnItemSelectedListener(reportTypesSpinner.getListener());
        spReportType.setAdapter(reportTypesSpinner.getAdapter());
        //idReportType = reportTypesSpinner.getIdReportType();
    }

    public void getReportTypes() {

        ReportRepository repo = new ReportRepository(getContext());
        final ReportTypesResponse bikeStatesResponse = new ReportTypesResponse();
        Call<ReportTypesResponse> call = repo.getReportTypes();
        call.enqueue(new Callback<ReportTypesResponse>() {
            @Override
            public void onResponse(retrofit.Response<ReportTypesResponse> response, Retrofit retrofit) {
                //dialogFragment.dismiss();
                //String error = "Error desconocido, verifique su conexión a internet o contacte al administrador";
                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    System.out.println(response.isSuccess());
                    System.out.println(response.message());
                    System.out.println(response.code());
                    bikeStatesResponse.setMessage(response.message());
                }
                if (response != null && response.isSuccess() && response.message() != null) {

                    List<ReportType> reports = response.body().brands;
                    List<String> listStates = new ArrayList<>();
                    listStates.add(0, "Escoge un tipo.");
                    for (ReportType bike : reports) {

                        listStates.add(bike.getId(), bike.getReportType());
                        System.out.println("id: " + bike.getId() + "\nstate: " + bike.getReportType());
                    }
                    reportTypesSpinner(listStates);
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
            }
        });
    }

    private void getAccountBikes() {

        String token = preferences.getString("token","");
        HomeRepository repo = new HomeRepository(getContext());
        Call<GetBikesResponse> call = repo.getAccountBikes(token);
        final GetBikesResponse bikesResponse = new GetBikesResponse();
        call.enqueue(new Callback<GetBikesResponse>() {
            @Override
            public void onResponse(retrofit.Response<GetBikesResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    if (response.code() == 400) {
                        showMessage("No hay datos.");
                        System.out.println(response.isSuccess());
                        System.out.println(response.message());
                        System.out.println(response.code());
                        bikesResponse.setMessage(response.message());
                    } else {
                        showMessage("Ocurrió un error en la red.");
                        System.out.println(bikesResponse.getMessage());
                    }
                }
                if (response != null && response.isSuccess() && response.message() != null) {


                    if (response.body().getBikes() != null) {
                        ArrayList<Bike> bikesList = response.body().getBikes();
                        initBikes(bikesList);
                    }else{
                        bikesResponse.setMessage(response.body().getMessage());
                        showMessage("No tienes bicicletas registradas.");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                bikesResponse.setMessage(t.getMessage());
                showMessage("No se pudo establecer la conexión de la red. " +
                        "Verifica que tengas conexión a internet.");
            }
        });
    }

    public void initBikes(final ArrayList<Bike> bikesList) {

        RvBikesAdapter rvBikesAdapter = new RvBikesAdapter(getContext(), bikesList,
                                                        R.layout.item_bikes_report);
        rvUserBikes.setAdapter(rvBikesAdapter);
        rvUserBikes.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,
                false));
        rvUserBikes.addOnItemTouchListener(
                new RVItemTouchListener(getContext(), new RVItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String bikeName = String.format(resources.getString(R.string.sr_itmtxt_bike_name),
                                bikesList.get(position).getBikename());
                        tvBikeName.setText(bikeName);

                        String bikeId = String.format(resources.getString(R.string.sr_itmtxt_bike_id),
                                bikesList.get(position).getIdframe());
                        tvBikeFrame.setText(bikeId);
                        idBike = bikesList.get(position).getId();
                        bikeState = bikesList.get(position).getBikestate();
                    }
                })
        );
    }

    public void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.BtnCreateReport){
            createReport();
        }
        if (v.getId() == R.id.ImgBtnAdressRep){
            if(gpsConnection.isGpsEnabled()){
                initPlacePicker();
            }else {
                gpsConnection.turnOnGps();
                //etAddresReport.requestFocus();
            }
        }
    }

    private void initPlacePicker() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            System.out.println("PlacePicker error: " + e.getMessage());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            getActivity();
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
                //showMessage(String.format("Place: %s", place.getName()));
                etAddresReport.setText(place.getAddress());
                System.out.println("Lat: " + place.getLatLng().latitude +
                        "\nLong: " + place.getLatLng().longitude +
                        "\nAtrib: " + place.getAttributions() +
                        "\nId: " + place.getId() + "\nPhone: " + place.getName() +
                        "\nName: " + place.getName() + "\nPlace: " + place.getAddress().toString());
                latitude = place.getLatLng().latitude;
                longitude =  place.getLatLng().longitude;
            }
        }
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return dateFormat(year, month, day);
    }

    public String dateFormat(int year, int month, int day) {

        month += 1;
        String monthFormat = String.valueOf(month);
        String dayFormat = String.valueOf(day);
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

    private void getCoordinatesFromAddress(String address)throws IOException{

        List<Address> addresses = null;
            addresses = geocoder.getFromLocationName(address, 1);
        if (addresses.size() > 0) {
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
            System.out.println("Manual: \tLatitud:" + latitude + "\tLongitud: " + longitude);
        }
    }

    private void createReport() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        idReportType = reportTypesSpinner.getIdReportType();
        String coordinates = "0,0";
        if (idBike ==0){
            showMessage("Selecciona una bicicleta por favor.");
            return;
        }
        if (idReportType == 0){
            showMessage("Por favor escoge un tipo de reporte.");
            return;
        }
        if(idReportType == 1 || idReportType == 4){

            if (latitude == 0 && longitude ==0){
                if (TextUtils.isEmpty(etAddresReport.getText())){
                    etAddresReport.setError("Agrégue una dirección por favor.");
                    return;
                }else{
                    try {
                        getCoordinatesFromAddress(etAddresReport.getText().toString());
                    }catch (IOException e) {
                        e.printStackTrace();
                        showMessage("No se pudo encontrar las coordenadas de la dirección" +
                                "\nPor favor utiliza el icono de GPS para seleccionar una ubicación.");
                        return;
                    }
                }
            }else{
                System.out.println("bipoGPS: \tLatitud:" + latitude + "\tLongitud: " + longitude);
            }
            coordinates = latitude + "," + longitude;
            if(TextUtils.isEmpty(etBikeDetails.getText())){
                etBikeDetails.setError("Describe los hechos por favor.");
                return;
            }
        }else{

            if ((idReportType == 2) && (!bikeState.equals("Robada"))){

                alert.setTitle("No se puede generar este reporte.");
                alert.setMessage("La bicicleta seleccionada no ha sido reportada como robada. " +
                        "Por lo tanto, no se puede reportar como recuperada.");
                alert.show();
                return;
            }
        }

        String token = preferences.getString("token", "");
        String date = getDate();
        String userName = preferences.getString("userName", "");
        String reportName = date + "_" + userName + "_" + idBike;
        String bikeDetails = etBikeDetails.getText().toString();
        Teclado.ocultarTeclado(getActivity());
        makeReport(token, reportName, idReportType.intValue(), coordinates, idBike, bikeDetails);
    }

    private void makeReport(final String token, final String reportName, int reportType,
                                String coordinates, int idBike, String reportDetails) {

        HomeRepository repo = new HomeRepository(getContext());
        Call<BikesResponse> call = repo.registerReport(token, reportName, reportType, coordinates,
                idBike, reportDetails);
        final BikesResponse reportResponse = new BikesResponse();
        call.enqueue(new Callback<BikesResponse>() {
            @Override
            public void onResponse(retrofit.Response<BikesResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    if (response.code() == 400) {
                        showMessage("hubo un error al enviar los datos");
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

                    if (response.body().getError().equals("false")) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Se ha generado un reporte.");
                        alert.setMessage("El reporte se ha generado exitosamente. " +
                                "\nGracias por utilizar bipo.")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        getActivity().onBackPressed();
                                    }
                                });
                    alert.show();

                    } else {
                        reportResponse.setMessage(response.body().getMessage());
                        System.out.println("Error: " + reportResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                reportResponse.setMessage(t.getMessage());
                showMessage("No se pudo establecer la conexión de la red. " +
                        "Verifica que tengas conexión a internet.");
            }
        });
    }
}
