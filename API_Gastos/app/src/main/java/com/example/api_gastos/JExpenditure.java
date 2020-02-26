package com.example.api_gastos;

import androidx.annotation.NonNull;

public class JExpenditure {
    private String mDescripcion;
    private String mFecha;
    private  float mCantidad;
    private String mTipoGasto;
    private String mCategoria;

    public JExpenditure ( String descripcion, String fecha, float cantidad, String tipoGasto, String categoria) {
        mDescripcion = descripcion;
        mFecha = fecha;
        mCantidad = cantidad;
        mTipoGasto = tipoGasto;
        mCategoria = categoria;
    }

    public String getmDescripcion() {
        return mDescripcion;
    }
    public String getmFecha() {
        return mFecha;
    }
    public float getmCantidad() {
        return mCantidad;
    }

    public String getmTipoGasto() {
        return mTipoGasto;
    }

    public String getmCategoria() {
        return mCategoria;
    }
    @NonNull
    @Override
    public String toString() {
        return mDescripcion+" - "+mCantidad;
    }

}
