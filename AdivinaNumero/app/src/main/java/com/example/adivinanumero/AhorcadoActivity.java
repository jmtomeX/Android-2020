package com.example.adivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class AhorcadoActivity extends AppCompatActivity {
    private final int MAX_SIZE=10;
    private TextView[] tvLetras = new TextView[MAX_SIZE];
    private char[] palabra;
    private  JAhorcado ah;

    private EditText entradaLetra;
    private ImageView imageAhorcado;
    private Button mbtnCheck;
    private TextView txtVpalabra;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ahorcado_layout);

        entradaLetra = findViewById(R.id.inputLetras);
        imageAhorcado = findViewById(R.id.imageAhorcado);
        mbtnCheck = findViewById(R.id.btnCheck);
        txtVpalabra = findViewById(R.id.txtVpalabra);

        //Enlazo desde el codigo con los TextViews de las letras:
        for (int i = 0; i < MAX_SIZE; i++) {
            //Obtener de forma dinamica el identificador del id del TextView
            String cajasTexto = String.format("tvLetra%s", i);
            int resId = getResources().getIdentifier(cajasTexto, "id", getPackageName());
            tvLetras[i] = findViewById(resId);
            // las ocultamos
            tvLetras[i].setVisibility(View.GONE);
        }


        Resources res = getResources();
        String[] palabras = res.getStringArray(R.array.palabras_array);
        Log.e("AhorcadoActivity", "Array -> " + palabras);
        int totalpalabras = palabras.length;

        ah = new JAhorcado(palabras);
        // obtenemos las letras para mostrar las casillas que hacen falta
        palabra = ah.obtenerPal(totalpalabras);
        for (int i = 0; i < palabra.length; i++) {
            tvLetras[i].setVisibility(View.VISIBLE);
        }


            mbtnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int contErrores = ah.getContErrores();
                    if (contErrores < 5) {
                        // comprobamos la entrada del usuario y eliminamos sobrantes
                        String cadena = entradaLetra.getText().toString();
                        if (!cadena.equals("")) {
                            char letra = ah.comprobarLetra(cadena);

                            // comprobamos la letras si son iguales.
                            boolean resultado = ah.comprobarIgualdad(palabra, letra);
                            char[] igualdades = ah.getLetraRes();
                            // Colocamos las letras.
                            for (int i = 0; i < igualdades.length; i++) {
                                String s = "" + igualdades[i];
                                tvLetras[i].setText(s);
                            }
                            if (!resultado) {
                                contErrores++;
                                ah.setContErrores(contErrores);
                                String cadenaImg = String.format("ahorcado%s", contErrores);
                                int resId = getResources().getIdentifier(cadenaImg, "drawable", getPackageName());
                                imageAhorcado.setImageResource(resId);
                                txtVpalabra.setText(String.format("Errores %d", contErrores));
                            } else {
                                if (ah.getPalabra().length() == ah.getAciertos()) {
                                    Toast.makeText(getApplicationContext(), "Has ganado el juego.", Toast.LENGTH_SHORT).show();
                                    mbtnCheck.setEnabled(false);
                                }
                            }
                            entradaLetra.setText("");
                        } else {
                            Toast.makeText(getApplicationContext(), "No has ingresado una letra.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Has perdido el juego.", Toast.LENGTH_SHORT).show();
                        txtVpalabra.setText(ah.getPalabra());
                        mbtnCheck.setEnabled(false);
                    }
                }
            });

        entradaLetra.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    mbtnCheck.performClick();
                }
                return false;
            }
        });

        // sonidos al pinchar y multi idioma y pantalla de selección de juego ListView.
        // buscar la palabra al pinchar sobre ella en google
        // taclado con botones
    }
}
