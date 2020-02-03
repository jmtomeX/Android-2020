package com.example.adivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int numeroMachine = (int) (Math.random() * 5) + 1;
    int cont = 0;
    // contador
    TextView boxCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Recogemos las variables que nos puedan enviar:
        String email = getIntent().getExtras().getString("email");
        Button mbtnPlay = findViewById(R.id.btnPlay);

        boxCont = findViewById(R.id.boxTxtPlays);
        TextView resEmail = findViewById(R.id.boxTxtRes);
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
                    numeroMachine = (int) (Math.random() * 3) + 1;
                    Log.e("MainActivity", "" + numeroMachine);
                    cont = 0;
                } else {
                    msg = "No has acertado el número";
                }

                Toast.makeText(MainActivity.this,
                        String.format("Respuesta %s", msg), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
