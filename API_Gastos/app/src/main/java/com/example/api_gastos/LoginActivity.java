package com.example.api_gastos;

import android.app.ProgressDialog;
import android.content.Context;
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

public class LoginActivity extends Sync_Activity {
    private final String TAG="LoginActivity";
    private EditText mEdTxtEmail;
    private EditText mEdtxtpassword;
    private Button mbtnLogin;
    private JUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mUser = null;
        mEdTxtEmail = findViewById(R.id.boxTxtEmail);
        mEdtxtpassword = findViewById(R.id.boxTxtPassword);
        mbtnLogin = findViewById(R.id.btnLogin);

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String email_admin = "iremti2@gmail.com";
        final String passw_admin = "11111111";

        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEdTxtEmail.getText().toString();
                String password = mEdtxtpassword.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Inserta una dirección de correo", Toast.LENGTH_SHORT).show();
                } else {
                    if (email.trim().matches(emailPattern)) {
                        Log.e("LoginActivity", "Email: " + email + " Comntaseña: " + password);
                        new LoginPostAsync().execute(email,password);
                    } else {
                        Toast.makeText(getApplicationContext(), "Inserta una dirección de correo válida", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private String ValidarUsuario(String mail, String pass){
        String token = null;
        JSONObject jsonResponse=null;
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("email", mail);
            jsonRequest.put("password", pass);
            String respuesta=ServerConnection(jsonRequest,"login","POST",null);
            if (respuesta!=null)
                try {
                    jsonResponse = new JSONObject(respuesta);
                } catch (Exception e) {
                    //Log.e(TAG,"Sync_Eventos doIn: (jsonResponse):"+e.getMessage());
                    jsonResponse=null;
                }
            if (jsonResponse!=null) {
                /* Respuesta esperada si hay ido bien:
                {
                    "success": true,
                    "data": {
                        "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMjFmMzQ4ZjE0MWQ1OWUzNGM1MTY3ZjQ4MTZkZGZlZDA2YWZiMWZhODU3NmNiNjU1M2UwNTM0MThhYTg0YTdiNWYzNzkxOTNmNWI3M2Q2MjYiLCJpYXQiOjE1ODI1NjA1MzcsIm5iZiI6MTU4MjU2MDUzNywiZXhwIjoxNjE0MTgyOTM3LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.sylACjno1qTIC-AdtOyk6IbGc8YVs6ZM99zK645K2rCiJ2n2fOEDyq5Y4Hc4250VNKq7jrZMd9Im3GL5-XdA-iAOUq6bw7gNwUIiepf_S_ESTyG8_6cnJh2m3k74yt2Tzi-ZQCCoEE6b_a6fvNM_C8YyIUIcsUYTt_F7mkVddtfhajV6IWRiMRE4KbBSuC6dYkSeO6aL68b8ps_G_OLEw1b0jey1wLvvPIxBMS19Z67-D6lZPdKYtSmmjie9LtvBKid1Q8_Q94U5M5uen5t148r41JzuPWlgoKHAvV3HvL2HvGUT8RR4zjbhz4jT_78PDehIUFzBh_Gp1JileA8pt-1O8gz5w45rWb862qX1Z3x_g0ih08A1t1Z_aneSzED8lQ6XfZDZftlUIiFUkYeoYm0cS0m4E4XDw_TlfSf7cmk9ef6J8vgCto_N9Nizk6JbmvL6dVONKW0RVTm29eOQQgRnxuWjzl7dEr_xSpDY4pmN8p-j32ZGm1sx_lhPU3vqtvfOvTA5Jpuizrc2JdY4Tl8KTlbI8IEmE5IM38n47euYvuOLumUQYdIEs0IxlSBhGnRcOlA1eHkBBXwpNtc_1L-XBF4dMUSFV7JipEFv3n_8qwrfgDp3JAE6KJ-MFtDyZan4frmhr6_XJ1FVCaecq47_tVJ2jYGdWOYErXQEcOU",
                        "name": "jose mari"
                    },
                    "message": "User login successfully."
                }
                 */
                //Comprobamos si "success" es true o false:
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    JSONObject jsonData = jsonResponse.getJSONObject("data");
                    //Creamos el objeto JUser:
                    token = jsonData.getString("token");
                } else {
                    String msg = jsonResponse.getString("message");
                }
            } else {
                throw new Exception("JSONResponse es nulo");
            }
        } catch (Exception ex) {
            Log.e(TAG, String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
        }
        return token;
    }

    private boolean ObtenerDatosUsuario(String token){
        mUser = null;
        JSONObject jsonResponse=null;
        try {
            String respuesta=ServerConnection(null,"user","GET",token);
            if (respuesta!=null)
                try {
                    jsonResponse = new JSONObject(respuesta);
                } catch (Exception e) {
                    //Log.e(TAG,"Sync_Eventos doIn: (jsonResponse):"+e.getMessage());
                    jsonResponse=null;
                }
            if (jsonResponse!=null) {
                /* Respuesta esperada si hay ido bien:
                {
                    "success": true,
                    "data": {
                        "id": 1,
                        "name": "jose mari",
                        "email": "iremti2@gmail.com",
                        "email_verified_at": null,
                        "created_at": "2020-02-24 15:59:07",
                        "updated_at": "2020-02-24 15:59:07"
                    },
                    "message": "User login successfully."
                }
                 */
                //Comprobamos si "success" es true o false:
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    JSONObject jsonData = jsonResponse.getJSONObject("data");
                    //Creamos el objeto JUser:
                    int id = jsonData.optInt("id",0);
                    String email = jsonData.optString("email","");
                    String name = jsonData.optString("name","");
                    if (id>0) {
                        mUser = new JUser(id,email,name,token);
                    }
                } else {
                    String msg = jsonResponse.getString("message");
                }
            } else {
                throw new Exception("JSONResponse es nulo");
            }
        } catch (Exception ex) {
            Log.e(TAG, String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
        }
        return mUser != null;
    }

    private class LoginPostAsync extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog mPD;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Creamos el dialogo de espera:
            mPD = new ProgressDialog(LoginActivity.this);
            mPD.setCancelable(false);
            mPD.setTitle("Conectando...");
            mPD.show();
        }

        @Override
        protected Boolean doInBackground(String... parametros) {
            boolean usuario_valido = false;
            try {
                //En parametros, vienen el email y el password:
                String email = parametros[0];
                String password = parametros[1];
                String token = ValidarUsuario(email,password);
                //Si obtenemos un token, llamamos a la API de obtener los datos del usuario:
                if (token!=null) {
                    usuario_valido = ObtenerDatosUsuario(token);
                }

            } catch (Exception ex) {
                Log.e("ServerConnection", String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
            }

            return usuario_valido;
        }

        @Override
        protected void onPostExecute(Boolean usuario_valido) {
            super.onPostExecute(usuario_valido);
            try {
                mPD.cancel();
            } catch (Exception e) {}
            if (usuario_valido) {
                String newToken = mUser.getToken();
                Toast.makeText(LoginActivity.this,"OK, token: "+ newToken ,Toast.LENGTH_LONG).show();
                //TODO: Guardar en las preferencias para poder llamar a otras apis:
                SharedPreferences prefs = getSharedPreferences("datos",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("prefToken", newToken);
                editor.putString("prefName", mUser.getName());
                editor.putString("prefEmail", mUser.getEmail());
                editor.putInt("prefEmail", mUser.getID());
                editor.apply();

                finish();
                Intent intent = new Intent(LoginActivity.this, ListExpendituresActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this,"El usuario no es válido",Toast.LENGTH_LONG).show();
            }
        }


    }


}
