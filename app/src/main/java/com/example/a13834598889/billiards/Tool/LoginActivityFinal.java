package com.example.a13834598889.billiards.Tool;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.Adapter.CardPagerAdapter;
import com.example.a13834598889.billiards.Tool.Adapter.SignInUpAdapter;
import com.flyco.tablayout.SlidingTabLayout;

public class LoginActivityFinal extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_in_up);
    }

    public void initViews() {
        ViewPager viewPager =(ViewPager)findViewById(R.id.viewPager_container_kaikai);
        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);


        SignInUpAdapter adapter = new SignInUpAdapter(LoginActivityFinal.this.getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        slidingTabLayout.setViewPager(viewPager);
    }


}
