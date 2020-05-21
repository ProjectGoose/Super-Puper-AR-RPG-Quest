package com.example.superpuper_ar_rpg.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.superpuper_ar_rpg.AppObjects.User;
import com.example.superpuper_ar_rpg.R;

public class ProfileFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        return(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = view.findViewById(R.id.tv_profile_nickname);
        textView.setText(User.getInstance().getNickname());
        textView = view.findViewById(R.id.tv_profile_level);
        textView.setText(String.valueOf(User.getInstance().getLevel()));
        textView = view.findViewById(R.id.tv_profile_age);
        textView.setText(String.valueOf(User.getInstance().getAge()));
        textView = view.findViewById(R.id.tv_profile_race);
        textView.setText(User.getInstance().getRace());
        ImageView imageView = view.findViewById(R.id.iv_profile_avatar);
        imageView.setImageDrawable(getContext().getDrawable(R.drawable.avatar_ork));
    }
}
