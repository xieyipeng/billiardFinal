package com.example.a13834598889.billiards.FragmentShopKeeperNo1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.Adapter.CardPagerAdapter;
import com.example.a13834598889.billiards.Tool.Adapter.ViewPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

/**
 * Created by xieyipeng on 2018/10/4.
 */

public class Fragment_No1 extends Fragment {

    public static Fragment_No1 newInstance() {
        return new Fragment_No1();
    }

    private SlidingTabLayout slidingTabLayout;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_no1, container, false);
        slidingTabLayout = view.findViewById(R.id.sliding_no1);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        slidingTabLayout.setViewPager(viewPager);
        return view;
    }
}
