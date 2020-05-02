package com.example.superpuper_ar_rpg.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.superpuper_ar_rpg.R;

public class QuestInfoFragment extends Fragment {
    private TextView tv_title;
    private View view;
    private String title;
    public QuestInfoFragment(String title){
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_questinfo, container, false);
        tv_title = view.findViewById(R.id.tv_questInfo);
        tv_title.setText(title);
        return(view);
    }


}
