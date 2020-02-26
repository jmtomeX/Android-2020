package com.example.api_gastos;

import androidx.annotation.NonNull;

public class JType {
    private int mId;
    private String mType;

    public JType(int id, String type) {
        mId = id;
        mType = type;
    }
    public int getmId() {
        return mId;
    }

    public String getType() {
        return mType;
    }

    @NonNull
    @Override
    public String toString() {
        return mType;
    }
}
