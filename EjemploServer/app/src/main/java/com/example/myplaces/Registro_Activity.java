package com.example.myplaces;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class Registro_Activity extends Sync_Activity {
    private final String TAG="Registro_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_layout);

        findViewById(R.id.btn_registrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtener los datos introducidos:
                EditText etNombre = (EditText)findViewById(R.id.et_nombre);
                String nombre = etNombre.getText().toString();

                EditText etMail = (EditText)findViewById(R.id.et_email);
                String eMail = etMail.getText().toString();

                //Obtener lo que el usuario ha escrito en la caja de texto PASS:
                EditText etPass = (EditText)findViewById(R.id.et_pass);
                String pass = etPass.getText().toString();
                SWRegistrar registrar = new SWRegistrar();
                registrar.execute(nombre, eMail, pass);
            }
        });
    }

    public class SWRegistrar extends AsyncTask<String, Void, Integer> {
        private ProgressDialog mPD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Creamos el dialogo de espera:
            mPD = new ProgressDialog(Registro_Activity.this);
            mPD.setCancelable(false);
            mPD.setTitle("Conectando...");
            mPD.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            int id = 0;
            try {
                String nombre = params[0];
                String mail = params[1];
                String pass = params[2];
                id = RegistrarUsuario(nombre,mail,pass);
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

                    Toast.makeText(Registro_Activity.this, "Usuario registrado OK!", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(Registro_Activity.this,Places_Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Registro_Activity.this, "Ha fallado el registro!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        }
    }

    private int RegistrarUsuario(String nombre, String mail, String pass){
        int id = 0;
        JSONObject jsonResponse=null;
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("nombre", nombre);
            jsonRequest.put("email", mail);
            jsonRequest.put("pass", pass);
            String respuesta=ServerConnection(jsonRequest,SW_ROOT,"user_registro.php",null);
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
