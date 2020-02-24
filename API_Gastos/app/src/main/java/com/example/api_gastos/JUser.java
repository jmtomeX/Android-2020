package com.example.api_gastos;

import androidx.annotation.NonNull;

public class JUser {
    private int mID;
    private String mEmail;
    private String mToken;
    private String mName;

    public int getID() {
        return mID;
    }
    public String getEmail() {
        return mEmail;
    }
    public String getName() {
        return mName;
    }
    public String getToken() {
        return mToken;
    }

    public JUser ( int id, String email, String name, String token) {
        mID = id;
        mName = name;
        mEmail = email;
        mToken = token;
    }

    @NonNull
    @Override
    public String toString() {
        return mName;
    }
}
