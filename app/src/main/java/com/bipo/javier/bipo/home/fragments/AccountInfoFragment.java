package com.bipo.javier.bipo.home.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.home.RVItemTouchListener;
import com.bipo.javier.bipo.home.RvBikesAdapter;
import com.bipo.javier.bipo.home.models.Bike;
import com.bipo.javier.bipo.home.models.GetBikesResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.login.activities.PassRestaurationActivity;
import com.bipo.javier.bipo.login.register.fragments.BikeFragment;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountInfoFragment extends Fragment implements View.OnClickListener{


    private ImageButton ibtnRigthButton;
    private Button btnNewBike;
    private RecyclerView rvBikes;
    private TextView tvAccountName, tvAccountLastName, tvAccountEmail, tvAccountDocument,
            tvAccountPhone, tvAccountPass;
    private EditText etAccountPhone;
    private android.content.res.Resources resources;
    private SharedPreferences preferences;

    public AccountInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);
        ibtnRigthButton = (ImageButton)getActivity().findViewById(R.id.ImbRight);
        ibtnRigthButton.setImageResource(R.mipmap.ic_edit);
        ibtnRigthButton.setTag(R.mipmap.ic_edit);
        ibtnRigthButton.setOnClickListener(this);
        resources = getResources();
        initComponets(view);
        return view;
    }

    private void initComponets(View view) {

        rvBikes = (RecyclerView)view.findViewById(R.id.RcvAccountBikes);
        btnNewBike = (Button)view.findViewById(R.id.BtnAccountNewBike);
        btnNewBike.setOnClickListener(this);
        tvAccountName = (TextView)view.findViewById(R.id.TvAccountName);
        tvAccountLastName = (TextView)view.findViewById(R.id.TvAccountLastName);
        tvAccountDocument = (TextView)view.findViewById(R.id.TvAccountId);
        tvAccountEmail = (TextView)view.findViewById(R.id.TvAccountEmail);
        tvAccountPhone = (TextView)view.findViewById(R.id.TvAccountPhone);
        etAccountPhone = (EditText)view.findViewById(R.id.EtAccountPhone);
        tvAccountPass = (TextView)view.findViewById(R.id.TvAccountPassword);
        tvAccountPass.setOnClickListener(this);
        preferences = getActivity().getSharedPreferences("UserInfo",0);
        String name = String.format(resources.getString(R.string.sr_account_info_name),
                preferences.getString("name",""));
        tvAccountName.setText(name);
        String lastName = String.format(resources.getString(R.string.sr_account_info_last_name),
                preferences.getString("lastName",""));
        tvAccountLastName.setText(lastName);
        String document = String.format(resources.getString(R.string.sr_account_info_document_id),
                preferences.getString("documentId",""));
        tvAccountDocument.setText(document);
        String email = String.format(resources.getString(R.string.sr_account_info_email),
                preferences.getString("email",""));
        tvAccountEmail.setText(email);
        String phone = String.format(resources.getString(R.string.sr_account_info_phone),
                preferences.getString("phone",""));
        tvAccountPhone.setText(phone);
        getAccountBikes();
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

        RvBikesAdapter rvBikesAdapter = new RvBikesAdapter(getContext(), bikesList);
        rvBikes.setAdapter(rvBikesAdapter);
        rvBikes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBikes.addOnItemTouchListener(
                new RVItemTouchListener(getContext(), new RVItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String status = bikesList.get(position).getBikeStatus();
                        String brand = bikesList.get(position).getBikeBrand();
                        String type = bikesList.get(position).getBikeType();
                        String color = bikesList.get(position).getBikeColor();
                        String idBike = bikesList.get(position).getBikeId();
                        boolean defaultBike = bikesList.get(position).isDefaultBike();
                        int image = R.drawable.wheel;
                        //String image = reportList.get(position).getReportPhotos().get(0);

                        //Argumentos del Bundle
                        Bundle arguments = new Bundle();
                        //arguments.putString("activity", "home");
                        arguments.putInt("image", image);
                        arguments.putString("status", status);
                        arguments.putString("brand", brand);
                        arguments.putString("type", type);
                        arguments.putString("color", color);
                        arguments.putBoolean("default", defaultBike);
                        arguments.putString("colorArea", idBike);
                        goToItemBikeFragment(arguments);
                    }
                })
        );
    }

   
    
    private void goToItemBikeFragment(Bundle arguments) {


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ImbRight:
                if (ibtnRigthButton.getTag().equals(R.mipmap.ic_edit)) {

                    editAccount();
                } else {

                    saveAccount();
                }
                break;
            case R.id.TvAccountPassword:
                goToPassRestaurationActivity();
                    break;
            case R.id.BtnAccountNewBike:
                newBike();
                break;
        }
    }

    private void newBike() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        BikeFragment bikeFragment = new BikeFragment();
        ft.replace(R.id.RlLytAccount,bikeFragment).commit();
    }

    private void goToPassRestaurationActivity() {

        Intent intent = new Intent(getActivity(), PassRestaurationActivity.class);
        startActivity(intent);
    }

    private void editAccount() {

        tvAccountPhone.setVisibility(View.INVISIBLE);
        etAccountPhone.setVisibility(View.VISIBLE);
        etAccountPhone.setText(tvAccountPhone.getText());
        tvAccountName.setTextColor(Color.GRAY);
        tvAccountLastName.setTextColor(Color.GRAY);
        tvAccountEmail.setTextColor(Color.GRAY);
        tvAccountDocument.setTextColor(Color.GRAY);
        ibtnRigthButton.setImageResource(R.mipmap.ic_save);
        ibtnRigthButton.setTag(R.mipmap.ic_save);
    }

    private void saveAccount() {

        showMessage("Guardando...");
        //TODO: Consumir servicio de actualizacion de datos del usuario.
        tvAccountPhone.setVisibility(View.VISIBLE);
        tvAccountPhone.setText(etAccountPhone.getText());
        etAccountPhone.setVisibility(View.INVISIBLE);
        etAccountPhone.getText().clear();
        tvAccountName.setTextColor(Color.BLACK);
        tvAccountLastName.setTextColor(Color.BLACK);
        tvAccountEmail.setTextColor(Color.BLACK);
        tvAccountDocument.setTextColor(Color.BLACK);
        ibtnRigthButton.setImageResource(R.mipmap.ic_edit);
        ibtnRigthButton.setTag(R.mipmap.ic_edit);
    }

    public void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
