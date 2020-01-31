package com.example.adivinanumero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int numeroMachine = (int) (Math.random() * 3) + 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mbtnPlay = findViewById(R.id.btnPlay);
       final EditText minputNumber = findViewById(R.id.inputNumber);

        mbtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MainActivity",""+numeroMachine);
                String textoNum = minputNumber.getText().toString();

                int numberUser = Integer.parseInt(textoNum);
                String msg = "";
                if(numberUser == numeroMachine) {
                    msg = "Has acertado el número";
                    numeroMachine = (int) (Math.random() * 3) + 1;
                    Log.e("MainActivity","" + numeroMachine);
                } else {
                    msg = "No has acertado el número";
                }

                Toast.makeText(MainActivity.this,
                        String.format("Respuesta %s",msg), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
