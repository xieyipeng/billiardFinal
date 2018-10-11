package com.example.a13834598889.billiards.FragmentShopKeeperNo3;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.a13834598889.billiards.JavaBean.Order;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.content.ContentValues.TAG;

public class FragmentShopKeeperNo3 extends Fragment {

    private LinearLayout noThings;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<Order> orderList = new ArrayList<>();

    //TODO: shop_fragment_bill
    public static FragmentShopKeeperNo3 newInstance() {
        return new FragmentShopKeeperNo3();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_keeper_no3, container, false);
        initViews(view);
        initData();
        initClick();
        return view;
    }


    private void initClick() {
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
        orderList.clear();
        BmobQuery<Order> orderBmobQuery = new BmobQuery<>();
        orderBmobQuery.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e == null) {
                    for (Order order : list) {
                        if (order.getStoreId().equals(User.getCurrentUser().getObjectId()) && order.getStatus().equals("已处理")) {
                            orderList.add(order);
                        }
                    }
                    if (orderList.size() != 0) {
                        noThings.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        loadingRecycleView();
                    } else {
                        noThings.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });

    }

    private void loadingRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OrderAdapter1 orderAdapter1 = new OrderAdapter1(R.layout.item_bill, orderList, getContext());
        orderAdapter1.openLoadAnimation();
        orderAdapter1.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        orderAdapter1.isFirstOnly(false);

        recyclerView.setAdapter(orderAdapter1);
    }

    private void initViews(View view) {
        noThings = view.findViewById(R.id.order_no_things);
        refreshLayout = view.findViewById(R.id.order_refresh);
        recyclerView = view.findViewById(R.id.order_recycle_view);
    }

    private class OrderAdapter1 extends BaseQuickAdapter<Order, BaseViewHolder> {

        private List<Order> orderList = new ArrayList<>();
        private Context context;

        public OrderAdapter1(int layoutResId, @Nullable List<Order> data, Context context) {
            super(layoutResId, data);
            this.context = context;
            orderList = data;
        }

        @Override
        protected void convert(final BaseViewHolder helper, final Order order) {
            BmobQuery<User> orderBmobQuery = new BmobQuery<>();
            orderBmobQuery.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getObjectId().equals(order.getStoreId())) {
                                helper.setText(R.id.order_item_name, list.get(i).getNickName() + " 球桌厅");
                            }
                        }
                    } else {
                        Log.e(TAG, "done: " + e.getMessage());
                    }
                }
            });

            helper.setText(R.id.order_item_time, order.getCreatedAt());
            helper.setText(R.id.order_item_table_num, String.valueOf(order.getTable_number()));

            String[] string1 = String.valueOf(order.getCreatedAt()).split(" ");
            String[] string2 = String.valueOf(order.getUpdatedAt()).split(" ");

            helper.setText(R.id.order_item_all_time, string1[1] + " - " + string2[1]);
            helper.setText(R.id.order_item_order_money, String.valueOf(order.getMoney_order()) + "元");
            if (order.getMoneyGoods() == null) {
                helper.setText(R.id.order_item_goods_money, "0元");
                helper.setText(R.id.order_item_all_money, "总计：" + String.valueOf(order.getMoney_order()) + "元");
            } else {
                helper.setText(R.id.order_item_goods_money, String.valueOf(order.getMoneyGoods()) + "元");
                Double a = order.getMoneyGoods() + order.getMoney_order();
                helper.setText(R.id.order_item_all_money, "总计：" + String.valueOf(a) + "元");
            }
        }
    }

//    private class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {
//        private List<Order> orders;
//
//        OrderAdapter(List<Order> list) {
//            orders = list;
//        }
//
//        @Override
//        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
//            return new OrderHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(OrderHolder holder, int position) {
//            Order order = orders.get(position);
//            holder.bindView(order);
//        }
//
//        @Override
//        public int getItemCount() {
//            return orders.size();
//        }
//    }
//
//    private class OrderHolder extends RecyclerView.ViewHolder {
//
//        private TextView shopName;
//        private TextView startTime;
//        private TextView tableNum;
//        private TextView allTime;
//        private TextView orderMoney;
//        private TextView goodsMoney;
//        private TextView allMoney;
//
//        public OrderHolder(View itemView) {
//            super(itemView);
//            shopName = itemView.findViewById(R.id.order_item_name);
//            startTime = itemView.findViewById(R.id.order_item_time);
//            tableNum = itemView.findViewById(R.id.order_item_table_num);
//            allTime = itemView.findViewById(R.id.order_item_all_time);
//            orderMoney = itemView.findViewById(R.id.order_item_order_money);
//            goodsMoney = itemView.findViewById(R.id.order_item_goods_money);
//            allMoney = itemView.findViewById(R.id.order_item_all_money);
//        }
//
//        public void bindView(final Order order) {
//            BmobQuery<User> orderBmobQuery = new BmobQuery<>();
//            orderBmobQuery.findObjects(new FindListener<User>() {
//                @Override
//                public void done(List<User> list, BmobException e) {
//                    if (e == null) {
//                        for (int i = 0; i < list.size(); i++) {
//                            if (list.get(i).getObjectId().equals(order.getStoreId())) {
//                                shopName.setText(list.get(i).getNickName() + " 球桌厅");
//                            }
//                        }
//                    } else {
//                        Log.e(TAG, "done: " + e.getMessage());
//                    }
//                }
//            });
//            startTime.setText(order.getCreatedAt());
//            tableNum.setText(String.valueOf(order.getTable_number()));
//
//            String[] string1 = String.valueOf(order.getCreatedAt()).split(" ");
//            String[] string2 = String.valueOf(order.getUpdatedAt()).split(" ");
//
//            allTime.setText(string1[1] + " - " + string2[1]);
//            orderMoney.setText(String.valueOf(order.getMoney_order()) + "元");
//            if (order.getMoneyGoods() == null) {
//                goodsMoney.setText( "0元");
//                allMoney.setText("总计：" + String.valueOf(order.getMoney_order()) + "元");
//            } else {
//                goodsMoney.setText(String.valueOf(order.getMoneyGoods()) + "元");
//                Log.e(TAG, "bindView: " + order.getMoneyGoods() + "   " + order.getMoney_order());
//                Double a = order.getMoneyGoods() + order.getMoney_order();
//                allMoney.setText("总计：" + String.valueOf(a) + "元");
//            }
//        }
//    }
}
