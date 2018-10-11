package com.example.a13834598889.billiards.FragmentCustomerMine.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a13834598889.billiards.JavaBean.Customer;
import com.example.a13834598889.billiards.JavaBean.Order;
import com.example.a13834598889.billiards.JavaBean.Table;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.volley.VolleyLog.TAG;

/**
 * Created by Administrator on 2018/9/24.
 */

public class Fragment_yuding extends Fragment {

    private RecyclerView recyclerView_yuding;
    private List<Order> cards = new ArrayList<>();
    private FragmentManager fragmentManager;
    private RelativeLayout back;
    private LinearLayout noThing;
    private SwipeRefreshLayout refreshLayout;
    private YudingAdapter adapter;

    //TODO: text_button_wodeyuding
    public static Fragment_yuding newInstance() {
        return new Fragment_yuding();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yuding, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view);
        initClicks();
//        addData();
        loadingViews();
        return view;
    }

    private void addData() {
        Order order=new Order();
        order.setMoneyGoods(2.0);
        order.setStatus("1");
        order.setMoney_order(13.0);
        order.setTable_number(4);
        order.setTime(String .valueOf(new Date()));
        order.setUserID("b19690a4df");
        order.setStoreId("4d5966c844");
        order.setTime_order(String .valueOf(new Date()));
        order.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Toast.makeText(getContext(), "添加数据成功", Toast.LENGTH_SHORT).show();
                }else {
                    Log.e(TAG, "done: "+e.getMessage() );
                }
            }
        });
    }

    private void loadingViews() {
        cards.clear();
        BmobQuery<Order> card = new BmobQuery<>();
        card.setLimit(20);
        card.addWhereEqualTo("userID", Customer.getCurrentUser().getObjectId());
        card.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e == null) {
                    if (list.size()==0){
                        noThing.setVisibility(View.VISIBLE);
                        recyclerView_yuding.setVisibility(View.INVISIBLE);
                    }else {
                        noThing.setVisibility(View.INVISIBLE);
                        recyclerView_yuding.setVisibility(View.VISIBLE);
                        cards.addAll(list);
                        setAdapter();
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    private void initViews(View view) {
        recyclerView_yuding = view.findViewById(R.id.recycler_view_yuding);
        back = view.findViewById(R.id.back_yuding);
        refreshLayout = view.findViewById(R.id.yuding_refresh);
        noThing = view.findViewById(R.id.customer_order_no_things);

    }

    private void initClicks() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingViews();
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.findFragmentByTag("text_button_wodeyuding") != null) {
                    fragmentManager.beginTransaction()
                            .remove(fragmentManager.findFragmentByTag("text_button_wodeyuding"))
                            .show(fragmentManager.findFragmentByTag("fragment_mine"))
                            .commit();
                }
            }
        });
    }

    public void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_yuding.setLayoutManager(layoutManager);
        adapter = new YudingAdapter();
        recyclerView_yuding.setAdapter(adapter);
    }

    class YudingAdapter extends RecyclerView.Adapter<YudingHolder> {

        @Override
        public YudingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yudiong_item, parent, false);
            return new YudingHolder(view);
        }

        @Override
        public void onBindViewHolder(YudingHolder holder, final int position) {
            final Order order = cards.get(position);
            holder.bindView(order);
            //TODO: 删除订单
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BmobQuery<Order> orderBmobQuery = new BmobQuery<>();
                    orderBmobQuery.findObjects(new FindListener<Order>() {
                        @Override
                        public void done(List<Order> list, BmobException e) {
                            if (e == null) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getObjectId().equals(order.getObjectId())) {
                                        list.get(i).delete(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                                    cards.remove(position);
                                                    adapter.notifyDataSetChanged();
                                                    //TODO: 释放table资源
                                                    BmobQuery<Table> tableBmobQuery = new BmobQuery<>();
                                                    tableBmobQuery.findObjects(new FindListener<Table>() {
                                                        @Override
                                                        public void done(List<Table> list, BmobException e) {
                                                            if (e == null) {
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    if (list.get(i).getTable_number().equals(order.getTable_number())) {
                                                                        list.get(i).setState("1");
                                                                    }
                                                                }
                                                            } else {
                                                                Log.e(TAG, "done: " + e.getMessage());
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Log.e(TAG, "done: " + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                            } else {
                                Log.e(TAG, "done: " + e.getMessage());
                            }
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return cards.size();
        }
    }

    class YudingHolder extends RecyclerView.ViewHolder {

        private TextView time;
        private TextView store_name;
        private TextView order_money;
        private TextView order_table;
        private TextView order_time;
        private TextView status;
        private ImageView del;

        public YudingHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time_pay);
            store_name = itemView.findViewById(R.id.store_name);
            order_money = itemView.findViewById(R.id.money_order);
            order_table = itemView.findViewById(R.id.order_table);
            status = itemView.findViewById(R.id.status);
            order_time = itemView.findViewById(R.id.yuding_time);
            del = itemView.findViewById(R.id.yuding_item_del);
        }

        private void bindView(final Order order) {
            String userID = order.getStoreId();

            BmobQuery<User> bmobQuery = new BmobQuery<>();
            bmobQuery.getObject(userID, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        store_name.setText(user.getNickName());
                    }
                }
            });
            time.setText(order.getTime());
            order_money.setText("" + order.getMoney_order());
            order_table.setText("" + order.getTable_number());
            status.setText(order.getStatus());
            order_time.setText("" + order.getCreatedAt());
        }
    }
}
