package com.example.adivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class TragaperrasActivity extends AppCompatActivity {
    private Button mbtnjugar;
    private int[] imagenes = {R.drawable.img_0,R.drawable.img_1,R.drawable.img_2,R.drawable.img_3,R.drawable.img_4,R.drawable.img_5,R.drawable.img_6};
    private ImageView imageView0;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView[] imageViews = new ImageView[4];
    private int[] jugada = new int[imagenes.length];
    private  Button mbtnJugar;

    private int numAleatorio ;
    private int imgAleatoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tragaperras_layout);
        /*
        imageView0 = findViewById(R.id.imageView0);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

         */

        imageViews[0] = findViewById(R.id.imageView0);
        imageViews[1] = findViewById(R.id.imageView1);
        imageViews[2] = findViewById(R.id.imageView2);
        imageViews[3] = findViewById(R.id.imageView3);

        mbtnJugar = findViewById(R.id.btnJugar);

        final Random r = new Random();

        //Registrar evto click sobre boton jugar:
        mbtnJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerJugada();
                calcularPremio();

                /*
                int numAleatorio = r.nextInt(imagenes.length);
                imgAleatoria = imagenes[numAleatorio];
                imageView0.setImageResource(imgAleatoria);

                numAleatorio = r.nextInt(imagenes.length);
                imgAleatoria = imagenes[numAleatorio];
                imageView1.setImageResource(imgAleatoria);

                numAleatorio = r.nextInt(imagenes.length);
                imgAleatoria = imagenes[numAleatorio];
                imageView2.setImageResource(imgAleatoria);

                numAleatorio = r.nextInt(imagenes.length);
                imgAleatoria = imagenes[numAleatorio];
                imageView3.setImageResource(imgAleatoria);
                 */



                /*
                for(int i = 0; i < imageViews.length; i++) {
                    numAleatorio = (int) (Math.random() * 6);
                    imgAleatoria = imagenes[numAleatorio];
                    //Obtener de forma dinamica el identificador del imageview
                    String nombreRecurso = String.format("imageView%s",i);
                    int resId = getResources().getIdentifier(nombreRecurso,"id", getPackageName());
                    ((ImageView)findViewById(resId)).setImageResource(imgAleatoria);
                }
                */

            }
        });
    }

    private void obtenerJugada() {
        for ( int i=0; i< imagenes.length; i++) {
            jugada[i] = 0;
        }
        for(int i = 0; i < imageViews.length; i++) {
            numAleatorio = (int) (Math.random() * 6);
            // comprobamos el número y le añadimos 1
            jugada[numAleatorio] ++;
            imgAleatoria = imagenes[numAleatorio];
            imageViews[i].setImageResource(imgAleatoria);

        }
    }

    private int calcularPremio() {
        //Comprobar si hay una o incluso dos parejas
         int[] premios =  {-1,0,2,5,10};
         int premio = 0;
        for(int i = 0; i < jugada.length; i++){
            if(jugada[i] > premio){ //
                premio += premios[jugada[i]];
                Log.e("TragaperrasActivity", "FOR " + jugada[i]);
            }
        }
        // descontamos 1 a la partida por jugar
        premio += premios[0];
        Log.e("TragaperrasActivity", "El premio ha sido de " + premio);
        return premio;
    }
}
