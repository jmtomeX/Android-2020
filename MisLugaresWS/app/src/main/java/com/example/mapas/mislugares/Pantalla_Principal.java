package com.example.mapas.mislugares;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Pantalla_Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diseno_principal);

        /*
        //Crear la lista de lugares concretos:
        final ArrayList<Lugar> lugares = new ArrayList<>();
        //Creamos la ficha de la torre eiffel, y la añadimos a la lista:
        lugares.add(new Lugar("Torre Eiffel", "Paris",48.858,2.294));
        lugares.add(new Lugar("Cebanc", "Donosti",43.304,-2.019));
        lugares.add(new Lugar("Estatua de la libertad", "New York",40.689,-74.046));
        lugares.add(new Lugar("Playa de la kontxa", "Donosti",43.317,-2.000));
        */
        //En lugar de meter los datos en la propia app, los pedimos por Internet:
        new SWListar().execute();

    }

    //Modelar el concepto de Lugar (la ficha en blanco):
    public class Lugar {
        //Definimos los datos que me interesan de un lugar:
        public String Nombre;
        public String Ciudad;
        public double Latitud;
        public double Longitud;

        //Función, que rellena los datos del lugar:
        public Lugar(String nombre, String ciudad, double latitud, double longitud){
            Nombre = nombre;
            Ciudad = ciudad;
            Latitud = latitud;
            Longitud = longitud;
        }

    }

    public class DatosLugares extends ArrayAdapter<Lugar> {
        private Context context;
        ArrayList<Lugar> lugares;

        public DatosLugares(Context context, ArrayList<Lugar> lugares) {
            super(context, -1, lugares);
            this.context = context;
            this.lugares = lugares;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.diseno_principal_fila, parent, false);

            TextView caja_nombre = rowView.findViewById(R.id.caja_nombre);
            TextView caja_ciudad = rowView.findViewById(R.id.caja_ciudad);

            caja_nombre.setText(lugares.get(position).Nombre);
            caja_ciudad.setText(lugares.get(position).Ciudad);
            return rowView;
        }
    }

    //Conecto al SW, obtengo un json con los datos de places
    //Generar un ArrayList con objetos JPlace (mItems)
    //Entre el doInBackground y el onPostExecute, pasamos la lista de lugares recibidos
    public class SWListar extends AsyncTask<String, Void, ArrayList<Lugar>> {
        private ProgressDialog mPD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Creamos el dialogo de espera:
            mPD = new ProgressDialog(Pantalla_Principal.this);
            mPD.setCancelable(false);
            mPD.setTitle("Conectando...");
            mPD.show();
        }

        @Override
        protected ArrayList<Lugar> doInBackground(String... params) {
            ArrayList<Lugar> lista_lugares = null;
            try {
                lista_lugares = ListarPlaces();
            } catch (Exception ex) {
                Log.e("SWListar", String.format("%s (doInBack): %s", ex.getStackTrace()[0].getMethodName(), ex.getMessage()));
            }
            return lista_lugares;
        }

        @Override
        protected void onPostExecute(final ArrayList<Lugar> lista_lugares) {
            super.onPostExecute(lista_lugares);
            try {
                mPD.cancel();
            } catch (Exception e) {}
            try {
                if (lista_lugares==null) {
                    Toast.makeText(Pantalla_Principal.this, "No he recibido places!", Toast.LENGTH_SHORT).show();
                } else {
                    ListView lv_lugares = findViewById(R.id.lista_lugares);
                    lv_lugares.setAdapter(
                            new DatosLugares(Pantalla_Principal.this, lista_lugares));

                    //Me interesa evento click sobre un elemento (item) de la lista:
                    lv_lugares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            //Lanzar la pantalla del mapa:
                            Intent mapa = new Intent(Pantalla_Principal.this,
                                    Pantalla_Mapa.class);
                            //Pasamos a la pantalla del mapa, latitud, longitud y nombre del lugar
                            //donde el usuario ha tocado:
                            mapa.putExtra("nombre",lista_lugares.get(position).Nombre);
                            mapa.putExtra("latitud",lista_lugares.get(position).Latitud);
                            mapa.putExtra("longitud",lista_lugares.get(position).Longitud);
                            startActivity(mapa);
                        }
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    private ArrayList<Lugar> ListarPlaces(){
        JSONArray jsonResponse=null;
        ArrayList<Lugar> lista_lugares = new ArrayList<>();
        try {
            JSONObject jsonRequest = new JSONObject();
            String respuesta=ServerConnection(jsonRequest,"places_list.php",null);
            if (respuesta!=null)
                try {
                    jsonResponse = new JSONArray(respuesta);
                } catch (Exception e) {
                    //Log.e(TAG,"Sync_Eventos doIn: (jsonResponse):"+e.getMessage());
                    jsonResponse=null;
                }
            if (jsonResponse!=null) {
                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject jsonItem = jsonResponse.getJSONObject(i);
                    String name = jsonItem.getString("name");
                    String city = jsonItem.getString("city");
                    String url = jsonItem.getString("url");
                    double lat = jsonItem.getDouble("lat");
                    double lon = jsonItem.getDouble("lon");
                    lista_lugares.add(new Lugar(name, city, lat, lon));
                }
            } else {
                throw new Exception("JSONResponse es nulo");
            }
        } catch (Exception ex) {
            Log.e("ListarPlaces", String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
        }
        return lista_lugares;
    }

    public String ServerConnection(JSONObject jsonRequest, String sw_ruta, String token){
        String SW_ROOT="http://192.168.43.148/myplaces_WS";
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
            //Log.e(TAG, String.format("%s: %s",ex.getStackTrace()[0].getMethodName(),ex.getMessage()));
            return null;
        }
    }
}
