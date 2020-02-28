package com.example.api_gastos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListExpendituresActivity extends Sync_Activity {
    private final String TAG = "ListExpendituresAct";
    private ArrayList<JExpenditure> listExpenditure;
    private ListView mListView;
    private FloatingActionButton mbtnNewExpenditure;
    private int itemSelected;
    private int itemSelectedPos;
    private ImageView ivUser;
    private TextView mTxtName;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_expenditures_layout);
        mbtnNewExpenditure = findViewById(R.id.btnNewExpenditure);
        mListView = findViewById(R.id.lvGastos);
        ivUser = findViewById(R.id.ivUser);
        //Comprobar si mUser tiene algo o null:
        if (mUser==null)
            finish();
        else {
            //Cargar la foto del usuario desde el servidor, con la librería Picasso
            Picasso.with(ListExpendituresActivity.this)
                    .load("http://192.168.43.168/Laravel/proyecto-v2-gastos/gastos_v2/public/img_users/user_" + mUser.getID() + ".jpg")
                    .resize(100, 100)
                    .into(ivUser);
            mTxtName = findViewById(R.id.txtName);
            mTxtName.setText(mUser.getName());

            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    itemSelected = listExpenditure.get(position).getid();
                    itemSelectedPos = position;

                    // Diálogo confirmación borrar gasto
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set title
                    alertDialogBuilder.setTitle("Your Title");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Desas eliminar el gasto?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    //MainActivity.this.finish();
                                    new deleteExpenditureAsync().execute();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });


                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                    return false;
                }
            });

            mbtnNewExpenditure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListExpendituresActivity.this, NewExpenditureActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Para que se refresque el contenido cada vez que vuelve al primer plano(Eliminar un item)
        new ListExpendituresActivity.expenditureGetAsync().execute();
    }

    // Servicio web y el método
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
                        int id = row.getInt("id");
                        JExpenditure exp = new JExpenditure( desc ,date , cantidad, tipo_gasto, categoria,id);
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

    // Hilo asincrono necesario para la llamada de servicios web
    // 1º param el [array] que recibe el doInBackground
    //2º param es para hacer una "barra de progreso"
    // 3º Es lo que devuelve el do y recibe el onPostExucute
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

    // Esto va asociado al ListView, recoge un array de objetos y los carga en un ListView
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

    // Eliminar un item de gasto
    private int  deleteExp() {
        int res = 0;
        JSONObject jsonResponse=null;
        try {
            String respuesta = ServerConnection(null,"ExpenditureDelete/"+itemSelected,"GET",mUser.getToken());
            if (respuesta!=null)
                try {
                    jsonResponse = new JSONObject(respuesta);
                } catch (Exception e) {
                    //Log.e(TAG,"Sync_Eventos doIn: (jsonResponse):"+e.getMessage());
                    jsonResponse=null;
                }
            if (jsonResponse!=null) {
                //Comprobamos si "success" es true o false:
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    res = jsonResponse.getInt("data");
                } else {
                    String msg = jsonResponse.getString("message");
                }
            } else {
                throw new Exception("JSONResponse es nulo");
            }
        } catch (Exception ex) {
            Log.e(TAG, String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
        }
        return res;

    }

    private class deleteExpenditureAsync extends AsyncTask<String, Integer, Integer> {
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
                expend = deleteExp();


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
                    Toast.makeText(ListExpendituresActivity.this,
                            "Eliminado", Toast.LENGTH_SHORT).show();
                    //Eliminado, TODO: Refrescar la lista:
                    //new ListExpendituresActivity.expenditureGetAsync().execute();
                    //Estático, sin volver a pedir el listado al SW:
                    listExpenditure.remove(itemSelectedPos);
                    ExpendituresAdapter expAdapter = new ExpendituresAdapter(ListExpendituresActivity.this,
                            R.layout.list_expenditures_row_layout,
                            listExpenditure);
                    mListView.setAdapter(expAdapter);
                } else {
                    Toast.makeText(ListExpendituresActivity.this,
                            "No se han encontado el expendi", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        }
    }

    // Diálogo



}
