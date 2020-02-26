package com.example.api_gastos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListExpendituresActivity extends Sync_Activity {
    private final String TAG = "ListExpendituresAct";
    private ArrayList listExpenditure;
    private ListView mListView;
    private FloatingActionButton mbtnNewExpenditure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_expenditures_layout);
        mbtnNewExpenditure = findViewById(R.id.btnNewExpenditure);
        mListView = findViewById(R.id.lvGastos);

        mbtnNewExpenditure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListExpendituresActivity.this, NewExpenditureActivity.class);
                startActivity(intent);
            }
        });

        //Comprobar si mUser tiene algo o null:
        if (mUser!=null) {
            //TODO:: llamar al async task
            new ListExpendituresActivity.expenditureGetAsync().execute();
        } else {
            finish();
        }
    }

    //
    private int expenditures(){
        int totalExpenditures = 0;
        JSONObject jsonResponse=null;
        try {
            String respuesta = ServerConnection(null,"expenditures","GET",mUser.getToken());
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
                    listExpenditure = new ArrayList<JExpenditure>();
                    //Recorremos todos los objetos del josnarray:
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject row = jsonData.getJSONObject(i);
                        String date =row.getString("date");
                        String desc =row.getString("description");
                        int cantidad =row.getInt("amount");
                        String tipo_gasto =row.getString("type");
                        String categoria =row.getString("category");
                        JExpenditure exp = new JExpenditure( desc ,date , cantidad, tipo_gasto, categoria);
                        Log.e(TAG,"Exp: "+exp);
                        listExpenditure.add(exp);

                    }
                    totalExpenditures = listExpenditure.size();


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
        return totalExpenditures;
    }

    private class expenditureGetAsync extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog mPD;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Creamos el dialogo de espera:
            mPD = new ProgressDialog(ListExpendituresActivity.this);
            mPD.setCancelable(false);
            mPD.setTitle("Conectando...");
            mPD.show();
        }

        @Override
        protected Integer doInBackground(String... parametros) {
            int expend = 0;
            try {
                 expend = expenditures();


            } catch (Exception ex) {
                Log.e("ServerConnection", String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
            }

            return expend;
        }

        @Override
        protected void onPostExecute(Integer expend) {
            super.onPostExecute(expend);
            try {
                mPD.cancel();
                if(expend > 0) {
                    ExpendituresAdapter expAdapter = new ExpendituresAdapter(ListExpendituresActivity.this,
                            R.layout.list_expenditures_row_layout,
                            listExpenditure);
                    mListView.setAdapter(expAdapter);
                } else {
                    Toast.makeText(ListExpendituresActivity.this,
                    "No he recibido gastos", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {}

        }
    }

    public class ExpendituresAdapter extends ArrayAdapter<JExpenditure> {

        private Context mContext;

        public ExpendituresAdapter(Context context, int resource, ArrayList<JExpenditure> gastos) {
            super(context, resource, gastos);
            mContext = context;
        }


        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            ExpenditureRowViewHolder uvh;

            if (convertView == null) {
                // Solo infla el diseño si aún no hay una vista reciclada
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                convertView = mInflater.inflate(R.layout.list_expenditures_row_layout, parent, false);

                // Etiqueta la vista con una clase que contenga las vistas encontradas con
                // findViewById () para evitar búsquedas posteriores
                uvh = new ExpenditureRowViewHolder(convertView);
                convertView.setTag(uvh);
            } else {
                // Si la vista no es nula, entonces ya habrá
                // creó el titular de la vista y lo configuró como una etiqueta
                uvh = (ExpenditureRowViewHolder) convertView.getTag();
            }

            // Ahora solo coloca al usuario en la posición especificada y
            // configura la vista según sea necesario
            JExpenditure gasto = getItem(pos);
            String fecha =  String.format("%s",gasto.getmFecha());
            String descripcion = String.format("%s", gasto.getmDescripcion());
            float cantidad = gasto.getmCantidad();


            uvh.tvExpFecha.setText(gasto.getmFecha());
            uvh.tvExpDescipcion.setText(gasto.getmDescripcion());
            uvh.tvExpCantdad.setText(String.valueOf(cantidad));
            //Es lo mismo que esto:
            //uvh.tvExpCantdad.setText(""+cantidad);

            return convertView;
        }
        // conexion con el layout row
        public class ExpenditureRowViewHolder {
            public final TextView tvExpFecha;
            public final TextView tvExpDescipcion;
            public final TextView tvExpCantdad;

            public ExpenditureRowViewHolder(View v) {
                tvExpFecha = v.findViewById(R.id.tvRowFecha);
                tvExpDescipcion = v.findViewById(R.id.tvRowDesc);
                tvExpCantdad = v.findViewById(R.id.tvRowCantidad);
            }
        }
    }




}
