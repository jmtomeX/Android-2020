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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class Place_Activity extends Sync_Activity {
    private final String TAG="Place_Activity";
    public final static int OP_ADD=1;
    public final static int OP_EDIT=2;
    public final static int OP_DEL=3;

    private TextView tvTitulo;
    private EditText etNombre;
    private EditText etCiudad;
    private EditText etUrl;
    private EditText etLat;
    private EditText etLon;
    private EditText etWeb;
    private EditText etVideo;

    private int mOP;
    private JPlace mPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_layout);

        tvTitulo = findViewById(R.id.tvTitulo);
        etNombre = findViewById(R.id.etNombre);
        etCiudad = findViewById(R.id.etCiudad);
        etUrl = findViewById(R.id.etUrl);
        etLat = findViewById(R.id.etLat);
        etLon = findViewById(R.id.etLon);
        etWeb = findViewById(R.id.etWeb);
        etVideo = findViewById(R.id.etVideo);

        //Distinguimos si es una operación de alta o de editar:
        Bundle datos = getIntent().getExtras();
        mOP = datos.getInt("op");

        if (mOP == OP_ADD) {
            tvTitulo.setText(R.string.place_title_new);
            //Deshabilitamos el botón de eliminar:
            findViewById(R.id.btn_Del).setEnabled(false);
            /*
            findViewById(R.id.btn_Del).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_Del).setVisibility(View.INVISIBLE);
            findViewById(R.id.btn_Del).setVisibility(View.GONE);
            */
        } else {
            //Obtengo los datos del place a editar:
            mPlace = (JPlace)datos.getSerializable("place");

            tvTitulo.setText(R.string.place_title_edit);
            etNombre.setText(mPlace.getName());
            etCiudad.setText(mPlace.getCity());
            etUrl.setText(mPlace.getUrl());
            etLat.setText(""+mPlace.getLat());
            etLon.setText(""+mPlace.getLon());
            etWeb.setText(""+mPlace.getWeb());
            etVideo.setText(""+mPlace.getVideo());
        }


        findViewById(R.id.btn_Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobamos que ha escrito algo en nombre y ciudad:
                if (etNombre.getText().toString().length()<=0) {
                    Toast.makeText(Place_Activity.this,"Indica un nombre",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etCiudad.getText().toString().length()<=0) {
                    Toast.makeText(Place_Activity.this,"Indica la ciudad",Toast.LENGTH_SHORT).show();
                    return;
                }
                float lat = 0f;
                float lon = 0f;
                try {
                    lat = Float.valueOf(etLat.getText().toString());
                } catch (Exception e) {}
                try {
                    lon = Float.valueOf(etLon.getText().toString());
                } catch (Exception e) {}

                if (etWeb.getText().toString().length()<=0) {
                    Toast.makeText(Place_Activity.this,"Indica la web",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etVideo.getText().toString().length()<=0) {
                    Toast.makeText(Place_Activity.this,"Indica el video en youtube",Toast.LENGTH_SHORT).show();
                    return;
                }

                SWPlaceGest placeGest = new SWPlaceGest();
                placeGest.execute(String.valueOf(mPlace.getID()),mPlace.getName(),mPlace.getCity(),
                        mPlace.getUrl(),String.valueOf(mPlace.getLat()),
                        String.valueOf(mPlace.getLon()),mPlace.getWeb(),mPlace.getVideo());
            }
        });

        findViewById(R.id.btn_Del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOP = OP_DEL;
                SWPlaceGest placeGest = new SWPlaceGest();
                placeGest.execute(String.valueOf(mPlace.getID()));
            }
        });

    }

    public class SWPlaceGest extends AsyncTask<String, Void, Integer> {
        private ProgressDialog mPD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Creamos el dialogo de espera:
            mPD = new ProgressDialog(Place_Activity.this);
            mPD.setCancelable(false);
            mPD.setTitle("Conectando...");
            mPD.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            int res=0;
            String id="";
            String name="";
            String city="";
            String url="";
            String lat="";
            String lon="";
            String web="";
            String video="";
            try {
                switch (mOP) {
                    case OP_ADD:
                        name = params[0];
                        city = params[1];
                        url = params[2];
                        lat = params[3];
                        lon = params[4];
                        web = params[5];
                        video = params[6];
                        break;
                    case OP_EDIT:
                        id = params[0];
                        name = params[1];
                        city = params[2];
                        url = params[3];
                        lat = params[4];
                        lon = params[5];
                        web = params[6];
                        video = params[7];
                        break;
                    case OP_DEL:
                        id = params[0];
                        break;
                }
                res = GestionarPlace(String.valueOf(mOP),id,name,city,url,lat,lon,web,video);
            } catch (Exception ex) {
                Log.e(TAG, String.format("%s (Ad_Click): %s", ex.getStackTrace()[0].getMethodName(), ex.getMessage()));
            }
            return res;
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

                    Toast.makeText(Place_Activity.this, "Operación realizada OK!", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(Place_Activity.this,Places_Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Place_Activity.this, "Ha fallado la operación!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        }
    }

    private int GestionarPlace(String op, String id, String name, String city, String url,
                               String lat, String lon, String web, String video){
        int res = 0;
        JSONObject jsonResponse=null;
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("op", op);
            if (id.length()<=0) id="0"; //Si es un alta, enviamos un 0
            jsonRequest.put("id", id);
            jsonRequest.put("name", name);
            jsonRequest.put("city", city);
            jsonRequest.put("url", url);
            jsonRequest.put("lat", lat);
            jsonRequest.put("lon", lon);
            jsonRequest.put("web", web);
            jsonRequest.put("video", video);
            String respuesta=ServerConnection(jsonRequest,SW_ROOT,"place_gest.php",null);
            if (respuesta!=null)
                try {
                    jsonResponse = new JSONObject(respuesta);
                } catch (Exception e) {
                    //Log.e(TAG,"Sync_Eventos doIn: (jsonResponse):"+e.getMessage());
                    jsonResponse=null;
                }
            if (jsonResponse!=null) {
                res = jsonResponse.getInt("res");
            } else {
                throw new Exception("JSONResponse es nulo");
            }
        } catch (Exception ex) {
            Log.e(TAG, String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
        }
        return res;
    }
}
