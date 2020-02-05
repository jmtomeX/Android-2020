package com.example.adivinanumero;

import java.util.Random;

public class JAhorcado {
    // atributos
    private String[] palabras;

    // constructor
    public JAhorcado(String[] palabras) {
        this.palabras = palabras;
    }

    // funciones

    // recibe un entero con el total de palabras en el array, devuelve un array de char con la palabra desconpuesta.
    public char[] obtenerPal(int totalpalabras) {
        Random r = new Random();
        int numAleatorio = r.nextInt(totalpalabras);
        // cogemos una palabra la azar
        String palabra = palabras[numAleatorio];
        // obtenemos los carácteres
        char[] letrasPalabra = palabra.toCharArray();

        return letrasPalabra;
    }



}
