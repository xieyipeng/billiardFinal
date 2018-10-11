package com.example.a13834598889.billiards.Tool.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.a13834598889.billiards.FragmentCustomerShare.Fragment_share1;
import com.example.a13834598889.billiards.FragmentCustomerShare.Fragment_share2;

/**
 * Created by Administrator on 2018/10/7.
 */

public class CardAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"专家指导","生活分享"};
    private Fragment[] fragments;

    public CardAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[TITLES.length];
        fragments[0] = Fragment_share2.newInstance();
        fragments[1] = Fragment_share1.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = Fragment_share2.newInstance();
                    break;
                case 1:
                    fragments[position] = Fragment_share1.newInstance();
                    break;
                default:
                    break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}