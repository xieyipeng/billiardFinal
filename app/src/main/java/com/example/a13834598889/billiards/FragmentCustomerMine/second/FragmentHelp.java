package com.example.a13834598889.billiards.FragmentCustomerMine.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a13834598889.billiards.R;

public class FragmentHelp extends Fragment{

    private ImageView back;
    private FragmentManager fragmentManager;

    public static FragmentHelp newInstance(){
        return new FragmentHelp();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shop_help,container,false);
        fragmentManager=getActivity().getSupportFragmentManager();
        initViews(view);
        initClicks();


        return view;
    }

    private void initClicks() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.findFragmentByTag("text_button_bangzhu") != null) {
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("text_button_bangzhu"))
                            .remove(fragmentManager.findFragmentByTag("text_button_bangzhu"))
                            .show(fragmentManager.findFragmentByTag("fragment_mine"))
                            .commit();
                }
            }
        });
    }

    private void initViews(View view) {
        back=view.findViewById(R.id.help_back_ImageView);
    }
}
