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

public class Fragment_keliu extends Fragment {

    private WebView webview1;

    public static Fragment_keliu newInstance(){
        Fragment_keliu fragment_keliu = new Fragment_keliu();
        return fragment_keliu;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.keliu, container, false);


        webview1 = (WebView)view.findViewById(R.id.webView5);


        webview1.getSettings().setDefaultTextEncodingName("utf-8");
        webview1.getSettings().setJavaScriptEnabled(true);
        webview1.getSettings().setUseWideViewPort(true);
        webview1.getSettings().setLoadWithOverviewMode(true);
        webview1.loadUrl("file:///android_asset/test5.html");


        return view;
    }

}
