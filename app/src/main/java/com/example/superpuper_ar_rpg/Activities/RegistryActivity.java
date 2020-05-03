package com.example.superpuper_ar_rpg.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.superpuper_ar_rpg.R;
import com.example.superpuper_ar_rpg.Activities.LoginActivity;

public class RegistryActivity extends AppCompatActivity {

    Button btn_registry, btn_goback;
    EditText et_email, et_password, et_password_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registry);

        btn_registry = findViewById(R.id.btn_register);
        btn_goback = findViewById(R.id.btn_goback);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_password_confirm = findViewById(R.id.et_password_again);

        final Context context = this;
        btn_registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_password.getText().toString().equals(et_password_confirm.getText().toString())) {
                    //TODO
                    //Registration processing

                    Intent goToLogin = new Intent(context, LoginActivity.class);
                    goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    goToLogin.putExtra(Intent.EXTRA_EMAIL, et_email.getText().toString());
                    startActivity(goToLogin);
                } else {
                    Toast.makeText(context, R.string.form_error_passwords, Toast.LENGTH_SHORT);
                }
            }
        });

        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToLogin = new Intent(context, LoginActivity.class);
                goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goToLogin);
                finish();
            }
        });
    }
}
