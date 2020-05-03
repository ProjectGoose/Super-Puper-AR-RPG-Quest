package com.example.superpuper_ar_rpg.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.superpuper_ar_rpg.AppObjects.User;

import com.example.superpuper_ar_rpg.Network.NetworkService;
import com.example.superpuper_ar_rpg.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    Button btn_login, btn_registry;
    EditText et_email, et_password;
    ProgressBar pb_login;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_registry = findViewById(R.id.btn_register);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        pb_login = findViewById(R.id.progressBarLogin);


        NetworkService.getInstance(); //инициализируем networkservice

        if (getIntent() != null) {
            et_email.setText(getIntent().getStringExtra(Intent.EXTRA_EMAIL));
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMap = new Intent(context, MapsActivity.class);
                goToMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                pb_login.setVisibility(View.VISIBLE);

                //Вызываем метод для авторизации, передавая в него необходимый нам callback
                NetworkService.getInstance().loginTest(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String responseString = response.body();
                            Log.d("NETWORK", "Response code is " + response.body() );
                            if(!responseString.equals("null")) {
                                Log.d("NETWORK", "OK");
                                try {
                                    User.firstSetInstance(response.body(), et_email.getText().toString() , 1);
                                } catch (NullPointerException e){
                                    Log.d("NETWORK", "Exception: " + e.getMessage());
                                }
                                startActivity(goToMap);
                                finish();
                            } else {
                                Toast.makeText(context, R.string.authWrongPassword, Toast.LENGTH_SHORT).show();
                                pb_login.setVisibility(View.GONE);
                                Log.d("NETWORK_AUTH", "Wrong login or password");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("NETWORK", "Error: " + t.getMessage());
                        pb_login.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.authFail, Toast.LENGTH_SHORT).show();
                    }
                }, et_email.getText().toString(), et_password.getText().toString());
            }
        });

        btn_registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegistry = new Intent(context, RegistryActivity.class);
                goToRegistry.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goToRegistry);
            }
        });
    }


}
