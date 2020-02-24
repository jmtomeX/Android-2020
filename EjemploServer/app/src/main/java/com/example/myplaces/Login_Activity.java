package com.example.myplaces;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login_Activity extends Sync_Activity implements View.OnClickListener {
    private final String TAG="Login_Activity";

    private EditText etMail;
    private EditText etPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //Controlar el evento click sobre el botón de Validar:

        //Enlazar desde el código con el botón validar del layout:
        Button btnValidar = (Button)findViewById(R.id.btn_validar);
        //Controlar el evento click sobre el botón, y responder al evento:
        btnValidar.setOnClickListener(this);

        Button btnRegistro = (Button)findViewById(R.id.btn_Registro);
        //Controlar el evento click sobre el botón, y responder al evento:
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cambiamos a la pantalla de registro:
                Intent cambiar_pantalla = new Intent(Login_Activity.this,Registro_Activity.class);
                startActivity(cambiar_pantalla);
            }
        });

    }
    //Ejemplo de creación de un nuevo "sub bloque" FUNCION
    public void responderBotonValidar(){

        //Obtener lo que el usuario ha escrito en la caja de texto EMAIL
        etMail = (EditText)findViewById(R.id.et_email);
        String eMail = etMail.getText().toString();

        //Obtener lo que el usuario ha escrito en la caja de texto PASS:
        etPass = (EditText)findViewById(R.id.et_pass);
        String pass = etPass.getText().toString();

        //Llamamos al sericio Web para validar:
        SWValidar swValidar = new SWValidar();
        swValidar.execute(eMail,pass);
    }

    @Override
    public void onClick(View view) {
        responderBotonValidar();
    }


    public class SWValidar extends AsyncTask<String, Void, Integer> {
        private ProgressDialog mPD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Creamos el dialogo de espera:
            mPD = new ProgressDialog(Login_Activity.this);
            mPD.setCancelable(false);
            mPD.setTitle("Conectando...");
            mPD.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            int id = 0;
            try {
                String mail = params[0];
                String pass = params[1];
                id = ValidarUsuario(mail,pass);
            } catch (Exception ex) {
                Log.e(TAG, String.format("%s (Ad_Click): %s", ex.getStackTrace()[0].getMethodName(), ex.getMessage()));
            }
            return id;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);
            try {
                mPD.cancel();
            } catch (Exception e) {}
            try {
                if (res>0) {
                    //Guardar en las preferencias el token del usuario::
                    SharedPreferences pref = getApplication().getSharedPreferences("app",0);
                    SharedPreferences.Editor editor = pref.edit();
                    //Guardamos el id del socio:
                    editor.putInt("user_id",res);
                    editor.apply();

                    Toast.makeText(Login_Activity.this, "Usuario Correcto!", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(Login_Activity.this,Places_Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login_Activity.this, "Usuario Incorrecto!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        }
    }


    private int ValidarUsuario(String mail, String pass){
        int id = 0;
        JSONObject jsonResponse=null;
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("email", mail);
            jsonRequest.put("pass", pass);
            String respuesta=ServerConnection(jsonRequest,SW_ROOT,"user_login.php",null);
            if (respuesta!=null)
                try {
                    jsonResponse = new JSONObject(respuesta);
                } catch (Exception e) {
                    //Log.e(TAG,"Sync_Eventos doIn: (jsonResponse):"+e.getMessage());
                    jsonResponse=null;
                }
            if (jsonResponse!=null) {
                id = jsonResponse.getInt("res");
            } else {
                throw new Exception("JSONResponse es nulo");
            }
        } catch (Exception ex) {
            Log.e(TAG, String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
        }
        return id;
    }
}
