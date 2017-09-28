package com.bipo.javier.bipo.account.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.models.BikesResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.login.utilities.Teclado;
import com.bipo.javier.bipo.report.models.BikeColor;
import com.bipo.javier.bipo.report.models.BikeColorsResponse;
import com.bipo.javier.bipo.report.models.ReportRepository;
import com.bipo.javier.bipo.utils.BikeColorSpinner;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditBikeFragment extends Fragment implements View.OnClickListener {

    private ImageButton btnFoto, btnDelete, btnLeft, btnRight;
    private ImageView imgvDefault, imgvFoto1, imgvFoto2, imgvFoto3, imgvFoto4, imgvDefBike, imgVCharge;
    private ViewFlipper viewFlipper;
    private static final int CAM_REQUEST = 1313;
    private static final int GALLERY_SELECT_IMAGE = 1020;
    private File directory;
    private int photo, uploadPhotos;
    private String file = "bike", format = ".jpg";
    private Boolean activePhoto;
    private ArrayList<BikeColor> listColors;
    private long idColor;
    private BikeColorSpinner bikeColorSpinner;
    private Spinner spColor;
    private SharedPreferences preferences;
    private Menu menu;
    private TextView tvBikeName, tvBikeBrand, tvBikeType, tvBikeColor, tvBikeIdFrame;
    private EditText etBikeFeatures;
    private android.content.res.Resources resources;
    private Animation anim;
    AlertDialog.Builder alert;
    private static final String BIPO_URL = "http://www.bipoapp.com/";
    private String token;
    private int bikeId;
    private RelativeLayout rlytCharge;

    public EditBikeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_bike, container, false);
        photo = 0;
        activePhoto = true;
        listColors = new ArrayList<>();
        bikeColorsList();
        preferences = getActivity().getSharedPreferences("UserInfo", 0);
        ContextWrapper cw = new ContextWrapper(getContext());
        directory = cw.getDir("Images", Context.MODE_APPEND);
        rlytCharge = (RelativeLayout)view.findViewById(R.id.RlytChargeEditB);
        imgVCharge = (ImageView)view.findViewById(R.id.ImgVChargeEditB);
        spColor = (Spinner) view.findViewById(R.id.SpBikeColor);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.VfpFlipper);
        btnFoto = (ImageButton) view.findViewById(R.id.ImgBtnFotoNueva);
        btnFoto.setOnClickListener(this);
        btnLeft = (ImageButton) view.findViewById(R.id.ImgBtnPrev);
        btnLeft.setOnClickListener(this);
        btnRight = (ImageButton) view.findViewById(R.id.ImgBtnNext);
        btnRight.setOnClickListener(this);
        btnDelete = (ImageButton) view.findViewById(R.id.ImgBtnDelete);
        btnDelete.setOnClickListener(this);
        imgvDefault = new ImageView(getContext());
        imgvDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoGallery();
            }
        });
        imgvFoto1 = new ImageView(getContext());
        imgvFoto2 = new ImageView(getContext());
        imgvFoto3 = new ImageView(getContext());
        imgvFoto4 = new ImageView(getContext());
        resources = getContext().getResources();
        imgvDefBike = (ImageView)view.findViewById(R.id.ImgVwDefBike);
        tvBikeName = (TextView) view.findViewById(R.id.TvBikeName);
        tvBikeBrand = (TextView) view.findViewById(R.id.TvBikeBrand);
        tvBikeType = (TextView) view.findViewById(R.id.TvBikeType);
        tvBikeColor = (TextView) view.findViewById(R.id.TvBikeColor);
        tvBikeIdFrame = (TextView) view.findViewById(R.id.TvBikeIdFrame);
        etBikeFeatures = (EditText) view.findViewById(R.id.EtBikeFeatures);
        etBikeFeatures.setEnabled(false);
        etBikeFeatures.setMovementMethod(new ScrollingMovementMethod());
        alert = new AlertDialog.Builder(getContext());
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_charge_rotation);
        anim.setDuration(2000);
        token = preferences.getString("token", "");
        bikeId = getArguments().getInt("id");
        cleanStorage();
        validateButtons();
        galeryActive(false);
        setHasOptionsMenu(true);
        bikeInfo();
        return view;
    }

    private void bikeInfo() {

        if (getArguments().getString("imageUrl").equals("")){
            imgvFoto1.setImageResource(R.mipmap.ic_no_image);
            viewFlipper.addView(imgvFoto1);
        }else{
            getBikePhotos();
        }

        if (getArguments().getInt("default") == 1){
            imgvDefBike.setVisibility(View.VISIBLE);
        }
        tvBikeName.setText(getArguments().getString("bikeName"));
        String bikeBrand = String.format(resources.getString(R.string.sr_itmtxt_bike_brand),
                getArguments().getString("brand"));
        tvBikeBrand.setText(bikeBrand);
        String bikeType = String.format(resources.getString(R.string.sr_itmtxt_bike_type),
                getArguments().getString("type"));
        tvBikeType.setText(bikeType);
        String bikeColor = String.format(resources.getString(R.string.sr_itmtxt_bike_color),
                getArguments().getString("color"));
        tvBikeColor.setText(bikeColor);
        String idFrame = String.format(resources.getString(R.string.sr_itmtxt_bike_idframe),
                getArguments().getString("idFrame"));
        tvBikeIdFrame.setText(idFrame);
        etBikeFeatures.setText(getArguments().getString("features"));
    }

    private void getBikePhotos(){

        anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_charge_rotation);
        anim.setDuration(2000);
        imgvFoto1.setAnimation(anim);

        String imageUrl = getArguments().getString("imageUrl");
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imgvFoto1.setImageBitmap(bitmap);
                imgvFoto1.getAnimation().cancel();
                //viewFlipper.addView(imgvFoto1);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                imgvFoto1.setImageResource(R.mipmap.ic_no_image);
                imgvFoto1.getAnimation().cancel();
                //viewFlipper.addView(imgvFoto1);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                imgvFoto1.setImageResource(R.mipmap.ic_charge);
                imgvFoto1.getAnimation().start();
                //viewFlipper.addView(imgvFoto1);
            }
        };
        viewFlipper.addView(imgvFoto1);
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(imgvFoto1));
        Picasso.with(getContext()).load(imageUrl).into(target);
    }

    private void bikeColorsList() {

        ReportRepository repo = new ReportRepository(getContext());
        final BikeColorsResponse bikeColorsResponse = new BikeColorsResponse();
        Call<BikeColorsResponse> call = repo.getBikeColors();
        call.enqueue(new Callback<BikeColorsResponse>() {
            @Override
            public void onResponse(retrofit.Response<BikeColorsResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {

                    bikeColorsResponse.setMessage(response.message());
                    showMessage("Ocurrió un error en la red.");
                    System.out.println(bikeColorsResponse.getMessage());
                }
                if (response != null && response.isSuccess() && response.message() != null) {

                    bikeColorsResponse.setBikeColor(response.body().bikeColor);
                    List<BikeColor> bikes = bikeColorsResponse.getBikeColor();
                    if (bikes != null) {
                        BikeColor bikeColor = new BikeColor();
                        bikeColor.setId(0);
                        bikeColor.setColor("Escoge un color.");
                        listColors.add(bikeColor);
                        for (BikeColor bike : bikes) {

                            listColors.add(bike);
                        }
                        colorSpinner();
                    }else{
                        Log.e("EditBike/BikeColorList","ColorList empty");
                        showMessage("Error al cargar la lista de colores."+
                                "\nContácte al administrador.");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                bikeColorsResponse.setMessage(t.getMessage());
                showMessage("No se pudo establecer la conexión de la red. " +
                        "Verifica que tengas conexión a internet.");
            }
        });

    }

    public void colorSpinner() {

        //Spinner del color de la bicicleta
        bikeColorSpinner = new BikeColorSpinner(getContext(), listColors);
        spColor.setOnItemSelectedListener(bikeColorSpinner.getListener());
        spColor.setAdapter(bikeColorSpinner.getAdapter());
        spColor.setDropDownWidth(550);
    }

    private void cleanStorage() {

        for (int i = 1; i <= 4; i++) {
            File fileBike = new File(directory.getAbsolutePath(), file + i + format);
            if (fileBike.exists()) {
                fileBike.delete();
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        this.menu = menu;
        inflater.inflate(R.menu.settings_bike_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(3).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.edit_bike) {

            editBike();
        } else if (item.getItemId() == R.id.default_bike){

            if (getArguments().getInt("default") != 1){
                setBikeDefault();
            }else{
                showMessage("Ya es tu bicicleta principal.");
            }
        }else if (item.getItemId() == R.id.delete_bike){

            alert.setTitle("Eliminar bicicleta.");
            alert.setMessage("¿Seguro que quieres eliminar a " + tvBikeName.getText() + "?" )
                    .setPositiveButton("Eliminar bicicleta", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            deleteBike();
                        }
                    })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();


        }else if (item.getItemId() == R.id.save_bike){

            saveBike();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setBikeDefault() {

        HomeRepository repo = new HomeRepository(getContext());
        Call<BikesResponse> call = repo.defaultBike(bikeId,token);
        final BikesResponse bikesResponse = new BikesResponse();
        call.enqueue(new Callback<BikesResponse>() {
            @Override
            public void onResponse(retrofit.Response<BikesResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    if (response.code() == 400) {
                        showMessage("hubo un error al enviar los datos");
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

                    bikesResponse.setError(response.body().getError());
                    if (bikesResponse.getError().equals("false")) {

                        imgvDefBike.setVisibility(View.VISIBLE);
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Cambiaste tu bicicleta principal.");
                        alert.setMessage(getArguments().getString("bikeName")
                                +" es ahora tu bicicleta principal.");
                        alert.show();
                    } else {
                        bikesResponse.setMessage(response.body().getMessage());
                        System.out.println("Error: " + bikesResponse.getMessage());
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


    private void editBike() {

        viewFlipper.removeView(imgvFoto1);
        galeryActive(true);
        tvBikeBrand.setTextColor(Color.GRAY);
        tvBikeType.setTextColor(Color.GRAY);
        tvBikeIdFrame.setTextColor(Color.GRAY);
        tvBikeColor.setVisibility(View.INVISIBLE);
        if (listColors != null){
            for (int i = 0; i < listColors.size() - 1;i++) {
                if (listColors.get(i).getColor().equals(getArguments().getString("color"))) {
                    spColor.setSelection(i);
                }
            }
        }else{showMessage("No hay colores");}
        spColor.setVisibility(View.VISIBLE);
        btnFoto.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
        etBikeFeatures.setEnabled(true);
        menu.getItem(3).setVisible(true);
        menu.close();
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
    }

    private void deleteBike() {

        HomeRepository repo = new HomeRepository(getContext());
        Call<BikesResponse> call = repo.deleteBike(bikeId, token);
        final BikesResponse bikesResponse = new BikesResponse();
        call.enqueue(new Callback<BikesResponse>() {
            @Override
            public void onResponse(retrofit.Response<BikesResponse> response, Retrofit retrofit) {

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

                    bikesResponse.setError(response.body().getError());
                    if (bikesResponse.getError().equals("false")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Has borrado una bicicleta.");
                        alert.setMessage(tvBikeName.getText()+ " se ha borrado exitosamente. ")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        getActivity().onBackPressed();
                                    }
                                });
                        alert.show();
                    } else {
                        bikesResponse.setMessage(response.body().getMessage());
                        System.out.println(bikesResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                bikesResponse.setMessage(t.getMessage());
            }
        });
    }

    private void saveBike() {

        galeryActive(false);
        tvBikeBrand.setTextColor(Color.BLACK);
        tvBikeType.setTextColor(Color.BLACK);
        tvBikeIdFrame.setTextColor(Color.BLACK);
        tvBikeColor.setVisibility(View.VISIBLE);
        idColor = bikeColorSpinner.getIdColor();
        String bikeColor = String.format(resources.getString(R.string.sr_itmtxt_bike_color),
                listColors.get((int)idColor).getColor());
        tvBikeColor.setText(bikeColor);
        if ((!listColors.get((int)idColor).getColor().equals(getArguments().getString("color")))
                || (!etBikeFeatures.getText().toString().equals(getArguments().getString("features")))){
            rlytCharge.setVisibility(View.VISIBLE);
            imgVCharge.startAnimation(anim);
            updateBike(bikeId,token, (int)idColor,etBikeFeatures.getText().toString());
        }else{
            File[] files = directory.listFiles();
            if (files.length != 0) {
                rlytCharge.setVisibility(View.VISIBLE);
                imgVCharge.startAnimation(anim);
                for (File item : files) {
                    uploadBikePhotos(item, tvBikeName.getText().toString(), token,
                            files.length);
                }
            }
        }
        spColor.setVisibility(View.INVISIBLE);
        btnFoto.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);
        etBikeFeatures.setEnabled(false);
        //viewFlipper.addView(imgvFoto1);
        menu.getItem(3).setVisible(false);
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(true);
        menu.getItem(2).setVisible(true);
    }

    private void updateBike(int bikeId, final String token, int idColor, String bikeFeatures) {

        HomeRepository repo = new HomeRepository(getContext());
        Call<BikesResponse> call = repo.updateBike(bikeId, token, idColor, bikeFeatures);
        final BikesResponse bikesResponse = new BikesResponse();
        call.enqueue(new Callback<BikesResponse>() {
            @Override
            public void onResponse(retrofit.Response<BikesResponse> response, Retrofit retrofit) {

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

                    bikesResponse.setError(response.body().getError());
                    if (bikesResponse.getError().equals("false")) {
                        imgVCharge.getAnimation().cancel();
                        rlytCharge.setVisibility(View.INVISIBLE);
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Se ha actualizado una bicicleta.");
                        alert.setMessage(tvBikeName.getText() + " se ha actualizado exitosamente. ");
                        alert.show();
                        /*File[] files = directory.listFiles();
                        if (files.length != 0) {
                            for (File item : files) {
                                uploadBikePhotos(item, tvBikeName.getText().toString(), token,
                                        files.length);
                            }
                        }else {
                            imgVCharge.getAnimation().cancel();
                            rlytCharge.setVisibility(View.INVISIBLE);
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("Se ha actualizado una bicicleta.");
                            alert.setMessage(tvBikeName.getText() + " se ha actualizado exitosamente. ");
                            alert.show();
                        }*/
                    } else {
                        showMessage("mal");
                        bikesResponse.setMessage(response.body().getMessage());
                        System.out.println(bikesResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                bikesResponse.setMessage(t.getMessage());
            }
        });
    }

    private void uploadBikePhotos(final File photo, final String bikeName, final String token, final int totalPhotos) {

        final okhttp3.MediaType MEDIA_TYPE = okhttp3.MediaType.parse("image/jpg");
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

        MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("bikeName",bikeName)
                .addFormDataPart("file",photo.getName(), okhttp3.RequestBody.create(MEDIA_TYPE,
                        new File(photo.getAbsolutePath())))
                .addFormDataPart("token", token)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://bipoapp.com/services/v1/bikePhoto")
                .post(body)
                .addHeader("authorization", "650E01A1B8F9A4DA4A2040FF86E699B7")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                System.out.println(call.request().body());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {


                System.out.println("Successfull?: " + response.isSuccessful());
                if (response.code() == 400) {
                    showMessage("No hay datos.");


                } else if (response.code() == 500) {
                    showMessage("Ocurrió un error en la red.");
                    System.out.println(response.message());
                } else if (response.code() == 200) {
                    uploadPhotos ++;
                    System.out.println("Se subio la foto: " + photo.getName());
                    if (uploadPhotos == totalPhotos){
                        getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Teclado.ocultarTeclado(getActivity());
                                    imgVCharge.getAnimation().cancel();
                                    rlytCharge.setVisibility(View.INVISIBLE);
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                    alert.setTitle("Se ha actualizado una bicicleta.");
                                    alert.setMessage(tvBikeName.getText() + " se ha actualizado exitosamente. ");
                                    alert.show();
                                }
                            });
                    }
                }else{
                    System.out.println("Error: " + response.message());
                    System.out.println("codigo: " + response.code());
                }

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.BtnPrevious: {
                this.getActivity().onBackPressed();
                break;
            }
            case R.id.ImgBtnFotoNueva: {
                newPhoto(activePhoto);
                break;
            }
            case R.id.ImgBtnPrev: {
                previousPhoto();
                break;
            }
            case R.id.ImgBtnNext: {
                nextPhoto();
                break;
            }
            case R.id.ImgBtnDelete: {
                deletePhoto();
            }
        }
    }

    private void photoGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_SELECT_IMAGE);
    }

    private void nextPhoto() {
        final Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setInAnimation(in);
        viewFlipper.showNext();
        deleteActive(viewFlipper.getDisplayedChild() != viewFlipper.indexOfChild(imgvDefault));
    }

    private void previousPhoto() {
        final Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setInAnimation(out);
        viewFlipper.showPrevious();
        deleteActive(viewFlipper.getDisplayedChild() != viewFlipper.indexOfChild(imgvDefault));
    }

    private void newPhoto(Boolean active) {
        if (active) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(cameraIntent, CAM_REQUEST);
            }
        } else {
            showMessage("Alcansáste el limite de fotos, borra una para tomar una foto nueva.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //this.data = data;
        Bitmap bMap;
        if (requestCode == CAM_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            bMap = (Bitmap) extras.get("data");
            saveToInternalStorage(bMap);
        } else if (requestCode == GALLERY_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {

            Bitmap result = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream is;
            try {
                is = getContext().getContentResolver().openInputStream(data.getData());
                result = BitmapFactory.decodeStream(is, null, options);
                assert is != null;
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            saveToInternalStorage(result);
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {

        photo++;
        FileOutputStream fos = null;
        for (int i = 1; i <= 4; i++) {
            File mypath = new File(directory, file + i + format);
            if (!mypath.exists()) {
                try {
                    fos = new FileOutputStream(mypath);
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    System.out.println("La imagen se guardó en: " + directory.getAbsolutePath() +
                            "/" + file + photo + format);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("saveToInternalStorage","Save image error.");
                } finally {
                    try {
                        assert fos != null;
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getPhotoBikes(i);
                listFields();
                break;
            }
        }
    }

    private void deletePhoto() {

        if (viewFlipper.getDisplayedChild() != viewFlipper.indexOfChild(imgvDefault)) {
            int numBike = 0;
            View bike = viewFlipper.getChildAt(viewFlipper.getDisplayedChild());
            if (bike == imgvFoto1) {
                numBike = 1;
            } else if (bike == imgvFoto2) {
                numBike = 2;
            } else if (bike == imgvFoto3) {
                numBike = 3;
            } else if (bike == imgvFoto4) {
                numBike = 4;
            }

            File fileBike = new File(directory.getAbsolutePath(), file + numBike + format);
            fileBike.delete();

            if (!fileBike.exists()) {
                System.out.println("Se ha borrado la foto: " + file + numBike + format);
                showMessage("La foto actual se ha borrado.");
            }

            viewFlipper.removeView(viewFlipper.getChildAt(viewFlipper.getDisplayedChild()));
            viewFlipper.setDisplayedChild(numBike);
            photo--;
            validateButtons();
            listFields();
        } else {
            showMessage("No se puede borrar");
        }

    }

    private void getPhotoBikes(int photoNumber) {

        try {
            File fileBike = new File(directory.getAbsolutePath(), file + photoNumber + format);
            if (fileBike.exists()) {
                Bitmap image = BitmapFactory.decodeStream(new FileInputStream(fileBike));
                getImageBitmap(image, photoNumber);
            }

        } catch (Exception e) {
            System.out.println("No existe el archivo: " + file + photo + format + "\n" + e);
        }
        deleteActive((viewFlipper.getDisplayedChild() != viewFlipper.indexOfChild(imgvDefault)) &&
                (viewFlipper.indexOfChild(imgvDefault) != -1));
        validateButtons();
    }

    public void getImageBitmap(Bitmap bikePhoto, int numPhoto) {

        switch (numPhoto) {
            case 1: {
                imgvFoto1.setImageBitmap(bikePhoto);
                viewFlipper.addView(imgvFoto1);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(imgvFoto1));
                break;
            }
            case 2: {
                imgvFoto2.setImageBitmap(bikePhoto);
                viewFlipper.addView(imgvFoto2);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(imgvFoto2));
                break;
            }
            case 3: {
                imgvFoto3.setImageBitmap(bikePhoto);
                viewFlipper.addView(imgvFoto3);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(imgvFoto3));
                break;
            }
            case 4: {
                imgvFoto4.setImageBitmap(bikePhoto);
                viewFlipper.addView(imgvFoto4);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(imgvFoto4));
                break;
            }
        }

    }

    private void validateButtons() {
        cameraActive();
        leftArrowActive();
        rightArrowActive();

    }

    private void cameraActive() {
        if (photo >= 4) {
            activePhoto = false;
            btnFoto.setImageResource(R.mipmap.ic_camera_des);
            galeryActive(false);
        } else {
            activePhoto = true;
            btnFoto.setImageResource(R.mipmap.ic_camera);
            galeryActive(true);
        }
    }

    private void leftArrowActive() {
        if (photo >= 1) {

            btnLeft.setEnabled(true);
            btnLeft.setImageResource(R.mipmap.ic_left_arrow);
            deleteActive(true);
        } else {
            btnLeft.setEnabled(false);
            btnLeft.setImageResource(R.mipmap.ic_left_arrow_des);
            deleteActive(false);
        }
    }

    private void rightArrowActive() {
        if (photo >= 1) {
            btnRight.setEnabled(true);
            btnRight.setImageResource(R.mipmap.ic_right_arrow);
        } else {
            btnRight.setEnabled(false);
            btnRight.setImageResource(R.mipmap.ic_right_arrow_des);
        }
    }

    private void deleteActive(boolean activate) {
        if (activate) {
            btnDelete.setEnabled(true);
            btnDelete.setImageResource(R.mipmap.ic_delete);
        } else {
            btnDelete.setEnabled(false);
            btnDelete.setImageResource(R.mipmap.ic_delete_des);
        }
    }

    private void galeryActive(boolean activate) {

        if ((activate) && viewFlipper.indexOfChild(imgvDefault) == -1) {

            imgvDefault.setImageResource(R.drawable.galery);
            viewFlipper.addView(imgvDefault);
        } else if (!activate) {
            viewFlipper.removeView(imgvDefault);
        }
    }

    private void listFields() {
        File[] files = directory.listFiles();
        for (File item : files) {
            System.out.println("\n" + item.getName());
        }
    }
}
