package com.example.a13834598889.billiards.FragmentCustomerMine.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a13834598889.billiards.FragmentCustomerMine.thired.Fragment_card_add;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.Adapter.CardPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13834598889 on 2018/5/3.
 */

public class Fragment_card extends Fragment {

    private ImageView imageView_back;
    private SlidingTabLayout slidingTabLayout;
    private View view;
    private FragmentManager fragmentManager;


    public static Fragment_card newInstance() {
        Fragment_card fragment_card = new Fragment_card();
        return fragment_card;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews();
        initClicks();
        return view;
    }

    private void initClicks() {
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.findFragmentByTag("card_fragment") != null) {
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("card_fragment"))
                            .remove(fragmentManager.findFragmentByTag("card_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_mine"))
                            .commit();
                }
            }
        });

    }

    public void initViews() {
        ViewPager viewPager = view.findViewById(R.id.viewPager_container3);
        slidingTabLayout = view.findViewById(R.id.sliding_tabs);
        imageView_back = view.findViewById(R.id.fragment_card_back);

        CardPagerAdapter adapter = new CardPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        slidingTabLayout.setViewPager(viewPager);
    }
}
