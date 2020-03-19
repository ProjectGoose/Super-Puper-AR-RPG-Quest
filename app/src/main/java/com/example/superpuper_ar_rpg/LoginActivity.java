package com.example.superpuper_ar_rpg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class LoginActivity extends AppCompatActivity {

    Button btn_login, btn_registry;
    EditText et_email, et_password;

    Switch sw_mocker; //debug

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

        sw_mocker = findViewById(R.id.switch_mocker); //debug


        if (getIntent() != null) {
            et_email.setText(getIntent().getStringExtra(Intent.EXTRA_EMAIL));
        }

        final Context context = this;
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO

                Intent goToMap = new Intent(context, MapsActivity.class);
                goToMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goToMap);
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

        //debug
        sw_mocker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    MapHandler.isMocking = true;
                else
                    MapHandler.isMocking = false;
            }
        });
    }
}
