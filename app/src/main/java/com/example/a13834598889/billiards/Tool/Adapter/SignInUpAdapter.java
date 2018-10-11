package com.example.a13834598889.billiards.Tool.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.a13834598889.billiards.Fragment_login;
import com.example.a13834598889.billiards.Fragment_register;

public class SignInUpAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Sign In","Sign Up"};
    private Fragment[] fragments;

    public SignInUpAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[TITLES.length];
        fragments[0] = Fragment_login.newInstance();
        fragments[1] = Fragment_register.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = Fragment_login.newInstance();
                    break;
                case 1:
                    fragments[position] = Fragment_register.newInstance();
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
        //获取TITLES
        return TITLES[position];
    }
}
