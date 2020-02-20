package com.example.mapas.mislugares;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Pantalla_Mapa extends FragmentActivity implements OnMapReadyCallback {
    private final int UBICACION = 1;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diseno_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.boton_guiar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle datos = getIntent().getExtras();
                String nombre = datos.getString("nombre");
                double latitud = datos.getDouble("latitud");
                double longitud = datos.getDouble("longitud");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+latitud+","+longitud+"?q="+latitud+","+longitud));
                startActivity(intent);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Recogemos los datos que nos han pasado desde la pantalla anterior:
        Bundle datos = getIntent().getExtras();
        String nombre = datos.getString("nombre");
        double latitud = datos.getDouble("latitud");
        double longitud = datos.getDouble("longitud");

        //Añadir un marcador en la posicion que nos han pasado:
        LatLng posicion = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(posicion).title(nombre));
        //Efecto de movimiento a la posición, y pone un zoom alto:
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(latitud,longitud));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        //Mostrar los botones de zoom + y -
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Para mostrar la ubicación actual del usuario, dependiendo de la versión de Android,
        // necesitaremos pedirle permiso en este momento:
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        UBICACION);
            }
        } else {
            //En las versiones anteriores de Android no se necesita pedir permisos:
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case UBICACION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }
}
