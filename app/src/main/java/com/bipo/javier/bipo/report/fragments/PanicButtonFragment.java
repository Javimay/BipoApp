package com.bipo.javier.bipo.report.fragments;


import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.fragments.BikeFragment;
import com.bipo.javier.bipo.home.fragments.SettingsFragment;
import com.bipo.javier.bipo.home.models.GetBikesResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class PanicButtonFragment extends Fragment implements View.OnClickListener{

    private ProgressBar progressBar;
    private ImageButton imgBtnPanicButton;
    private Runnable rotationRunnable = null;
    private ObjectAnimator anim;
    private SharedPreferences preferences;
    private View view;

    public PanicButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_panic_button, container, false);
        preferences = getActivity().getSharedPreferences("UserInfo",0);
        getAccountBikes();
        initComponets();
        setHasOptionsMenu(true);
        return view;

    }

    private void initComponets() {

        imgBtnPanicButton = (ImageButton)view.findViewById(R.id.ImgBtnPanicButton);
        progressBar = (ProgressBar)view.findViewById(R.id.PgBarPanicButton);
        imgBtnPanicButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
                    anim.setDuration(4000);
                    anim.setInterpolator(new DecelerateInterpolator());
                    anim.start();

                    v.post(rotationRunnable);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (progressBar.getProgress() == 100) {

                        progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(),
                                R.drawable.ok_circular_progress_bar));
                        anim = ObjectAnimator.ofInt(progressBar, "progress", 99, 100);
                        anim.start();

                        sendReport();
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

                    if (response.body().getBikes() == null) {
                        //ArrayList<Bike> bikesList = response.body().getBikes();
                        validateBikes(0);
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

    private void validateBikes(int bikes) {

        if (bikes == 0){

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

                            //TODO: Volver al fragmento anterior.
                            getActivity().onBackPressed();
                        }
                    });
            alert.show();
        }
    }

    private void sendReport() {

        showMessage("Creando Reporte.");
        //TODO: Des comentar para cambiar el color de la ProgressBar al finalizar el servicio.
        /*progressBar.setProgressDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.circular_progress_bar));*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.TvMakeReport){
            goToMakeReport();
        }
    }

    private void goToMakeReport() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MakeReportFragment reportFragment = new MakeReportFragment();
        ft.replace(R.id.RlyEvents, reportFragment).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
