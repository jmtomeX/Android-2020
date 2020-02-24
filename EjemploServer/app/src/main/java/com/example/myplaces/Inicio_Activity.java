package com.example.myplaces;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Inicio_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_layout);

        //Esperamos 3 segundos y pasamos al main:
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pasarAlMain();
            }
        },3000);
    }

    private void pasarAlMain(){
        finish();
        Intent cambiar_pantalla = new Intent(Inicio_Activity.this,Login_Activity.class);
        startActivity(cambiar_pantalla);
    }
}
