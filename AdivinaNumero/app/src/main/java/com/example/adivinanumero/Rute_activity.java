package com.example.adivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Rute_activity extends AppCompatActivity {
    private Button mBtnAdivina;
    private Button mBtTragaperras;
    private Button mBtAhorcado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rute_layout);
        TextView resEmail = findViewById(R.id.txtEmail);
        //Recogemos las variables que nos puedan enviar, como un post en js:
        String email = getIntent().getExtras().getString("email");
        resEmail.setText(String.format("Bienvenido %s", email));

        mBtnAdivina = findViewById(R.id.btnAdivina);
        mBtTragaperras = findViewById(R.id.btntragaperras);
        mBtAhorcado = findViewById(R.id.btnAhorcado);


        mBtnAdivina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent (AhorcadoActivity.this, AyudaAcitivity.class);
                Intent intent = new Intent (Rute_activity.this, MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        mBtTragaperras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent (AhorcadoActivity.this, AyudaAcitivity.class);
                Intent intent = new Intent (Rute_activity.this, TragaperrasActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        mBtAhorcado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent (AhorcadoActivity.this, AyudaAcitivity.class);
                Intent intent = new Intent (Rute_activity.this, AhorcadoActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }
}
