package com.example.a13834598889.billiards.FragmentCustomerOrder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a13834598889.billiards.LoginActivity;
import com.example.a13834598889.billiards.R;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2018/10/8.
 */

public class Fragment_order2 extends Fragment {

    public static Fragment_order2 newInstance(){
        Fragment_order2 fragment_order2 = new Fragment_order2();
        return fragment_order2;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teach2, container, false);

        return view;
    }
}
