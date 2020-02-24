package com.example.myplaces;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Carlos on 16/01/2018.
 */

public class Sync_Activity extends AppCompatActivity {
    //public final String SW_ROOT="http://10.0.2.2/myplaces";
    public final static String SW_ROOT="http://192.168.1.100/myplaces";

    public String ServerConnection(JSONObject jsonRequest, String sw_root, String sw_ruta, String token){
        try {
            String link = String.format("%s/%s",sw_root,sw_ruta);
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
            //Log.e(TAG, String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
            return null;
        }
    }

}
