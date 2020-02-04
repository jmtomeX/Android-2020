package com.example.adivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int numeroMachine = (int) (Math.random() * 10) + 1;
    int cont = 0;
    // contador
    TextView boxCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Recogemos las variables que nos puedan enviar, como un post en js:
        String email = getIntent().getExtras().getString("email");
        Button mbtnPlay = findViewById(R.id.btnPlay);

        boxCont = findViewById(R.id.boxTxtPlays);
        TextView resEmail = findViewById(R.id.boxTxtRes);
        final TextView resRecord = findViewById(R.id.boxTxtRecord);
        //Mostramos el record actual:
        SharedPreferences prefs = getSharedPreferences("recordPersonal", Context.MODE_PRIVATE);
        int record_actual = prefs.getInt("recordPersonal", 10000);
        if (record_actual>=10000)
            resRecord.setText("Aun no hay record");
        else
            resRecord.setText(String.format("Tu record es %d", record_actual));
        //resEmail.setText("Bienvenido "+ email );
        resEmail.setText(String.format("Bienvenido %s", email));

        final EditText minputNumber = findViewById(R.id.inputNumber);

        mbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont++;
                boxCont.setText(String.format("Número de intentos %s", cont));
                Log.e("MainActivity", "" + numeroMachine);
                String textoNum = minputNumber.getText().toString();

                int numberUser = Integer.parseInt(textoNum);
                String msg = "";
                if (numberUser == numeroMachine) {
                    msg = "Has acertado el número";
                    boxCont.setText(String.format("Has acertado a los %s intentos \n Introduce otro número para seguir jugando.", cont));
                    //Leer si hay un record ya guardado:
                    SharedPreferences prefs = getSharedPreferences("recordPersonal", Context.MODE_PRIVATE);
                    int record_actual = prefs.getInt("recordPersonal", 10000);
                    //Comprobar si lo hemos mejorado:
                    if (cont < record_actual) {
                        //Hay un nuevo record
                        resRecord.setText(String.format("Tu record es %d", cont));
                        //Guardar en las preferencias el nuevo record:
                        prefs.edit().putInt("recordPersonal", cont).apply();
                    }

                    //Inventamos nuevo numero para la siguiente partida:
                    numeroMachine = (int) (Math.random() * 10) + 1;
                    Log.e("MainActivity", "" + numeroMachine);
                    //Volvemos a poner intentos a cero para la nueva partida
                    cont = 0;
                } else {
                    msg = "No has acertado el número";
                }
                minputNumber.setText("");
                Toast.makeText(MainActivity.this,
                        String.format("Respuesta %s", msg), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
