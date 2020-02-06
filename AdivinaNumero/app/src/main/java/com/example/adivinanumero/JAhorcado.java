package com.example.adivinanumero;

import java.util.Random;

public class JAhorcado {
    // atributos
    private String[] palabras;
    private  int contErrores;
    private String palabra;
    private char[] letraRes;
    private int aciertos;

    public int getAciertos() {
        return aciertos;
    }

    // getters
    public String getPalabra() {
        return palabra;
    }
    public int getContErrores() {
        return contErrores;
    }

    public void setContErrores(int contErrores) {
        this.contErrores = contErrores;
    }

    public char[] getLetraRes() {
        return letraRes;
    }

    // constructor
    public JAhorcado(String[] palabras)
    {
        this.palabras = palabras;
        aciertos =0;
    }

    // funciones

    // recibe un entero con el total de palabras en el array, devuelve un array de char con la palabra desconpuesta.
    public char[] obtenerPal(int totalpalabras) {
        contErrores = 0;
        Random r = new Random();
        int numAleatorio = r.nextInt(totalpalabras);
        // cogemos una palabra la azar
        palabra = palabras[numAleatorio].toLowerCase();
        // obtenemos los carácteres
        char[] letrasPalabra = palabra.toCharArray();
        // le damos tamaño al char[] que va conteniendo el resultado
        letraRes =  new char[letrasPalabra.length];
        //Inicializamos con guion bajo
        for (int i=0; i< letraRes.length; i++) {
            letraRes[i] = '_';
        }
        return letrasPalabra;
    }

    // comprobamos si la letra tiene sobrantes, recibe un char y devuelve un char
    public char comprobarLetra(String cadena) {
        cadena = cadena.toLowerCase();
        char letra = cadena.trim().charAt(0);
        return letra;
    }

    // comprobamos si hay letras iguales, devolvemos un array de char con las letras y su posicion
    public boolean comprobarIgualdad(char[] palabra, char letra) {
       boolean resultado = false;
        for (int i = 0; i < palabra.length; i++) {
            if (palabra[i] == (letra)) {
                letraRes[i] = letra;
                aciertos++;
                resultado = true;
            }
        }
        return resultado;
    }



}
