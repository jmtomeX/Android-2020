package com.example.adivinanumero;

import android.util.Log;
import android.widget.ImageView;

import java.util.Random;
public class JTragaperras {

    private int saldo;
    private int num_imagenes;

    private int[] jugada;
    private int[] combinacion;
    private int NUM_CASILLAS;


    // constructor
    public JTragaperras(int num_imagenes, int casillas,  int saldo_inicial) {
        this.saldo = saldo_inicial;
        this.num_imagenes = num_imagenes;
        jugada = new int[num_imagenes];
        NUM_CASILLAS = casillas;
        combinacion = new int[casillas];

    }


    // Getter & setter
    public int getSaldo() {
        return saldo;
    }

    public int[] getCombinacion() {
        return combinacion;
    }

    // funciones

    public void obtenerJugada( ) {
         int numAleatorio ;
         int imgAleatoria;
        for ( int i=0; i < num_imagenes; i++) {
            jugada[i] = 0;
        }
        for(int i = 0; i < NUM_CASILLAS; i++) {
            numAleatorio = (int) (Math.random() * 6);
            //Lo añadimos a nuestra combinación ganadora:
            combinacion[i] = numAleatorio;
            // comprobamos el número y le añadimos 1
            jugada[numAleatorio] ++;

        }
    }

    public int calcularPremio() {
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
        saldo += premio;

        return premio;
    }
}
