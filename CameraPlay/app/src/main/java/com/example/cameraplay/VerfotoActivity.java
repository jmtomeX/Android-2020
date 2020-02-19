package com.example.cameraplay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class VerfotoActivity extends AppCompatActivity {
    private String mPath;
    private ArrayList<JFoto> mListaFotos;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verfoto_layout);
        ImageView ivFoto = findViewById(R.id.ivFoto);

        Bundle extras = getIntent().getExtras();
        mPath = extras.getString("path");
        //String image_name = extras.getString("image_name");
        //Log.e("VerFotoActivity","Foto: "+image_name);
        Log.e("VerFotoActivity","PAth: "+mPath);

        getImageFiles();

        mListView = findViewById(R.id.listFiles);
        FotoAdapter fotosAdapter = new FotoAdapter(VerfotoActivity.this, R.layout.verfoto_row_layout, mListaFotos);
        mListView.setAdapter(fotosAdapter);


        final ImageView btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*
        ArrayList<String> filepath= new ArrayList<String>();
        //list for storing all file paths



        File file = new File(path);
        ivFoto.setImageDrawable(Drawable.createFromPath(file.toString()));
        */

    }
    //TODO: Crear un listview y cargar todas las fotos con nombre:, y cuando se haga click en una mostrar a pantalla compleata
    private void getImageFiles() {
        File file = new File(mPath);
        File[] files = file.listFiles();
        Log.e("Files", "Size: "+ files.length);
        mListaFotos = new ArrayList<>();
        for (int i = 0; i < files.length; i++)
        {
            Log.e("Files", "FileName:" + files[i].getName());
            mListaFotos.add(new JFoto(i+1,files[i].getName()));
        }
    }

    public class FotoAdapter extends ArrayAdapter<JFoto> {

        private Context mContext;

        public FotoAdapter(Context context, int resource, ArrayList<JFoto> fotos) {
            super(context, resource, fotos);
            mContext = context;
        }


        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            FileRowViewHolder uvh;

            if (convertView == null) {
                // Only inflate the layout if there's not already a recycled view
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                convertView = mInflater.inflate(R.layout.verfoto_row_layout, parent, false);

                // Tag the view with a class holding the views found with
                // findViewById() to prevent lookups later
                uvh = new FileRowViewHolder(convertView);
                convertView.setTag(uvh);
            } else {
                // If the view is non-null, then you will have already
                // created the view holder and set it as a tag
                uvh = (FileRowViewHolder) convertView.getTag();
            }

            // Now just get the user at the specified position and
            // set up the view as necessary
            JFoto foto = getItem(pos);
            String path = String.format("%s/%s",mPath,foto.getFileName());
            File file = new File(path);
            uvh.ivFoto.setImageDrawable(Drawable.createFromPath(file.toString()));
            uvh.tvFileName.setText(foto.getFileName());
            //uvh.tvFileDesc.setText(termino.getDesc());

            return convertView;
        }
// conexion con el layout row
        public class FileRowViewHolder {
            public final TextView tvFileName;
            public final TextView tvFileDesc;
            public final ImageView ivFoto;

            public FileRowViewHolder(View v) {
                tvFileName = v.findViewById(R.id.tvRowFileName);
                tvFileDesc = v.findViewById(R.id.tvRowFileDesc);
                ivFoto = v.findViewById(R.id.ivRowFoto);
            }
        }
    }

}
