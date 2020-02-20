package com.example.api_gastos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private EditText mEdTxtEmail;
    private EditText mEdtxtpassword;
    private Button mbtnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
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

    private class LoginPostAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... parametros) {
            try {
                //En parametros, vienen el email y el password:
                String email = parametros[0];
                String password = parametros[1];
                //Preparar la llamada al SW:
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("email", email);
                jsonRequest.put("password", password);
                String respuesta=ServerConnection(jsonRequest,"login",null);
            } catch (JSONException ex) {
                Log.e("ServerConnection", String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String aDouble) {
            super.onPostExecute(aDouble);
        }


        public String ServerConnection(JSONObject jsonRequest, String sw_ruta, String token){
            //String SW_ROOT="http://127.0.0.1:8000/api";
            String SW_ROOT="http://192.168.43.168/Laravel/Laravel/proyecto-v2-gastos/gastos_v2/public/api";
            try {
                String link = String.format("%s/%s",SW_ROOT,sw_ruta);
                URL url = new URL(link);
                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                client.setDoOutput(true);
                client.setDoInput(true);
                client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                if (token!=null)
                    client.setRequestProperty("Authorization", "Bearer " + token);
                client.setRequestMethod("POST");
                client.connect();
                //Comunicación con el servidor
                if (jsonRequest!=null) {
                    OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream());
                    String output = jsonRequest.toString();
                    writer.write(output);
                    writer.flush();
                    writer.close();
                }
                int respCode = client.getResponseCode();
                if (respCode != HttpURLConnection.HTTP_OK) {
                    Log.e("Sync_Act", "INPUT:" + client.getErrorStream());
                    return null;
                } else {
                    InputStream input = client.getInputStream();
                    Log.e("Sync_Act", "INPUT:" + input.toString());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    //Obtenemos el String con la respuesta:
                    String line;
                    while ((line = reader.readLine()) != null) result.append(line);
                    input.close();
                    return result.toString();
                }
            } catch (Exception ex) {
                Log.e("ServerConnection", String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
                return null;
            }
        }



    }


}
