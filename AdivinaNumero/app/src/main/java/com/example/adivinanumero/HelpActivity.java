package com.example.adivinanumero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    private ArrayList<JTermino> mListaTerminos;
    private ListView mListView;
    ImageView btnReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        btnReturn=findViewById(R.id.btnReturn);

        //Cargar el arraylist con los terminos:
        String[] listItem = getResources().getStringArray(R.array.palabras_array);
        mListaTerminos = new ArrayList();
        for (int i=0;i<listItem.length;i++) {
            mListaTerminos.add(new JTermino(i+1,listItem[i],"Lorem Ipsum description "+i));
        }
        //Definir el ListView y cargarlo con los elementos del ArrayList
        mListView = findViewById(R.id.listHelp);
        TerminosAdapter terminosAdapter = new TerminosAdapter(HelpActivity.this, R.layout.help_row_layout, mListaTerminos);
        mListView.setAdapter(terminosAdapter);


        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public class TerminosAdapter extends ArrayAdapter<JTermino> {

        private Context mContext;

        public TerminosAdapter(Context context, int resource, ArrayList<JTermino> terminos) {
            super(context, resource, terminos);
            mContext = context;
        }


        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            TerminoViewHolder uvh;

            if (convertView == null) {
                // Only inflate the layout if there's not already a recycled view
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                convertView = mInflater.inflate(R.layout.help_row_layout, parent, false);

                // Tag the view with a class holding the views found with
                // findViewById() to prevent lookups later
                uvh = new TerminoViewHolder(convertView);
                convertView.setTag(uvh);
            } else {
                // If the view is non-null, then you will have already
                // created the view holder and set it as a tag
                uvh = (TerminoViewHolder) convertView.getTag();
            }

            // Now just get the user at the specified position and
            // set up the view as necessary
            JTermino termino = getItem(pos);
            uvh.tvPal.setText(termino.getPalabra());
            uvh.tvDesc.setText(termino.getDesc());

            return convertView;
        }

        public class TerminoViewHolder {
            public final TextView tvPal;
            public final TextView tvDesc;

            public TerminoViewHolder(View v) {
                tvPal = v.findViewById(R.id.tvRowPalabra);
                tvDesc = v.findViewById(R.id.tvRowDescription);
            }
        }
    }
}
