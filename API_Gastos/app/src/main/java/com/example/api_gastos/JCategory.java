package com.example.api_gastos;

import androidx.annotation.NonNull;

public class JCategory {
    private int mId;
    private String mCategoria;

    public  JCategory (int id, String categoria) {
        mId = id;
        mCategoria = categoria;
    }
    public int getmId() {
        return mId;
    }

    public String getmCategoria() {
        return mCategoria;
    }

    @NonNull
    @Override
    public String toString() {
        return mCategoria;
    }
}
