package com.example.api_gastos;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewExpenditureActivity extends Sync_Activity {
    private ArrayList<JCategory> listCategories;
    private ArrayList<JType> mListaTypes;
    private final String TAG = "NewExpenditureActivity";

    // You spinner view
    private Spinner mySpinner;
    private Spinner mySpinnerTypes;
    private SpinAdapter mAdapter;
    private SpinTypeAdapter mTypeAdapter;
    private JCategory mCatSelected;
    private JType mTypeSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_expenditure_layout);
        mySpinner = findViewById(R.id.spCategorias);
        mySpinnerTypes = findViewById(R.id.spTypes);
        if (mUser == null)
            finish();
        else {
            new categoriesGetAsync().execute();
            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    // Here you get the current item (a JCategory object) that is selected by its position
                    mCatSelected = mAdapter.getItem(position);
                    // Here you can do the action you want to...
                    //Toast.makeText(NewExpenditureActivity.this, "ID: " + cat.getmId() + "\nCat: " + cat.getmCategoria(), Toast.LENGTH_SHORT).show();
                    //Cargar los tipos de esta categoria:
                    new typesGetAsync().execute();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });

            mySpinnerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    mTypeSelected = mTypeAdapter.getItem(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });
        }
    }
    private int  categories() {
        int totalExpenditures = 0;
        JSONObject jsonResponse=null;
        try {
            String respuesta = ServerConnection(null,"getCategories","GET",mUser.getToken());
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
                    JSONArray jsonData = jsonResponse.getJSONArray("data");
                    listCategories = new ArrayList<JCategory>();
                    //Recorremos todos los objetos del josnarray:
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject row = jsonData.getJSONObject(i);

                        int id =row.getInt("id");
                        String category =row.getString("category");

                        JCategory cat = new JCategory(id ,category);
                        Log.e(TAG,"Cat: " + cat);
                        listCategories.add(cat);

                    }
                    totalExpenditures = listCategories.size();


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

    private class categoriesGetAsync extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog mPD;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Creamos el dialogo de espera:
            mPD = new ProgressDialog(NewExpenditureActivity.this);
            mPD.setCancelable(false);
            mPD.setTitle("Conectando...");
            mPD.show();
        }

        @Override
        protected Integer doInBackground(String... parametros) {
            int expend = 0;
            try {
                expend = categories();


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
                    // Initialize the adapter sending the current context
                    // Send the simple_spinner_item layout
                    // And finally send the JCategorys array (Your data)
                    mAdapter = new SpinAdapter(NewExpenditureActivity.this,
                            android.R.layout.simple_spinner_item,
                            listCategories);
                    mySpinner.setAdapter(mAdapter);
                } else {
                    Toast.makeText(NewExpenditureActivity.this,
                            "No he recibido categorias", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        }
    }

    public class SpinAdapter extends ArrayAdapter<JCategory> {

        // Your sent context
        private Context context;
        // Your custom values for the spinner (JCategory)
        private ArrayList<JCategory> values;

        public SpinAdapter(Context context, int textViewResourceId,
                           ArrayList<JCategory> values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.size();
        }

        @Override
        public JCategory getItem(int position){
            return values.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            // Then you can get the current item using the values array (JCategorys array) and the current position
            // You can NOW reference each method you has created in your bean object (JCategory class)
            label.setText(values.get(position).getmCategoria());

            // And finally return your dynamic (or custom) view for each spinner item
            return label;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values.get(position).getmCategoria());

            return label;
        }
    }


    /***** TIPO DE GASTO **********/

    private int  types() {
        int totalExpenditures = 0;
        JSONObject jsonResponse=null;
        try {
            String respuesta = ServerConnection(null,"getTypes/"+mCatSelected.getmId(),"GET",mUser.getToken());
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
                    JSONArray jsonData = jsonResponse.getJSONArray("data");
                    mListaTypes = new ArrayList<JType>();
                    //Recorremos todos los objetos del josnarray:
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject row = jsonData.getJSONObject(i);

                        int id =row.getInt("id");
                        String title =row.getString("type");

                        JType type = new JType(id ,title);
                        Log.e(TAG,"Cat: " + type);
                        mListaTypes.add(type);

                    }
                    totalExpenditures = mListaTypes.size();


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

    private class typesGetAsync extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog mPD;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Creamos el dialogo de espera:
            mPD = new ProgressDialog(NewExpenditureActivity.this);
            mPD.setCancelable(false);
            mPD.setTitle("Conectando...");
            mPD.show();
        }

        @Override
        protected Integer doInBackground(String... parametros) {
            int expend = 0;
            try {
                expend = types();


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
                    mTypeAdapter = new SpinTypeAdapter(NewExpenditureActivity.this,
                            android.R.layout.simple_spinner_item,
                            mListaTypes);
                    mySpinnerTypes.setAdapter(mTypeAdapter);
                } else {
                    Toast.makeText(NewExpenditureActivity.this,
                            "No he recibido categorias", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
        }
    }

    public class SpinTypeAdapter extends ArrayAdapter<JType> {

        // Your sent context
        private Context context;
        // Your custom values for the spinner (JCategory)
        private ArrayList<JType> values;

        public SpinTypeAdapter(Context context, int textViewResourceId,
                           ArrayList<JType> values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.size();
        }

        @Override
        public JType getItem(int position){
            return values.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            // Then you can get the current item using the values array (JCategorys array) and the current position
            // You can NOW reference each method you has created in your bean object (JCategory class)
            label.setText(values.get(position).getType());

            // And finally return your dynamic (or custom) view for each spinner item
            return label;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values.get(position).getType());

            return label;
        }
    }


}
