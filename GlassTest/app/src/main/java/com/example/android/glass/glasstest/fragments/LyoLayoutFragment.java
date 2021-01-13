package com.example.android.glass.glasstest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.glass.glasstest.R;
import com.squareup.picasso.Picasso;

public class LyoLayoutFragment extends BaseFragment{

    public static LyoLayoutFragment newInstance(int id, String step, String type, String title, String url, String desc) {

        final LyoLayoutFragment fragment =new LyoLayoutFragment();

        final Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("step", step);
        args.putString("type", type);
        args.putString("title", title);
        args.putString("url", url);
        args.putString("desc", desc);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.list_final, container, false);
        if (getArguments() != null){
            final TextView tvStep = view.findViewById(R.id.list_final_tvStep);
            final TextView tvTitle = view.findViewById(R.id.list_final_tvTitle);
            final ImageView ivIcon = view.findViewById(R.id.list_final_ivIcon);
            final TextView tvDesc = view.findViewById(R.id.list_final_tvDesc);
            final ImageView imageView = view.findViewById(R.id.list_final_imageview);

            tvStep.setText(getArguments().getString("step", ""));
            tvTitle.setText(getArguments().getString("title", ""));
            tvDesc.setText(getArguments().getString("desc", ""));
            Picasso.get().load("https:"+getArguments().getString("url")).into(imageView);

            String type = getArguments().getString("type");
            switch (type){
                case "normal":
                    ivIcon.setImageResource(R.drawable.ic_action);
                    break;

                case "critical":
                    ivIcon.setImageResource(R.drawable.ic_warning);
                    break;

                case "camera":
                    ivIcon.setImageResource(R.drawable.ic_camera);
                    break;

                case "info":
                    ivIcon.setImageResource(R.drawable.ic_info);
                    break;
            }
        }

        return view;
    }
}
