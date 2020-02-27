package com.example.api_gastos;

import androidx.annotation.NonNull;

public class JExpenditure {
    private String mDescripcion;
    private String mFecha;
    private  float mCantidad;
    private String mTipoGasto;
    private String mCategoria;
    private  int m_id;

    public JExpenditure ( String descripcion, String fecha, float cantidad, String tipoGasto, String categoria, int id) {
        mDescripcion = descripcion;
        mFecha = fecha;
        mCantidad = cantidad;
        mTipoGasto = tipoGasto;
        mCategoria = categoria;
        m_id = id;
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

    public int getid() {
        return m_id;
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
