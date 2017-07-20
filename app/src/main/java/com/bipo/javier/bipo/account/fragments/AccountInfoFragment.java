package com.bipo.javier.bipo.account.fragments;


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
import com.bipo.javier.bipo.home.utils.RVItemTouchListener;
import com.bipo.javier.bipo.home.utils.RvBikesAdapter;
import com.bipo.javier.bipo.account.models.Bike;
import com.bipo.javier.bipo.home.models.GetBikesResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.login.activities.PassRestaurationActivity;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountInfoFragment extends Fragment implements View.OnClickListener{


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
    private boolean bikesLimit;

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

        rvBikes = (RecyclerView)view.findViewById(R.id.RcvAccountBikes);
        btnNewBike = (Button)view.findViewById(R.id.BtnAccountNewBike);
        btnNewBike.setVisibility(View.INVISIBLE);
        bikesLimit = false;
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
        tvRedError = (TextView)view.findViewById(R.id.TvRedError);
        imgReload = (ImageView)view.findViewById(R.id.ImgVReload);
        imgCharge = (ImageView)view.findViewById(R.id.ImgVCharge);
        imgCharge.setImageResource(R.mipmap.ic_charge);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_charge_rotation);
        anim.setDuration(2000);
        imgCharge.startAnimation(anim);
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
                        btnNewBike.setVisibility(View.VISIBLE);
                        imgCharge.getAnimation().cancel();
                        imgCharge.setImageResource(0);
                    }else{
                        bikesResponse.setMessage(response.body().getMessage());
                        rvBikes.setVisibility(View.INVISIBLE);
                        btnNewBike.setVisibility(View.VISIBLE);
                        tvRedError.setVisibility(View.VISIBLE);
                        tvRedError.setText("No tienes bicicletas registradas.");
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

        if (bikesList.size() >= 3){
            //btnNewBike.setEnabled(false);
            bikesLimit = true;
            btnNewBike.setBackgroundColor(Color.GRAY);
        }
        RvBikesAdapter rvBikesAdapter = new RvBikesAdapter(getContext(), bikesList,R.layout.item_bikes);
        rvBikes.setAdapter(rvBikesAdapter);
        rvBikes.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,
                                                                                            false));
        rvBikes.addOnItemTouchListener(
                new RVItemTouchListener(getContext(), new RVItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //if ()
                        //showMessage("Tocando: " + view.getId());

                        String status = bikesList.get(position).getBikestate();
                        String brand = bikesList.get(position).getBrand();
                        String type = bikesList.get(position).getType();
                        String color = bikesList.get(position).getColor();
                        String name = bikesList.get(position).getBikename();
                        String idFrame = bikesList.get(position).getIdframe();
                        String features = bikesList.get(position).getBikefeatures();
                        int idBike = bikesList.get(position).getId();
                        boolean defaultBike = bikesList.get(position).isDefaultbike();
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
                        arguments.putString("bikeName", name);
                        arguments.putString("idFrame", idFrame);
                        arguments.putString("features", features);
                        arguments.putBoolean("default", defaultBike);
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
        ft.replace(R.id.RlyEvents,editBikeFragment).addToBackStack(null).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        this.menu = menu;
        inflater.inflate(R.menu.edit_menu, menu);
        menu.getItem(1).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals("Edit account")){
            editAccount();
        }else{
            saveAccount();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.TvAccountPassword:
                goToChangePass();
                    break;
            case R.id.BtnAccountNewBike:
                if (!bikesLimit){
                newBike();
                }else{
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
        ft.replace(R.id.RlyEvents,bikeFragment).addToBackStack(null).commit();
    }

    private void goToChangePass() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        ft.replace(R.id.RlyEvents,changePasswordFragment).addToBackStack(null).commit();
    }

    private void editAccount() {

        tvAccountPhone.setVisibility(View.INVISIBLE);
        etAccountPhone.setVisibility(View.VISIBLE);
        etAccountPhone.setHint(tvAccountPhone.getText());
        tvAccountName.setTextColor(Color.GRAY);
        tvAccountLastName.setTextColor(Color.GRAY);
        tvAccountEmail.setTextColor(Color.GRAY);
        tvAccountDocument.setTextColor(Color.GRAY);
        btnNewBike.setVisibility(View.INVISIBLE);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
        //ibtnRigthButton.setImageResource(R.mipmap.ic_save);
        //ibtnRigthButton.setTag(R.mipmap.ic_save);
    }

    private void saveAccount() {

        if (!validateCel()) {
            etAccountPhone.setError("Tu número de celular debe contener minimo 10 digitos.");
            return;
        }
        showMessage("Guardando...");
        //TODO: Consumir servicio de actualizacion de datos del usuario.
        tvAccountPhone.setVisibility(View.VISIBLE);
        String phone = String.format(resources.getString(R.string.sr_account_info_phone),
                etAccountPhone.getText());
        tvAccountPhone.setText(phone);
        etAccountPhone.setVisibility(View.INVISIBLE);
        etAccountPhone.getText().clear();
        tvAccountName.setTextColor(Color.BLACK);
        tvAccountLastName.setTextColor(Color.BLACK);
        tvAccountEmail.setTextColor(Color.BLACK);
        tvAccountDocument.setTextColor(Color.BLACK);
        btnNewBike.setVisibility(View.VISIBLE);
        menu.getItem(1).setVisible(false);
        menu.getItem(0).setVisible(true);
        //ibtnRigthButton.setImageResource(R.mipmap.ic_edit);
        //ibtnRigthButton.setTag(R.mipmap.ic_edit);
    }

    private boolean validateCel() {
        boolean ok = false;
        String num = etAccountPhone.getText().toString();
        if (!(num.length() < 10)) {
            ok = true;
        }
        return ok;
    }

    public void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
