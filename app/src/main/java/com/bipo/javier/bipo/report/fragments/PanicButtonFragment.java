package com.bipo.javier.bipo.report.fragments;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationListener;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.fragments.BikeFragment;
import com.bipo.javier.bipo.account.models.Bike;
import com.bipo.javier.bipo.home.fragments.SettingsFragment;
import com.bipo.javier.bipo.home.models.GetBikesResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.login.activities.LoginActivity;
import com.bipo.javier.bipo.splashScreen.SplashScreenActivity;
import com.bipo.javier.bipo.utils.GpsConnection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;


/**
 * A simple {@link Fragment} subclass.
 */
public class PanicButtonFragment extends Fragment implements LocationListener {

    private ProgressBar progressBar;
    private ImageButton imgBtnPanicButton;
    private Runnable rotationRunnable = null;
    private ObjectAnimator anim;
    private SharedPreferences preferences;
    private View view;
    private double latitude, longitude;
    private Animation animation;
    private LocationManager locationManager;
    private boolean isUserActive = false;
    private int idBike;
    private String GPS = LocationManager.GPS_PROVIDER;
    private String NETWORK = LocationManager.NETWORK_PROVIDER;

    public PanicButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_panic_button, container, false);
        preferences = getActivity().getSharedPreferences("UserInfo",0);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        getGPSPermission(GPS);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_charge_rotation);
        animation.setDuration(2000);
        getAccountBikes();
        initComponets();
        setHasOptionsMenu(true);
        return view;

    }

    public void getGPSPermission(String provider) {

        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

            //locationManager.requestLocationUpdates(provider, 0, 0, this);
            locationManager.requestLocationUpdates(GPS, 0, 0, this);
            locationManager.requestLocationUpdates(NETWORK, 0, 0, this);

        }
    }

    private boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        return (res == PERMISSION_GRANTED);
    }

    private void initComponets() {

        imgBtnPanicButton = (ImageButton)view.findViewById(R.id.ImgBtnPanicButton);
        progressBar = (ProgressBar)view.findViewById(R.id.PgBarPanicButton);
        imgBtnPanicButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    anim = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), 100);
                    anim.setDuration(4000);
                    anim.setInterpolator(new DecelerateInterpolator());
                    anim.start();

                    v.post(rotationRunnable);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (progressBar.getProgress() == 100) {

                        GpsConnection gps = new GpsConnection(getActivity(), getContext());
                        if (gps.isGpsEnabled()){

                            progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(),
                                    R.drawable.ok_circular_progress_bar));
                            anim = ObjectAnimator.ofInt(progressBar, "progress", 99, 100);
                            anim.start();
                            isUserActive = true;
                            sendReport();
                        }else{
                            turnOnGps();
                        }

                    } else {
                        anim.cancel();
                        anim = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), 0);
                        anim.setDuration(2000);
                        anim.setInterpolator(new DecelerateInterpolator());
                        anim.start();
                    }
                }

                return true;
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
                        if (bikesList.size() == 0) {
                            validateBikes();
                        }else {
                            for (Bike bike : bikesList) {
                                if (bike.isDefaultbike()) {
                                    idBike = bike.getId();
                                }
                            }
                        }
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

    private void validateBikes() {

            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("No tienes bicicletas registradas.");
            alert.setMessage("No puedes realizar ningún reporte sin tener alguna bicicleta registrada.")
                    .setPositiveButton("Registrar una bicicleta", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            BikeFragment bikeFragment = new BikeFragment();
                            ft.add(R.id.RlyEvents,bikeFragment).commit();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            getActivity().onBackPressed();
                        }
                    });
            alert.show();
    }

    private void sendReport() {

            if (latitude == 0 && longitude == 0) {

                showMessage("Buscando tu ubicación...");
                imgBtnPanicButton.setImageResource(R.mipmap.ic_charge);
                imgBtnPanicButton.setAnimation(animation);
                imgBtnPanicButton.getAnimation().start();
                imgBtnPanicButton.setEnabled(false);
            } else {

                showMessage("Ubicación encontrada!");
                System.out.println("Latitude: " + latitude + "\nLongitude: " + longitude);
                imgBtnPanicButton.setEnabled(true);
                isUserActive = false;
                progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(),
                        R.drawable.circular_progress_bar));
                //anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
                progressBar.setProgress(0);
                createReport();
            }
    }

    private void createReport() {

        String token = preferences.getString("token", "");
        String date = getDate();
        String userName = preferences.getString("userName", "");
        String reportName = date + "_" + userName + "_" + idBike;
        String coordinates = latitude + "," + longitude;
        //TODO: Crear reporte en el servidor.
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Se ha generado un reporte de robo.");
        alert.setMessage("Estaremos pendientes de cualquier noticia sobre tu bicicleta robada. " +
                "\nTe enviaremos un correo si sabemos algo de ella."+
                "\nCoordenadas: " + coordinates + "\nReportName: " + reportName)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressBar.setProgress(0);
                        progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(),
                                R.drawable.circular_progress_bar));
                        anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
                        getActivity().onBackPressed();
                    }
                });
        alert.show();
    }

    private String getDate() {
        String currentDate = "";
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return currentDate = dateFormat(year, month, day);
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

    private void goToMakeReport() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MakeReportFragment reportFragment = new MakeReportFragment();
        ft.replace(R.id.RlyEvents, reportFragment).addToBackStack(null).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.create_report, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals("Crear reporte")) {
            goToMakeReport();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void turnOnGps() {

        int off = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                off = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            System.out.println("SettingNotFoundException: " + e.getMessage());
        }
        if (off == 0 || off == 2) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("No hay acceso al  GPS.");
            alert.setMessage("bipo no tiene acceso al GPS. " +
                    "\nActiva el GPS para que bipo pueda obtener tu ubicación.")
                    .setPositiveButton("Activar el acceso a la ubicación", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(onGPS);
                            progressBar.setProgress(0);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(),
                                    R.drawable.circular_progress_bar));
                            anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);

                        }
                    });
            alert.show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (isUserActive){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            imgBtnPanicButton.getAnimation().cancel();
            imgBtnPanicButton.setImageResource(R.drawable.panic_button);
            locationManager.removeUpdates(this);
            sendReport();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        turnOnGps();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }
}
