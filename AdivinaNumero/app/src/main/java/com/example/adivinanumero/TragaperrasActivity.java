package com.example.adivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class TragaperrasActivity extends AppCompatActivity {
    private int[] imagenes = {R.drawable.img_0,R.drawable.img_1,R.drawable.img_2,R.drawable.img_3,R.drawable.img_4,R.drawable.img_5,R.drawable.img_6};
    private ImageView[] imageViews = new ImageView[4];
    private Button mbtnjugar;
    private ImageView imageView0;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

    private  Button mbtnJugar;
    private TextView mBoxTxtRes;

    private int saldo_inicial;
    private JTragaperras newMachine;

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
        mBoxTxtRes = findViewById(R.id.boxTxtRes);

        SharedPreferences prefs = getSharedPreferences("saldo", Context.MODE_PRIVATE);
        saldo_inicial = prefs.getInt("saldo", 10);


        newMachine = new JTragaperras(imagenes.length,4, saldo_inicial);

        //Registrar evto click sobre boton jugar:
        mbtnJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMachine.obtenerJugada();
                int[] combinacion = newMachine.getCombinacion();

                //Actualizamos la pantalla segun la jugada obtenida:
                for (int i =0; i<combinacion.length ; i++) {
                    int imgAleatoria = imagenes[combinacion[i]];
                    imageViews[i].setImageResource(imgAleatoria);
                }

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
                int premio = newMachine.calcularPremio();
                mBoxTxtRes.setText(String.format("El premio ha sido de %s y el saldo es %s", premio, newMachine.getSaldo()));

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("saldo", Context.MODE_PRIVATE);
        //Guardar en las preferencias el nuevo saldo:
        prefs.edit().putInt("saldo", newMachine.getSaldo()).apply();
    }
}
