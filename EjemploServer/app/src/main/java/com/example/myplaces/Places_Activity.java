package com.example.myplaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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

import com.androidquery.AQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Places_Activity extends Sync_Activity {
    private final String TAG="Places_Activity";

    public ArrayList<JPlace> mItems;
    public ListView mLV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_layout);

        mLV = (ListView)findViewById(R.id.lvPlaces);
        mLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detalle = new Intent(Places_Activity.this,Place_Activity.class);
                detalle.putExtra("op",Place_Activity.OP_EDIT);
                detalle.putExtra("place",mItems.get(position));
                startActivity(detalle);
            }
        });

        //Alta de nuevo lugar:
        findViewById(R.id.btn_Nuevo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detalle = new Intent(Places_Activity.this,Place_Activity.class);
                detalle.putExtra("op",Place_Activity.OP_ADD);
                startActivity(detalle);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SWListar listar = new SWListar();
        //Tenemos que pasarle un id de usuario, de momento un 1 fijo:
        listar.execute("1");
    }

    public class ViewHolder {
        public final View mView;
        public final TextView tvName;
        public final TextView tvCity;
        public final ImageView ivPlace;

        public ViewHolder(View v) {
            mView = v;
            tvName = (TextView) v.findViewById(R.id.tvPlaceName);
            tvCity = (TextView) v.findViewById(R.id.tvPlaceCity);
            ivPlace = (ImageView) v.findViewById(R.id.ivPlace);
        }
    }

    private class ListaAdapter extends ArrayAdapter<JPlace> {
        public ListaAdapter(Context context) {
            super(context, R.layout.places_row_layout, mItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.places_row_layout, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(mItems.get(position).getName());
            holder.tvCity.setText(mItems.get(position).getCity());

            /*
            //Mostramos la imagen, como un recurso dentro de la app:
            String nombre_img = String.format("img_%s",mItems.get(position).getID());
            int resId = getResources().getIdentifier(nombre_img,"drawable",getPackageName());
            holder.ivPlace.setImageResource(resId);
            */

            //Caregamos la imagen desde una dirección de Internet (Son necesarios permisos de Internet):
            AQuery aq = new AQuery(Places_Activity.this);
            aq.id(holder.ivPlace).progress(this).image(mItems.get(position).getUrl(), false, true, 0, R.mipmap.ic_launcher, null, AQuery.FADE_IN);
            return convertView;
        }
    }

    //Conecto al SW, obtengo un json con los datos de places
    //Generar un ArrayList con objetos JPlace (mItems)
    //Entre el doInBackground y el onPostExecute, pasamos un entero, correspondiente al numero de items recibidos
    public class SWListar extends AsyncTask<String, Void, Integer> {
        private ProgressDialog mPD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Creamos el dialogo de espera:
            mPD = new ProgressDialog(Places_Activity.this);
            mPD.setCancelable(false);
            mPD.setTitle("Conectando...");
            mPD.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            int id = 0;
            try {
                String user_id = params[0];
                id = ListarPlaces(user_id);
            } catch (Exception ex) {
                Log.e(TAG, String.format("%s (doInBack): %s", ex.getStackTrace()[0].getMethodName(), ex.getMessage()));
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
                    ListaAdapter adapter = new ListaAdapter(Places_Activity.this);
                    mLV.setAdapter(adapter);
                } else {
                    Toast.makeText(Places_Activity.this, "No he recibido places!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        }
    }

    private int ListarPlaces(String user_id){
        JSONArray jsonResponse=null;
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("user", user_id);
            String respuesta=ServerConnection(jsonRequest,SW_ROOT,"places_list.php",null);
            if (respuesta!=null)
                try {
                    jsonResponse = new JSONArray(respuesta);
                } catch (Exception e) {
                    //Log.e(TAG,"Sync_Eventos doIn: (jsonResponse):"+e.getMessage());
                    jsonResponse=null;
                }
            if (jsonResponse!=null) {
                mItems = new ArrayList<>();
                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject jsonItem = jsonResponse.getJSONObject(i);
                    int id = jsonItem.getInt("id");
                    String name = jsonItem.getString("name");
                    String city = jsonItem.getString("city");
                    String url = jsonItem.getString("url");
                    float lat = (float) jsonItem.getDouble("lat");
                    float lon = (float) jsonItem.getDouble("lon");
                    String web = "";//jsonItem.getString("web");
                    String video = ""; //jsonItem.getString("video");
                    mItems.add(new JPlace(id, name, city, url,
                            lat, lon, web, video));
                }
            } else {
                throw new Exception("JSONResponse es nulo");
            }
        } catch (Exception ex) {
            Log.e(TAG, String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
        }
        return mItems.size();
    }
}
