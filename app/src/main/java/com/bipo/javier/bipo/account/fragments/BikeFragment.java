package com.bipo.javier.bipo.account.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.models.BikesResponse;
import com.bipo.javier.bipo.home.models.HomeRepository;
import com.bipo.javier.bipo.login.models.AccountRepository;
import com.bipo.javier.bipo.login.utilities.Teclado;
import com.bipo.javier.bipo.report.models.BikeBrand;
import com.bipo.javier.bipo.report.models.BikeBrandsResponse;
import com.bipo.javier.bipo.report.models.BikeColor;
import com.bipo.javier.bipo.report.models.BikeColorsResponse;
import com.bipo.javier.bipo.report.models.BikeType;
import com.bipo.javier.bipo.report.models.BikeTypesResponse;
import com.bipo.javier.bipo.report.models.ReportRepository;
import com.bipo.javier.bipo.utils.BikeBrandSpinner;
import com.bipo.javier.bipo.utils.BikeColorSpinner;
import com.bipo.javier.bipo.utils.BikeTypeSpinner;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import okhttp3.MediaType;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.Retrofit;




/**
 * A simple {@link Fragment} subclass.
 */
public class BikeFragment extends Fragment implements View.OnClickListener {

    private EditText idMarco, confirmacionId, etBikeFeatures, etBikeName;
    private CheckBox chbxFotos, chbxTerminos;
    private Spinner spBrand, spColor, spType;
    private ImageButton btnFoto, btnDelete, btnLeft, btnRight;
    private ImageView imgvDefault, imgvFoto1, imgvFoto2, imgvFoto3, imgvFoto4;
    private ViewFlipper viewFlipper;
    private static final int CAM_REQUEST = 1313;
    private static final int GALLERY_SELECT_IMAGE = 1020;
    private File directory;
    private int photo;
    private String file = "userBike", format = ".jpg";
    private Boolean activePhoto;
    private List<String> listBrands, listTypes;
    private ArrayList<BikeColor> listColors;
    private long idBrand, idColor, idType;
    private BikeTypeSpinner bikeTypeSpinner;
    private BikeBrandSpinner bikeBrandSpinner;
    private BikeColorSpinner bikeColorSpinner;
    private SharedPreferences preferences;
    private int uploadPhotos;
    private AlertDialog.Builder alert;

    public BikeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bike, container, false);
        photo = 0;
        activePhoto = true;
        listBrands = new ArrayList<>();
        listColors = new ArrayList<>();
        listTypes = new ArrayList<>();
        bikeBrandsList();
        bikeColorsList();
        bikeTypesList();
        preferences = getActivity().getSharedPreferences("UserInfo", 0);
        ContextWrapper cw = new ContextWrapper(getContext());
        directory = cw.getDir("Images", Context.MODE_APPEND);
        spBrand = (Spinner) view.findViewById(R.id.SpBrand);
        spColor = (Spinner) view.findViewById(R.id.SpColor);
        spType = (Spinner) view.findViewById(R.id.SpType);
        Button BtBack = (Button) view.findViewById(R.id.BtnPrevious);
        BtBack.setOnClickListener(this);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.VfpFlipper);
        Button BtRegister = (Button) view.findViewById(R.id.BtnUserRegister);
        BtRegister.setOnClickListener(this);
        btnFoto = (ImageButton) view.findViewById(R.id.ImgBtnFotoNueva);
        btnFoto.setOnClickListener(this);
        btnLeft = (ImageButton) view.findViewById(R.id.ImgBtnPrev);
        btnLeft.setOnClickListener(this);
        btnRight = (ImageButton) view.findViewById(R.id.ImgBtnNext);
        btnRight.setOnClickListener(this);
        btnDelete = (ImageButton) view.findViewById(R.id.ImgBtnDelete);
        btnDelete.setOnClickListener(this);
        etBikeName = (EditText) view.findViewById(R.id.EtBikeName);
        idMarco = (EditText) view.findViewById(R.id.EtIdMarco);
        confirmacionId = (EditText) view.findViewById(R.id.EtConfirmacionId);
        etBikeFeatures = (EditText) view.findViewById(R.id.EtCaracteristicas);
        chbxFotos = (CheckBox) view.findViewById(R.id.ChbxPoliticaFotos);
        chbxTerminos = (CheckBox) view.findViewById(R.id.ChbxPoliticaTerminos);
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
        alert = new AlertDialog.Builder(getContext());
        cleanStorage();
        validateButtons();
        return view;
    }

    private void cleanStorage() {

        for (int i = 1; i <= 4; i++) {
            File fileBike = new File(directory.getAbsolutePath(), file + i + format);
            if (fileBike.exists()) {
                fileBike.delete();
            }
        }
        listFields();
    }

    private void brandSpinner() {

        //Spinner de la marca de la bicicleta
        bikeBrandSpinner = new BikeBrandSpinner(getContext(), listBrands);
        spBrand.setOnItemSelectedListener(bikeBrandSpinner.getListener());
        spBrand.setAdapter(bikeBrandSpinner.getAdapter());
        //idBrand = bikeBrandSpinner.getIdBrand();
    }

    public void colorSpinner() {

        //Spinner del color de la bicicleta
        bikeColorSpinner = new BikeColorSpinner(getContext(), listColors);
        spColor.setOnItemSelectedListener(bikeColorSpinner.getListener());
        spColor.setAdapter(bikeColorSpinner.getAdapter());
        spColor.setDropDownWidth(550);
        //idColor = bikeColorSpinner.getIdColor();
    }

    public void typeSpinner() {

        //Spinner del tipo de la bicicleta
        bikeTypeSpinner = new BikeTypeSpinner(getContext(), listTypes);
        spType.setOnItemSelectedListener(bikeTypeSpinner.getListener());
        spType.setAdapter(bikeTypeSpinner.getAdapter());
        //idType = bikeTypeSpinner.getIdType();
    }

    //<editor-fold desc="Spinner's services">
    private void bikeBrandsList() {

        ReportRepository repo = new ReportRepository(getContext());
        final BikeBrandsResponse bikeBrandsResponse = new BikeBrandsResponse();
        Call<BikeBrandsResponse> call = repo.getBikeBrands();
        call.enqueue(new Callback<BikeBrandsResponse>() {
            @Override
            public void onResponse(retrofit.Response<BikeBrandsResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    System.out.println(response.isSuccess());
                    System.out.println(response.message());
                    System.out.println(response.code());
                    bikeBrandsResponse.setMessage(response.message());
                    showMessage("Ocurrió un error en la red.");
                    System.out.println(bikeBrandsResponse.getMessage());
                }
                if (response != null && response.isSuccess() && response.message() != null) {

                    List<BikeBrand> bikes = response.body().brands;
                    BikeBrandsResponse bikeResponse = new BikeBrandsResponse();
                    bikeResponse.setBrands(response.body().brands);

                    listBrands.add(0, "Escoge una marca.");
                    for (BikeBrand bike : bikes) {

                        listBrands.add(bike.getId(), bike.getBrand());
                        System.out.println("id: " + bike.getId() + "\nbrand: " + bike.getBrand());
                    }
                    brandSpinner();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                bikeBrandsResponse.setMessage(t.getMessage());
                showMessage("No se pudo establecer la conexión de la red. " +
                        "Verifica que tengas conexión a internet.");
            }
        });
    }

    private void bikeTypesList() {

        ReportRepository repo = new ReportRepository(getContext());
        final BikeTypesResponse bikeTypesResponse = new BikeTypesResponse();
        Call<BikeTypesResponse> call = repo.getBikeTypes();
        call.enqueue(new Callback<BikeTypesResponse>() {
            @Override
            public void onResponse(retrofit.Response<BikeTypesResponse> response, Retrofit retrofit) {

                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    System.out.println(response.isSuccess());
                    System.out.println(response.message());
                    System.out.println(response.code());
                    bikeTypesResponse.setMessage(response.message());
                    showMessage("Ocurrió un error en la red.");
                    System.out.println(bikeTypesResponse.getMessage());
                }
                if (response != null && response.isSuccess() && response.message() != null) {

                    List<BikeType> bikes = response.body().biketypes;
                    BikeTypesResponse bikeResponse = new BikeTypesResponse();
                    bikeResponse.setBiketypes(response.body().biketypes);
                    listTypes = new ArrayList<>();
                    listTypes.add(0, "Escoge un tipo.");
                    for (BikeType bike : bikes) {

                        listTypes.add(bike.getType());
                        System.out.println("id: " + bike.getId() + "\ntype: " + bike.getType());
                    }
                    typeSpinner();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                System.out.println("onFailure!: " + t);
                bikeTypesResponse.setMessage(t.getMessage());
                showMessage("No se pudo establecer la conexión de la red. " +
                        "Verifica que tengas conexión a internet.");
            }
        });
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

                    List<BikeColor> bikes = response.body().bikeColor;
                    BikeColor color = new BikeColor();
                    color.setId(0);
                    color.setColor("Escoge un color.");
                    listColors.add(color);
                    for (BikeColor bike : bikes) {

                        listColors.add(bike);
                    }
                    colorSpinner();
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
    //</editor-fold>

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.BtnPrevious: {
                this.getActivity().onBackPressed();
                break;
            }
            case R.id.BtnUserRegister: {

                validateFields(); //TODO: Habilitar
               /* String token = preferences.getString("token", "");
                File[] files = directory.listFiles();
                if (files.length != 0){
                    for (File item : files) {
                        uploadBikePhotos(item, "La panadera", token, files.length);

                    }
                }else{
                    showMessage("Reporte enviado exitosamente! \nGracias por tu cooperación!");
                }*/
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

    private void validateFields() {
        Teclado.ocultarTeclado(getActivity());
        String bikeName = etBikeName.getText().toString();
        String id = idMarco.getText().toString();
        String confId = confirmacionId.getText().toString();
        idBrand = bikeBrandSpinner.getIdBrand();
        idColor = bikeColorSpinner.getIdColor();
        idType = bikeTypeSpinner.getIdType();
        int idBikeState = 3;
        String bikeFeatures = etBikeFeatures.getText().toString();
        String token = preferences.getString("token", "");

        if (TextUtils.isEmpty(bikeName)) {

            etBikeName.setError("Escribe un nombre para esta bicicleta.");
            return;
        }
        if (idBrand ==0){
            showMessage("Escoge una marca por favor.");
            return;
        }
        if (idColor ==0){
            showMessage("Escoge un color por favor.");
            return;
        }
        if (TextUtils.isEmpty(id)) {
            idMarco.setError("Escribe el id del marco de tu bicicleta.");
            return;
        }
        if (TextUtils.isEmpty(confId)) {
            confirmacionId.setError("Confirma el id del marco de tu bicicleta.");
            return;

        }
        if (!id.equals(confId)) {
            confirmacionId.setError("La confirmación no corresponde con el id.");
            return;
        }
        if (idType ==0){
            showMessage("Escoge un tipo de bicicleta por favor.");
            return;
        }
        if (!chbxTerminos.isChecked()) {
            showMessage("Debes aceptar los terminos y condiciones");
            return;
        }
        //rLytCharge.setVisibility(View.VISIBLE);
        //imgvCharge.startAnimation(anim);
        //imgvCharge.getAnimation().setDuration(2000);
        registerBike(bikeName, idBrand, idColor, id, idType, bikeFeatures, idBikeState, token);
    }

    private void registerBike(final String bikeName, long idBrand, long idColor, String idFrame,
                              long idType, String bikeFeatures, int idBikeState, final String token) {


        HomeRepository repo = new HomeRepository(getContext());
        Call<BikesResponse> call = repo.bikeRegister(bikeName, (int) idBrand, (int) idColor, idFrame,
                (int) idType, bikeFeatures, idBikeState, token);
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

                    System.out.println("Mensaje: " + response.message());
                    File[] files = directory.listFiles();
                    if (files.length != 0){
                        for (int i = 0; i < files.length; i++) {
                            File item = files[i];
                            uploadBikePhotos(item, bikeName, token, files.length);
                        }
                    }else{
                        successFull(bikeName);
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

    private void uploadBikePhotos(final File photo, final String bikeName, String token, final int totalPhotos) {

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

        System.out.println("Response!!!");
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
                        Teclado.ocultarTeclado(getActivity());
                        successFull(bikeName);


                    }
                }else{
                    System.out.println("Error: " + response.message());
                    System.out.println("codigo: " + response.code());
                }

            }
        });
    }

    private void successFull(String bikeName) {

        Teclado.ocultarTeclado(getActivity());
        alert.setTitle(bikeName + " se ha registrado exitosamente");
        alert.setMessage("Gracias por registrar a " + bikeName + ".")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cleanStorage();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
        alert.show();

    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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

            imgvDefault.setImageResource(R.mipmap.ic_gallery);
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
