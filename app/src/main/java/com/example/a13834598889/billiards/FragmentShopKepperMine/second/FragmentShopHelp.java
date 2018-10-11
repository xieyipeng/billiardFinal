package com.example.a13834598889.billiards.FragmentShopKepperMine.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a13834598889.billiards.R;

import static cn.volley.VolleyLog.TAG;

public class FragmentShopHelp extends Fragment{

    private ImageView helpBackImageView;
    private Fragment fragmentTest;
    private FragmentManager fragmentManager;

    public static FragmentShopHelp newInstance(){
        return new FragmentShopHelp();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_help, container, false);
        initViews(view);
        initClicks();
        return view;
    }

    private void initClicks() {
        helpBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTest = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .remove(fragmentTest)
                        .show(fragmentManager.findFragmentByTag("shop_fragment_mine"))
                        .commit();
            }
        });
    }
    private void initViews(View view) {
        helpBackImageView=view.findViewById(R.id.help_back_ImageView);
    }
}


