package com.example.api_gastos;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Sync_Activity extends AppCompatActivity {
    //Cuando utilizo un emulador (maquina virtual) esta sería la dirección del servidor:
    //public final String SW_ROOT="http://10.0.2.2/Laravel/proyecto-v2-gastos/gastos_v2/public/api";
    public final static String SW_ROOT="http://192.168.43.168/Laravel/proyecto-v2-gastos/gastos_v2/public/api";

    protected String ServerConnection(JSONObject jsonRequest, String sw_ruta, String method, String token){
        try {
            String link = String.format("%s/%s",SW_ROOT,sw_ruta);
            URL url = new URL(link);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            //https://stackoverflow.com/questions/8587913/what-exactly-does-urlconnection-setdooutput-affect/8587943#8587943
            if (!method.equals("GET"))
                client.setDoOutput(true);
            client.setDoInput(true);
            client.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (token!=null)
                client.setRequestProperty("Authorization", "Bearer " + token);
            client.setRequestMethod(method);
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
            //Log.e(TAG, String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
            return null;
        }
    }

}