package com.example.api_gastos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class ListExpendituresActivity extends Sync_Activity {
    private JUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_expenditures_layout);

        //Leemos de las preferencias el usuario actual:
        SharedPreferences prefs = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String token = prefs.getString("prefToken", "");
        String name = prefs.getString("prefName", "");
        String email = prefs.getString("prefEmail", "");
        int id = prefs.getInt("prefEmail", 0);
        if (id>0) {
            mUser = new JUser(id, email,name,token);
            //TODO:: llamar al async task

        } else {
            //Eliminamos las preferencias:
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            finish();
        }
    }

    //
    private String expenditures(){
        String token = null;
        JSONObject jsonResponse=null;
        try {
            String respuesta = ServerConnection(null,"expenditure","POST",null);
            if (respuesta!=null)
                try {
                    jsonResponse = new JSONObject(respuesta);
                } catch (Exception e) {
                    //Log.e(TAG,"Sync_Eventos doIn: (jsonResponse):"+e.getMessage());
                    jsonResponse=null;
                }
            if (jsonResponse!=null) {
                /*
                {
                    "success": true,
                    "data": [
                        {
                            "id": 1,
                            "date": "2004-01-29",
                            "description": "Velit non quae laborum aut.",
                            "amount": 600,
                            "type_id": 4,
                            "file": null,
                            "type": "Casa",
                            "category_id": 3,
                            "category": "Seguros"
                        },
            * */
                //Comprobamos si "success" es true o false:
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    JSONArray jsonData = jsonResponse.getJSONArray("data");
                    //Recorremos todos los objetos del josnarray:
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject row = jsonData.getJSONObject(i);
                        String date =row.getString("date");
                        String desc =row.getString("description");
                        int cantidad =row.getInt("amount");
                        String tipo_gasto =row.getString("type");
                        String categoria =row.getString("category");
                    }
                    //TODO : crear la clase jexpenditure y crear un objeto jexpenditure porcada fila y meterlo en un jexpenditure que sera el que
                   // TODO  alimente el listview

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
}
