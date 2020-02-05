package com.example.adivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AhorcadoActivity extends AppCompatActivity {
    private final int MAX_SIZE=10;
    private TextView[] tvLetras = new TextView[MAX_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ahorcado_layout);


        //Enlazo desde el codigo con los TextViews de las letras:
        for(int i = 0; i < MAX_SIZE; i++) {
            //Obtener de forma dinamica el identificador del id del TextView
            String nombreRecurso = String.format("tvLetra%s",i);
            int resId = getResources().getIdentifier(nombreRecurso,"id", getPackageName());
            tvLetras[i] = findViewById(resId);
            tvLetras[i].setVisibility(View.INVISIBLE);
        }

        Resources res = getResources();
        String[] palabras = res.getStringArray(R.array.palabras_array);
        Log.e("AhorcadoActivity", "Array -> " + palabras );
        int totalpalabras = palabras.length;
        JAhorcado ah = new JAhorcado(palabras);

        char[] palabra = ah.obtenerPal(totalpalabras);
        for(int i = 0; i < palabra.length; i++) {
            tvLetras[i].setVisibility(View.VISIBLE);
        }

    }
}
