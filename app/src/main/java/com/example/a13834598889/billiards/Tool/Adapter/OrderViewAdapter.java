package com.example.a13834598889.billiards.Tool.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.a13834598889.billiards.FragmentCustomerOrder.Fragment_map;
import com.example.a13834598889.billiards.FragmentCustomerOrder.Fragment_order2;
import com.example.a13834598889.billiards.FragmentShopKeeperNo1.Fragment_income;
import com.example.a13834598889.billiards.FragmentShopKeeperNo1.Fragment_keliu;
import com.example.a13834598889.billiards.FragmentShopKeeperNo1.Fragment_qiuzhuo_dianzhu;
import com.example.a13834598889.billiards.FragmentShopKeeperNo1.Fragment_sales;

/**
 * Created by Administrator on 2018/10/8.
 */

public class OrderViewAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"店铺位置","数据"};
    private Fragment[] fragments;

    public OrderViewAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[TITLES.length];
        fragments[0] = Fragment_map.newInstance();
        fragments[1] = Fragment_order2.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] =  Fragment_map.newInstance();
                    break;
                case 1:
                    fragments[position] = Fragment_order2.newInstance();
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

