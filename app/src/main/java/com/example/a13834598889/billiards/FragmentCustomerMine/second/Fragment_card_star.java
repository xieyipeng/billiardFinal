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

public class Fragment_card_star extends Fragment {

    private RelativeLayout noShoucang;
    private RecyclerView recyclerView;
    private List<Card> cards=new ArrayList<>();

    public static Fragment_card_star newInstance(){
        Fragment_card_star fragment_card_star=new Fragment_card_star();
        return fragment_card_star;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_star,container,false);
        initViews(view);
        initData();
        return view;
    }

    private void initData() {
        BmobQuery<Card> cardBmobQuery=new BmobQuery<>();
        cardBmobQuery.findObjects(new FindListener<Card>() {
            @Override
            public void done(List<Card> list, BmobException e) {
                if (e==null){
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list.get(i).getShoucang().size(); j++) {
                            if (list.get(i).getShoucang().contains(Customer.getCurrentUser().getObjectId())){
                                cards.add(list.get(i));
                            }
                        }
                    }
                    setRecycleView();
                }else {
                    Log.e(TAG, "done: "+e.getMessage() );
                }
            }
        });
    }

    private void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        recyclerView.setLayoutManager(layoutManager);

        if (cards.size()==0){
            noShoucang.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            noShoucang.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            ShareAdapter shareAdapter = new ShareAdapter(R.layout.share_item, cards, getContext());
            shareAdapter.openLoadAnimation();
            //TODO: 设置滑动动画
            shareAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            shareAdapter.isFirstOnly(false);
            recyclerView.setAdapter(shareAdapter);
        }
    }

    private void initViews(View view) {
        noShoucang=view.findViewById(R.id.customer_star_no_card_Layout);
        recyclerView=view.findViewById(R.id.customer_star_card_RecycleView);
        recyclerView.addItemDecoration(new SpaceItem(30));
    }
}
