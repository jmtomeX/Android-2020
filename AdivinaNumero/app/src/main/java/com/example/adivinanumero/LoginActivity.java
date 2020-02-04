package com.example.adivinanumero;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText boxEditEmail;
    EditText boxEditPassword;
    Button mbtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
         boxEditEmail = findViewById(R.id.boxtxtEmail);
         boxEditPassword = findViewById(R.id.boxtxtPassword);
         mbtnLogin = findViewById(R.id.btnLogin);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String email_admin = "iremti2@gmail.com";
        final String passw_admin = "11111111";

        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = boxEditEmail.getText().toString();
                String password = boxEditPassword.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Inserta una dirección de correo", Toast.LENGTH_SHORT).show();
                } else {
                    if (email.trim().matches(emailPattern)) {
                        if (email.equals(email_admin) && password.equals(passw_admin)) {
                            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Intent intent = new Intent(LoginActivity.this, TragaperrasActivity.class);
                            //Pasar la var email entre activitis
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Inserta una dirección de correo válida", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
