package com.example.cameraplay;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class VerfotoActivity extends AppCompatActivity {
    private String mPath;
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
        for (int i = 0; i < files.length; i++)
        {
            Log.e("Files", "FileName:" + files[i].getName());
        }
    }
}
