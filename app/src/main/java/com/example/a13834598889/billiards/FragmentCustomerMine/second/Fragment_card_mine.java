package com.example.a13834598889.billiards.FragmentCustomerMine.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.a13834598889.billiards.JavaBean.Card;
import com.example.a13834598889.billiards.JavaBean.Customer;
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
 * Created by 13834598889 on 2018/5/4.
 */

public class Fragment_card_mine extends Fragment {

    private RecyclerView recyclerView_share;
    private RelativeLayout relativeLayout_no_card;
    private List<Card> cards = new ArrayList<>();

    public static Fragment_card_mine newInstance() {
        Fragment_card_mine fragment_card_mine = new Fragment_card_mine();
        return fragment_card_mine;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_mine, container, false);
        initViews(view);
        initData();
        return view;
    }

    private void initData() {
        final Customer customer = Customer.getCurrentUser(Customer.class);
        BmobQuery<Card> cardBmobQuery = new BmobQuery<>();
        cardBmobQuery.findObjects(new FindListener<Card>() {
            @Override
            public void done(List<Card> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getCustomer().getObjectId().equals(customer.getObjectId())) {
                            cards.add(list.get(i));
                        }
                    }
                    setRecycleView();
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    private void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        recyclerView_share.setLayoutManager(layoutManager);
        recyclerView_share.addItemDecoration(new SpaceItem(30));

        if (cards.size() == 0) {
            relativeLayout_no_card.setVisibility(View.VISIBLE);
            recyclerView_share.setVisibility(View.GONE);
        } else {
            relativeLayout_no_card.setVisibility(View.GONE);
            recyclerView_share.setVisibility(View.VISIBLE);
            ShareAdapter shareAdapter = new ShareAdapter(R.layout.share_item, cards, getContext());

            shareAdapter.openLoadAnimation();
            //TODO: 设置滑动动画
            shareAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            shareAdapter.isFirstOnly(false);
            recyclerView_share.setAdapter(shareAdapter);
        }
    }

    private void initViews(View view) {
        recyclerView_share = view.findViewById(R.id.customer_mine_card_RecycleView);
        relativeLayout_no_card = view.findViewById(R.id.customer_share_no_card_Layout);
    }
}
