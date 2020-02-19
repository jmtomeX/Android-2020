package com.example.cameraplay;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    //private final LatLng Cebanc = new LatLng(43.304953, -2.016847);
    private final LatLng Cebanc = new LatLng(4, 15);
    private TextView mTxtLongitud;
    private TextView mTxtLatitud;
    private Button mbtnMyPosition;
    //Para conseguir la ubicación actualizada:
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_layout);
        mbtnMyPosition = findViewById(R.id.btnMyPosition);
        mTxtLongitud = findViewById(R.id.textLongitud);
        mTxtLatitud = findViewById(R.id.textLatitud);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        RadioGroup rgViews = findViewById(R.id.rg_views);


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Cebanc, 15));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Cebanc));

        mMap.addMarker(new MarkerOptions()
                .position(Cebanc)
                .title("Cebanc")
                .snippet("San Sebastián")
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_compass))
                .anchor(0.5f, 0.5f));


        rgViews.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_normal){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }else if(checkedId == R.id.rb_satellite){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }else if(checkedId == R.id.rb_terrain){
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
            }

        });

        mbtnMyPosition.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      //Tenemos que avisar al usuario y que nos de permiso para ubicarle
                      checkLocationPermission();
                  }
              });
    }

    public boolean checkLocationPermission() {
        //La primera vez, entrara por los dos ifs:
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permiso de ubicación")
                        .setMessage("Das permiso para localizar tu posición")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            //Ya nos habia concedido el permiso
            getLocalizacion();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        //locationManager.requestLocationUpdates(provider, 400, 1, this);
                        getLocalizacion();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.e("MapsActivity","getCurrLoc: Lat:"+mLastLocation.getLatitude()+ " Long: "+mLastLocation.getLongitude());
            // Add a marker in myLocation and move the camera
            marcarUbicacion(new LatLng(mLastLocation.getLatitude()  , mLastLocation.getLongitude() ));
        }
    };


    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }


    private void getLocalizacion() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Log.e("MapsActivity","getCurrLoc: Lat:"+location.getLatitude()+ " Long: " + location.getLongitude());
                            mTxtLongitud.setText("" + location.getLongitude());
                            mTxtLatitud.setText("" + location.getLatitude());
                            // Add a marker in myLocation and move the camera
                            marcarUbicacion(new LatLng(location.getLatitude() , location.getLongitude() ));

                        }
                    }
                }
        );
    }

    private void marcarUbicacion(LatLng myLocation) {
        mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("Mi posición")
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_compass))
                .anchor(0.5f, 0.5f));


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation ));

        //zoom to position with level 15
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation , 15);
        mMap.animateCamera(cameraUpdate);
    }

}
//http://wptrafficanalyzer.in/blog/google-map-android-api-v2-switching-between-normal-view-satellite-view-and-terrain-view/