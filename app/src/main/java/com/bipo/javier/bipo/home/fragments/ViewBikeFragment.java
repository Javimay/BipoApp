package com.bipo.javier.bipo.home.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.health.PackageHealthStats;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.test.mock.MockPackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bipo.javier.bipo.R;
import com.bipo.javier.bipo.account.fragments.BikeFragment;
import com.google.android.gms.maps.LocationSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;


public class ViewBikeFragment extends Fragment implements View.OnClickListener, LocationListener {

    private static final int REQUEST_LOCATION = 2;
    private ImageButton btnFoto, btnDelete, btnLeft, btnRight, btnGetAddress;
    private ImageView imgvDefault, imgvFoto1, imgvFoto2, imgvFoto3, imgvFoto4;
    private EditText etAddress;
    private ViewFlipper viewFlipper;
    private static final int CAM_REQUEST = 1313;
    private static final int GALLERY_SELECT_IMAGE = 1020;
    private File directory;
    private int photo;
    private String file = "bikeView", format = ".jpg";
    private Boolean activePhoto, isGpsEnabled;
    private LocationManager locationManager;
    private double latitude, longitude;
    private Geocoder geocoder;

    public ViewBikeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_bike, container, false);
        photo = 0;
        activePhoto = true;
        etAddress = (EditText) view.findViewById(R.id.EtViewAddress);
        ContextWrapper cw = new ContextWrapper(getContext());
        btnGetAddress = (ImageButton) view.findViewById(R.id.ImgBtnGetAdress);
        btnGetAddress.setOnClickListener(this);
        directory = cw.getDir("Images", Context.MODE_PRIVATE);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.VfpFlipperView);
        Button BtRegister = (Button) view.findViewById(R.id.BtnViewBikeSend);
        BtRegister.setOnClickListener(this);
        btnFoto = (ImageButton) view.findViewById(R.id.ImgBtnFotoNuevaView);
        btnFoto.setOnClickListener(this);
        btnLeft = (ImageButton) view.findViewById(R.id.ImgBtnPrevView);
        btnLeft.setOnClickListener(this);
        btnRight = (ImageButton) view.findViewById(R.id.ImgBtnNextView);
        btnRight.setOnClickListener(this);
        btnDelete = (ImageButton) view.findViewById(R.id.ImgBtnDeleteView);
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
        cleanStorage();
        validateButtons();
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.BtnPrevious:
                this.getActivity().onBackPressed();
                break;

            case R.id.BtnViewBikeSend:
                validateFields();
                //Todo: Borrar las fotos despues de enviar el reporte.
                //Toast.makeText(getContext(), "Registrando...", Toast.LENGTH_LONG).show();
                break;

            case R.id.ImgBtnGetAdress:
                getAddress();
                break;

            case R.id.ImgBtnFotoNuevaView:
                newPhoto(activePhoto);
                break;

            case R.id.ImgBtnPrevView:
                previousPhoto();
                break;

            case R.id.ImgBtnNextView:
                nextPhoto();
                break;

            case R.id.ImgBtnDeleteView:
                deletePhoto();

        }
    }

    private void getAddress() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            turnOnGps();
        }
    }

    private void turnOnGps() {

        int off = 0;
        try {
            off = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            System.out.println("SettingNotFoundException: " + e.getMessage());
        }
        if(off == 0 || off == 2){
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("No hay acceso al  GPS.");
            alert.setMessage("bipo no tiene acceso al GPS. " +
                    "\nActiva el GPS para que bipo pueda acceder a tu ubicación o ingresa una direccion manualmente")
                    .setPositiveButton("Activar el acceso a la ubicación", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(onGPS);
                        }
                    })
                    .setNegativeButton("Manualmente", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            etAddress.requestFocus();
                        }
                    });
            alert.show();
        }else{


            System.out.println("Geolocalizacion: \tLatitud:" +latitude + "\tLongitud: " + longitude);
            showMessage("Obteniendo coordenadas...");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PERMISSION_GRANTED) {

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("No hay acceso al  GPS.");
                    alert.setMessage("bipo no tiene acceso al GPS. " +
                            "\nDebes agregar la dirección manualmente para continuar.")
                            .setPositiveButton("Agregar manualmente", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    etAddress.requestFocus();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //TODO: Volver al fragmento anterior.
                                    //getActivity().onBackPressed();
                                }
                            });
                    alert.show();
                }

            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        return (res == PERMISSION_GRANTED);
    }

    private void cleanStorage() {

        for (int i = 1; i <= 4; i++) {
            File fileBike = new File(directory.getAbsolutePath(), file + i + format);
            if (fileBike.exists()) {
                fileBike.delete();
            }

        }
    }

    private void validateFields() {

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
                    System.out.println("La imagen se guardo en: " + directory.getAbsolutePath() +
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

            imgvDefault.setImageResource(R.drawable.galery);
            viewFlipper.addView(imgvDefault);
        } else if (!activate) {
            viewFlipper.removeView(imgvDefault);
        }
    }

    private void listFields() {
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File item = files[i];
            System.out.println("\n" + item.getName());
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGpsEnabled) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            System.out.println("Geolocalizacion: \tLatitud:" +latitude + "\tLongitud: " + longitude);
            try {
                getAdress(latitude, longitude);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error tomando las coordenadas: " + e.getMessage());
            }
        }

        System.out.println("Geolocalizacion: \tLatitud:" +latitude + "\tLongitud: " + longitude);
    }

    private void getAdress(double latitude, double longitude) throws IOException {

        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        System.out.println("Direccion: " + address + "," + city + "," + country);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
