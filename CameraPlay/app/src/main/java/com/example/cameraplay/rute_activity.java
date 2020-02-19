package com.example.cameraplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class rute_activity extends AppCompatActivity {
    private Button mBtnCam;
    private Button mBtnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rute_layout);

        mBtnCam = findViewById(R.id.btnCam);
        mBtnMap = findViewById(R.id.btnMap);

        mBtnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (rute_activity.this, MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        mBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (rute_activity.this, MapsActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }


}
