package com.example.api_gastos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class LoginRetrofitActivity extends AppCompatActivity {
    private EditText mEdTxtEmail;
    private EditText mEdtxtpassword;
    private Button mbtnLogin;

    //Implementación de retrofit:
    final String URL_BASE = "https://pokeapi.co/api/v2/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mEdTxtEmail = findViewById(R.id.boxTxtEmail);
        mEdtxtpassword = findViewById(R.id.boxTxtPassword);
        mbtnLogin = findViewById(R.id.btnLogin);

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String email_admin = "iremti2@gmail.com";
        final String passw_admin = "11111111";

        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEdTxtEmail.getText().toString();
                String password = mEdtxtpassword.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Inserta una dirección de correo", Toast.LENGTH_SHORT).show();
                } else {
                    if (email.trim().matches(emailPattern)) {
                        Log.e("LoginActivity", "Email: " + email + " Comntaseña: " + password);
                    } else {
                        Toast.makeText(getApplicationContext(), "Inserta una dirección de correo válida", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PokemonAPI pokemonAPI = retrofit.create(PokemonAPI.class);

        Call<PokemonByIdResponse> call = pokemonAPI.getPokemonById("ditto");
        call.enqueue(new Callback<PokemonByIdResponse>() {
            @Override
            public void onResponse(Call<PokemonByIdResponse> call, Response<PokemonByIdResponse> response) {
                if(response.isSuccessful()){
                    PokemonByIdResponse pokemon = response.body();
                    Log.e("Pokemon","info: "+pokemon.name);
                } else {
                    Log.e("error", "Hubo un error inesperado!");
                }
            }

            @Override
            public void onFailure(Call<PokemonByIdResponse> call, Throwable t) {
            }
        });

    }

    public interface PokemonAPI {
        @GET("pokemon/{id}")
        Call<PokemonByIdResponse> getPokemonById(@Path("id") String id);
    }

    public class PokemonByIdResponse {
        @SerializedName("base_experience")
        private int baseExperience;
        private String name;
        private int id;

        public int getBaseExperience() {
            return baseExperience;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}
