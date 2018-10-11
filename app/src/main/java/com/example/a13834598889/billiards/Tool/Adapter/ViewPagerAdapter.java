package com.example.a13834598889.billiards.Tool.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.a13834598889.billiards.FragmentShopKeeperNo1.Fragment_income;
import com.example.a13834598889.billiards.FragmentShopKeeperNo1.Fragment_keliu;
import com.example.a13834598889.billiards.FragmentShopKeeperNo1.Fragment_qiuzhuo_dianzhu;
import com.example.a13834598889.billiards.FragmentShopKeeperNo1.Fragment_sales;

/**
 * Created by xieyipeng on 2018/10/4.
 */

public class ViewPagerAdapter  extends FragmentPagerAdapter {
    private final String[] TITLES = {"球桌","收入", "销量", "汇总"};
    private Fragment[] fragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[TITLES.length];
        fragments[0] = Fragment_qiuzhuo_dianzhu.newInstance();
        fragments[1] = Fragment_income.newInstance();
        fragments[2] = Fragment_sales.newInstance();
        fragments[3] = Fragment_keliu.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] =  Fragment_qiuzhuo_dianzhu.newInstance();
                    break;
                case 1:
                    fragments[position] = Fragment_income.newInstance();
                    break;
                case 2:
                    fragments[position] = Fragment_sales.newInstance();
                    break;
                case 3:
                    fragments[position] = Fragment_keliu.newInstance();
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
