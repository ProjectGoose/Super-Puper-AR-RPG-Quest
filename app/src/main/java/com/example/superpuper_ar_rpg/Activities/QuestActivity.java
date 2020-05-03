package com.example.superpuper_ar_rpg.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.superpuper_ar_rpg.R;

public class QuestActivity extends AppCompatActivity {

    private TextView tv_title, tv_rating, tv_description;
    private Button bt_accept;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);
        tv_title = findViewById(R.id.tv_title);
        tv_rating = findViewById(R.id.tv_rating);
        tv_description = findViewById(R.id.tv_description);

        tv_title.setText(getIntent().getExtras().getString("title"));
        tv_rating.setText(getIntent().getExtras().getString("rating"));
        tv_description.setText(getIntent().getExtras().getString("description"));

    }


}
