package com.example.a13834598889.billiards.FragmentShopKeeperNo1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.a13834598889.billiards.R;

/**
 * Created by xieyipeng on 2018/10/4.
 */

public class Fragment_income extends Fragment {


    private WebView webview1;
    private WebView webview2;

    public static Fragment_income newInstance(){
        Fragment_income fragment_income = new Fragment_income();
        return fragment_income;
    }


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.income, container, false);


        webview1 = (WebView)view.findViewById(R.id.webView1);
        webview2 = (WebView)view.findViewById(R.id.webView2);


        webview1.getSettings().setDefaultTextEncodingName("utf-8");
        webview1.getSettings().setJavaScriptEnabled(true);
        webview1.getSettings().setUseWideViewPort(true);
        webview1.getSettings().setLoadWithOverviewMode(true);
        webview1.loadUrl("file:///android_asset/test.html");

        webview2.getSettings().setDefaultTextEncodingName("utf-8");
        webview2.getSettings().setJavaScriptEnabled(true);
        webview2.getSettings().setUseWideViewPort(true);
        webview2.getSettings().setLoadWithOverviewMode(true);
        webview2.loadUrl("file:///android_asset/test4.html");


        return view;
    }

}
