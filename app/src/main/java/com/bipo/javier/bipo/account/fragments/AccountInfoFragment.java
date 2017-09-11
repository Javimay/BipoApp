package com.bipo.javier.bipo.account.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.models.BikePhotos;
import com.bipo.javier.bipo.home.utils.RVItemTouchListener;
import com.bipo.javier.bipo.home.utils.RvBikesAdapter;
import com.bipo.javier.bipo.account.models.Bike;
import com.bipo.javier.bipo.home.models.GetBikesResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.login.activities.PassRestaurationActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountInfoFragment extends Fragment implements View.OnClickListener {


    private Button btnNewBike;
    private RecyclerView rvBikes;
    private TextView tvAccountName, tvAccountLastName, tvAccountEmail, tvAccountDocument,
            tvAccountPhone, tvAccountPass;
    private EditText etAccountPhone;
    private android.content.res.Resources resources;
    private SharedPreferences preferences;
    private Menu menu;
    private ImageView imgCharge, imgReload;
    private Animation anim;
    private TextView tvRedError;
    private boolean bikesLimit, isFirstBike = false;
    private static final String BASE_URL = "http://www.bipoapp.com/";

    public AccountInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_info, container, false);
        resources = getResources();
        initComponets(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void initComponets(View view) {

        rvBikes = (RecyclerView) view.findViewById(R.id.RcvAccountBikes);
        btnNewBike = (Button) view.findViewById(R.id.BtnAccountNewBike);
        btnNewBike.setVisibility(View.INVISIBLE);
        bikesLimit = false;
        btnNewBike.setOnClickListener(this);
        tvAccountName = (TextView) view.findViewById(R.id.TvAccountName);
        tvAccountLastName = (TextView) view.findViewById(R.id.TvAccountLastName);
        tvAccountDocument = (TextView) view.findViewById(R.id.TvAccountId);
        tvAccountEmail = (TextView) view.findViewById(R.id.TvAccountEmail);
        tvAccountPhone = (TextView) view.findViewById(R.id.TvAccountPhone);
        etAccountPhone = (EditText) view.findViewById(R.id.EtAccountPhone);
        tvAccountPass = (TextView) view.findViewById(R.id.TvAccountPassword);
        tvAccountPass.setOnClickListener(this);
        preferences = getActivity().getSharedPreferences("UserInfo", 0);
        String name = String.format(resources.getString(R.string.sr_account_info_name),
                preferences.getString("name", ""));
        tvAccountName.setText(name);
        String lastName = String.format(resources.getString(R.string.sr_account_info_last_name),
                preferences.getString("lastName", ""));
        tvAccountLastName.setText(lastName);
        String document = String.format(resources.getString(R.string.sr_account_info_document_id),
                preferences.getString("documentId", ""));
        tvAccountDocument.setText(document);
        String email = String.format(resources.getString(R.string.sr_account_info_email),
                preferences.getString("email", ""));
        tvAccountEmail.setText(email);
        String phone = String.format(resources.getString(R.string.sr_account_info_phone),
                preferences.getString("phone", ""));
        tvAccountPhone.setText(phone);
        tvRedError = (TextView) view.findViewById(R.id.TvRedError);
        imgReload = (ImageView) view.findViewById(R.id.ImgVReload);
        imgCharge = (ImageView) view.findViewById(R.id.ImgVCharge);
        imgCharge.setImageResource(R.mipmap.ic_charge);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_charge_rotation);
        anim.setDuration(2000);
        imgCharge.startAnimation(anim);
        getAccountBikes();
    }

    private void getAccountBikes() {

        String token = preferences.getString("token", "");
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
                        imgCharge.getAnimation().cancel();
                        imgCharge.setImageResource(0);
                    } else {
                        showMessage("Ocurrió un error en la red.");
                        System.out.println(bikesResponse.getMessage());
                        imgCharge.getAnimation().cancel();
                        imgCharge.setImageResource(0);
                    }
                }
                if (response != null && response.isSuccess() && response.message() != null) {


                    if (response.body().getBikes() != null) {
                        ArrayList<Bike> bikesList = response.body().getBikes();
                        initBikes(bikesList);
                        btnNewBike.setVisibility(View.VISIBLE);
                        imgCharge.getAnimation().cancel();
                        imgCharge.setImageResource(0);
                        isFirstBike = false;
                    } else {
                        bikesResponse.setMessage(response.body().getMessage());
                        imgCharge.getAnimation().cancel();
                        imgCharge.setImageResource(0);
                        rvBikes.setVisibility(View.INVISIBLE);
                        btnNewBike.setVisibility(View.VISIBLE);
                        tvRedError.setVisibility(View.VISIBLE);
                        tvRedError.setText("No tienes bicicletas registradas.");
                        isFirstBike = true;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                bikesResponse.setMessage(t.getMessage());
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
                AccountInfoFragment accountInfoFragment = new AccountInfoFragment();
                ft.replace(R.id.RlyEvents, accountInfoFragment).addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });
    }

    public void initBikes(final ArrayList<Bike> bikesList) {

        if (bikesList.size() >= 3) {
            //btnNewBike.setEnabled(false);
            bikesLimit = true;
            btnNewBike.setBackgroundColor(Color.GRAY);
        }
        final RvBikesAdapter rvBikesAdapter = new RvBikesAdapter(getContext(), bikesList, R.layout.item_bikes);
        rvBikes.setAdapter(rvBikesAdapter);
        rvBikes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false));
        rvBikes.addOnItemTouchListener(
                new RVItemTouchListener(getContext(), new RVItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String imageUrl = "";

                        String status = bikesList.get(position).getBikestate();
                        String brand = bikesList.get(position).getBrand();
                        String type = bikesList.get(position).getType();
                        String color = bikesList.get(position).getColor();
                        String name = bikesList.get(position).getBikename();
                        String idFrame = bikesList.get(position).getIdframe();
                        String features = bikesList.get(position).getBikefeatures();
                        int idBike = bikesList.get(position).getId();
                        int defaultBike = bikesList.get(position).getIsDefault();

                        if (bikesList.get(position).getBikePhotos() != null) {
                            if (bikesList.get(position).getBikePhotos().size() != 0) {
                                imageUrl = BASE_URL + bikesList.get(position).getBikePhotos()
                                        .get(0).getUrl();
                            }
                        }
                        //boolean defaultBike = true;

                        //Argumentos del Bundle
                        Bundle arguments = new Bundle();
                        arguments.putString("imageUrl", imageUrl);
                        arguments.putString("status", status);
                        arguments.putString("brand", brand);
                        arguments.putString("type", type);
                        arguments.putString("color", color);
                        arguments.putString("bikeName", name);
                        arguments.putString("idFrame", idFrame);
                        arguments.putString("features", features);
                        arguments.putInt("default", defaultBike);
                        arguments.putInt("id", idBike);
                        goToItemBikeFragment(arguments);
                    }


                })
        );

    }


    private void goToItemBikeFragment(Bundle arguments) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EditBikeFragment editBikeFragment = new EditBikeFragment();
        editBikeFragment.setArguments(arguments);
        ft.replace(R.id.RlyEvents, editBikeFragment).addToBackStack(null).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        this.menu = menu;
        inflater.inflate(R.menu.settings_menu, menu);
        //menu.getItem(1).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.TvAccountPassword:
                goToChangePass();
                break;
            case R.id.BtnAccountNewBike:
                if (!bikesLimit) {
                    newBike();
                } else {
                    showMessage("Has alcanzado el limite de bicicletas. " +
                            "\nBorra una para registrar una nueva");
                }
                break;
        }
    }

    private void newBike() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        BikeFragment bikeFragment = new BikeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFirstBike", isFirstBike);
        bikeFragment.setArguments(bundle);
        ft.replace(R.id.RlyEvents, bikeFragment).addToBackStack(null).commit();
    }

    private void goToChangePass() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        ft.replace(R.id.RlyEvents, changePasswordFragment).addToBackStack(null).commit();
    }

    public void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
