package com.example.cameraplay;

public class JFoto {
    private int mID;
    private String mFileName;

    public int getID() {
        return mID;
    }
    public String getFileName() {
        return mFileName;
    }
    public JFoto (int id, String filename) {
        mID = id;
        mFileName = filename;
    }
}
