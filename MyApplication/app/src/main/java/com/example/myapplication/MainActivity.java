package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText mTextInputAyuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Quiero controlar el evento click sobre boton de acceso: (TENGO QUE ENLAZAR CON EL CONTROL ADECUADO)
        Button btnAcceso = findViewById(R.id.btnAcceso);
        mTextInputAyuda = findViewById(R.id.textInputAyuda);
        final ImageView imgLogo =(ImageView)findViewById(R.id.imgLogo);

        btnAcceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MainActivity","onCreate: btnAcceso.click");
            }
        });

        Button btnAyuda = findViewById(R.id.btnAyuda);

        btnAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textInput = mTextInputAyuda.getText().toString();
                //Para formatear textos con variables:

                //Mensaje emergente que se cierra solo:
                Toast.makeText(MainActivity.this,
                        String.format("Esta es la ayuda %s",textInput), Toast.LENGTH_SHORT).show();
            }
        });
        imgLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    imgLogo.setImageResource(R.drawable.indice);
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    imgLogo.setImageResource(R.drawable.img_logotipo);
                }
                return true;
            }
        });

    }

}
