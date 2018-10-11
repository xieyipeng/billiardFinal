package com.example.a13834598889.billiards.FragmentCustomerShare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.a13834598889.billiards.FragmentCustomerMine.thired.Fragment_card_add;
import com.example.a13834598889.billiards.JavaBean.Card;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.Adapter.ShareAdapter;
import com.example.a13834598889.billiards.Tool.SpaceItem;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.volley.VolleyLog.TAG;

/**
 * Created by Administrator on 2018/10/7.
 */

public class Fragment_share1 extends Fragment {

    private FragmentManager fragmentManager;
    private RecyclerView recyclerView_shares;
    private SwipeRefreshLayout refreshLayout;
    private List<Card> cards = new ArrayList<>();


    public static Fragment_share1 newInstance() {
        return new Fragment_share1();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        initViews(view);
        initClicks();
        initData();
        return view;
    }

    private void initClicks() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initData() {
        cards.clear();
        BmobQuery<Card> cardBmobQuery = new BmobQuery<>();
        cardBmobQuery.setLimit(20);
        cardBmobQuery.findObjects(new FindListener<Card>() {
            @Override
            public void done(List<Card> list, BmobException e) {
                if (e == null) {
                    cards.addAll(list);
                    if (cards.size() != 0) {
                        setAdapter();
                    } else {

                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        recyclerView_shares.setLayoutManager(layoutManager);
        ((SimpleItemAnimator) recyclerView_shares.getItemAnimator()).setSupportsChangeAnimations(false);

        ShareAdapter shareAdapter = new ShareAdapter(R.layout.share_item, cards, getContext());
        shareAdapter.openLoadAnimation();
        //TODO: item点击事件
        shareAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (cards.get(position).getPicture() != null) {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .hide(fragmentManager.findFragmentByTag("fragment_share"))
                            .add(R.id.fragment_container, Fragment_share_message.newInstance(cards.get(position).getCustomer(),
                                    cards.get(position).getPicture().getFileUrl(),
                                    cards.get(position).getText()), "fragment_share_message")
                            .commit();
                }
            }
        });
        //TODO: 设置滑动动画
        shareAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        shareAdapter.isFirstOnly(false);


        recyclerView_shares.setAdapter(shareAdapter);
    }

    private void initViews(View view) {

        fragmentManager = getActivity().getSupportFragmentManager();
        recyclerView_shares = view.findViewById(R.id.recycler_View_shares);
        refreshLayout = view.findViewById(R.id.customer_share1_item_refresh);
        recyclerView_shares.addItemDecoration(new SpaceItem(30));
    }
}
