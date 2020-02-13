package com.example.adivinanumero;

import android.widget.ImageView;
import android.widget.Toast;

public class JTermino {
    //Atributos
    private int mID;
    private String mPalabra;
    private String mDescripcion;

    //Propiedades:
    public int getID() {
        return mID;
    }

    public String getPalabra() {
        return mPalabra;
    }

    public String getDesc() {
        return mDescripcion;
    }

    //Constructor:
    public JTermino(int id, String palabra, String descripcion) {
        mID = id;
        mPalabra = palabra;
        mDescripcion = descripcion;
    }

}
