package com.bipo.javier.bipo.report.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.models.Bike;
import com.bipo.javier.bipo.home.utils.RVItemTouchListener;
import com.bipo.javier.bipo.home.utils.RvBikesAdapter;
import com.bipo.javier.bipo.home.models.GetBikesResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeReportFragment extends Fragment {

    private RecyclerView rvUserBikes;
    private SharedPreferences preferences;
    private android.content.res.Resources resources;
    private TextView tvBikeName, tvBikeFrame;
    private EditText etBikeDetails;
    private Spinner spReportType;

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
        etBikeDetails = (EditText)view.findViewById(R.id.EtBikeDetailsReport);
        spReportType = (Spinner)view.findViewById(R.id.SpReportType);
        preferences = getActivity().getSharedPreferences("UserInfo",0);
        resources = getContext().getResources();
        getAccountBikes();
        return view;
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
                        /*//String bikeName = bikesList.get(position).getBikeName();
                        String status = bikesList.get(position).getBikestate();
                        String brand = bikesList.get(position).getBrand();
                        String type = bikesList.get(position).getType();
                        String color = bikesList.get(position).getColor();
                        String idFrame = bikesList.get(position).getIdframe();
                        boolean defaultBike = bikesList.get(position).isDefaultbike();
                        int image = R.drawable.wheel;
                        //String image = reportList.get(position).getReportPhotos().get(0);

                        //Argumentos del Bundle
                        Bundle arguments = new Bundle();
                        //arguments.putString("activity", "button");
                        arguments.putInt("image", image);
                        arguments.putString("status", status);
                        arguments.putString("brand", brand);
                        arguments.putString("type", type);
                        arguments.putString("color", color);
                        arguments.putBoolean("default", defaultBike);
                        arguments.putString("colorArea", idFrame);
                        //goToItemBikeFragment(arguments);*/
                    }
                })
        );
    }

    public void showMessage(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
