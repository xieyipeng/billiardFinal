package com.example.a13834598889.billiards.FragmentShopKeeperNo1;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.a13834598889.billiards.JavaBean.BilliardStore;
import com.example.a13834598889.billiards.JavaBean.Order;
import com.example.a13834598889.billiards.JavaBean.ShopKeeper;
import com.example.a13834598889.billiards.JavaBean.Table;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.BmobRealTimeData.TAG;

public class Fragment_qiuzhuo_dianzhu extends Fragment {

    private LinearLayout noTable;
    private LinearLayout haveTable;
    private Integer bmobTableNum = 0;
    private TextView commitTextView;
    private EditText tableNum;
    private EditText vipPay;
    private EditText noemalPay;
    private SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView_table;
    private TableAdapter1 adapter;
    private List<Table> tableList = new ArrayList<>();
    private int num;
    private String state;
    private LinearLayout qiuzhuo;

    public static Fragment_qiuzhuo_dianzhu newInstance() {
        return new Fragment_qiuzhuo_dianzhu();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qiuzhuo_dianzhu, container, false);
        initViews(view);
        loadingView();
        initClicks();
        return view;
    }
//
//    class TableAdapter extends RecyclerView.Adapter<ViewHolder> {
//
//        private List<Table> tableList;
//
//        public TableAdapter(List<Table> tableList) {
//            this.tableList = tableList;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, int position) {
//            final Table table = tableList.get(position);
//            holder.textView_table_number.setText(String.valueOf(table.getTable_number()));
//            switch (table.getState()) {
//                case "1":
//                    holder.TextView_table_state.setText("空    闲");
//                    break;
//                case "2":
//                    holder.TextView_table_state.setText("已 预 约");
//                    holder.fight.setVisibility(View.VISIBLE);
//                    break;
//                case "3":
//                    holder.TextView_table_state.setText("已 开 始");
//                    holder.fight.setVisibility(View.VISIBLE);
//                    break;
//                default:
//                    break;
//            }
//
//
//            holder.tableView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    state = table.getState();
//                    num = table.getTable_number();
//                    if (state.equals("1")) {
//                        final SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
//                        dialog.setTitleText("确定开桌? ")
//                                .setCancelText("取消")
//                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setConfirmText("确定")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                                        BmobQuery<Table> query = new BmobQuery<>();
//                                        query.addWhereEqualTo("storeID", User.getCurrentUser().getObjectId());
//                                        query.addWhereEqualTo("table_number", num);
//                                        query.findObjects(new FindListener<Table>() {
//                                            @Override
//                                            public void done(List<Table> list, BmobException e) {
//                                                if (e == null) {
//                                                    for (Table table1 : list) {
//                                                        Table table2 = new Table();
//                                                        table2.setState("3");
//                                                        table2.update(table1.getObjectId(), new UpdateListener() {
//                                                            @Override
//                                                            public void done(BmobException e) {
//                                                                if (e == null) {
//                                                                    Toast.makeText(getContext(), "更新成功", Toast.LENGTH_LONG).show();
//                                                                } else {
//                                                                    Toast.makeText(getContext(), "更新失败", Toast.LENGTH_LONG).show();
//                                                                }
//                                                            }
//                                                        });
//                                                    }
//                                                }
//                                            }
//                                        });
//                                        loadingTable();
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .show();
//                        dialog.setCanceledOnTouchOutside(true);
//                    } else if (state.equals("3")) {
//                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
//                        sweetAlertDialog.setTitleText("确定顾客已经离开？");
//                        sweetAlertDialog.show();
//                        sweetAlertDialog.setCancelText("取消");
//                        sweetAlertDialog.setCancelable(true);
//                        sweetAlertDialog.setConfirmText("确定");
//                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                BmobQuery<Table> query = new BmobQuery<>();
//                                query.addWhereEqualTo("storeID", User.getCurrentUser().getObjectId());
//                                query.addWhereEqualTo("table_number", num);
//                                query.findObjects(new FindListener<Table>() {
//                                    @Override
//                                    public void done(List<Table> list, BmobException e) {
//                                        if (e == null) {
//                                            for (Table table1 : list) {
//                                                Table table2 = new Table();
//                                                table2.setState("1");
//                                                table2.update(table1.getObjectId(), new UpdateListener() {
//                                                    @Override
//                                                    public void done(BmobException e) {
//                                                        if (e == null) {
//                                                            Toast.makeText(getContext(), "更新成功", Toast.LENGTH_LONG).show();
//                                                        } else {
//                                                            Toast.makeText(getContext(), "更新失败", Toast.LENGTH_LONG).show();
//                                                        }
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//                                });
//
//                                BmobQuery<Order> query1 = new BmobQuery<>();
//                                query1.addWhereEqualTo("storeId", User.getCurrentUser().getObjectId());
//                                query1.addWhereEqualTo("table_number", num);
//                                query1.findObjects(new FindListener<Order>() {
//                                    @Override
//                                    public void done(List<Order> list, BmobException e) {
//                                        if (e == null) {
//                                            for (Order order : list) {
//                                                Order order1 = new Order();
//                                                order1.setStatus("已处理");
//                                                order1.update(order.getObjectId(), new UpdateListener() {
//                                                    @Override
//                                                    public void done(BmobException e) {
//                                                        if (e == null) {
//
//                                                        }
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//                                });
//                                loadingTable();
//                                sweetAlertDialog.dismiss();
//                            }
//                        });
//                    } else {
//                        final SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
//                        dialog.setTitleText("确定开桌？")
//                                .setConfirmText("确定")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        BmobQuery<Table> query = new BmobQuery<>();
//                                        query.addWhereEqualTo("storeID", User.getCurrentUser().getObjectId());
//                                        query.addWhereEqualTo("table_number", num);
//                                        query.findObjects(new FindListener<Table>() {
//                                            @Override
//                                            public void done(List<Table> list, BmobException e) {
//                                                if (e == null) {
//                                                    for (Table table1 : list) {
//                                                        Table table2 = new Table();
//                                                        table2.setState("3");
//                                                        table2.update(table1.getObjectId(), new UpdateListener() {
//                                                            @Override
//                                                            public void done(BmobException e) {
//                                                                if (e == null) {
//                                                                    Toast.makeText(getContext(), "更新成功", Toast.LENGTH_LONG).show();
//                                                                } else {
//                                                                    Toast.makeText(getContext(), "更新失败", Toast.LENGTH_LONG).show();
//                                                                }
//                                                            }
//                                                        });
//                                                    }
//                                                }
//                                            }
//                                        });
//                                        loadingTable();
//                                        dialog.dismiss();
//                                    }
//                                }).show();
//                        dialog.setCanceledOnTouchOutside(true);
//                    }
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return tableList.size();
//        }
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private View tableView;
//        private TextView textView_table_number;
//        private TextView TextView_table_state;
//        private RelativeLayout fight;
//
//
//        public ViewHolder(View view) {
//            super(view);
//            tableView = view;
//            textView_table_number = view.findViewById(R.id.table_number);
//            TextView_table_state = view.findViewById(R.id.table_state);
//            fight = view.findViewById(R.id.fight_state);
//        }
//    }

    class TableAdapter1 extends BaseQuickAdapter<Table, BaseViewHolder> {

        public TableAdapter1(int layoutResId, @Nullable List<Table> data, Context context) {
            super(layoutResId, data);
            tableList = data;
        }

        @Override
        protected void convert(BaseViewHolder helper, Table table) {
            helper.setText(R.id.table_number, String.valueOf(table.getTable_number()));
            RelativeLayout fight = helper.getView(R.id.fight_state);
            switch (table.getState()) {
                case "1":
                    helper.setText(R.id.table_state, "空    闲");
                    break;
                case "2":
                    helper.setText(R.id.table_state, "已 预 约");
                    fight.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    helper.setText(R.id.table_state, "已 开 始");
                    fight.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    //TODO: 点击事件
    private void initClicks() {

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingTable();
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });

        commitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableNum.getText().toString().equals("")
                        || vipPay.getText().toString().equals("")
                        || noemalPay.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "不能为空", Toast.LENGTH_SHORT).show();
                } else if (!isInteger(tableNum.getText().toString())) {
                    Toast.makeText(getContext(), "球桌数必须为整数", Toast.LENGTH_SHORT).show();
                } else if (!isNumber(vipPay.getText().toString())) {
                    Toast.makeText(getContext(), "会员价必须为数字", Toast.LENGTH_SHORT).show();
                } else if (!isNumber(noemalPay.getText().toString())) {
                    Toast.makeText(getContext(), "普通价必须为数字", Toast.LENGTH_SHORT).show();
                } else {
                    updataTableNumber();
                    updataPay();
                    for (int i = 0; i < Integer.valueOf(tableNum.getText().toString()); i++) {
                        Table table = new Table();
                        table.setStoreID(ShopKeeper.getCurrentUser().getObjectId());
                        table.setTable_number(i);
                        table.setState("1");
                        table.setStart(false);
                        table.setReserve(false);
                        table.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Log.e(TAG, "done: " + s);
                                } else {
                                    Log.e(TAG, "done: " + e.getMessage());
                                }
                            }
                        });
                    }
                    noTable.setVisibility(View.GONE);
                    haveTable.setVisibility(View.VISIBLE);
                    qiuzhuo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }
        });
    }

    private void loadingView() {
        BmobQuery<ShopKeeper> tableBmobQuery = new BmobQuery<>();
        tableBmobQuery.findObjects(new FindListener<ShopKeeper>() {
            @Override
            public void done(List<ShopKeeper> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getObjectId().equals(ShopKeeper.getCurrentUser().getObjectId())) {
                            if (list.get(i).getTableNum() != null) {
                                bmobTableNum = list.get(i).getTableNum();
                            }
                        }
                    }
                    if (bmobTableNum == 0) {
                        noTable.setVisibility(View.VISIBLE);
                        haveTable.setVisibility(View.INVISIBLE);
                    } else {
                        noTable.setVisibility(View.GONE);
                        haveTable.setVisibility(View.VISIBLE);
                        loadingTable();
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    public void loadingTable() {
        tableList.clear();
        BmobQuery<Table> tableBmobQuery = new BmobQuery<>();
        tableBmobQuery.findObjects(new FindListener<Table>() {
            @Override
            public void done(List<Table> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStoreID().equals(User.getCurrentUser().getObjectId())) {
                            tableList.add(list.get(i));
                        }
                    }
                    Log.e(TAG, "done: "+tableList.size());
                    if (tableList.size() != 0) {
                        adapter = new TableAdapter1(R.layout.table_item, tableList, getContext());
                        //TODO: 点击事件
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(final BaseQuickAdapter adapter, View view, final int position) {
                                state = tableList.get(position).getState();
                                num = tableList.get(position).getTable_number();
                                if (state.equals("1")) {
                                    final SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    dialog.setTitleText("确定开桌? ")
                                            .setCancelText("取消")
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setConfirmText("确定")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    BmobQuery<Table> query = new BmobQuery<>();
                                                    query.addWhereEqualTo("storeID", User.getCurrentUser().getObjectId());
                                                    query.addWhereEqualTo("table_number", num);
                                                    query.findObjects(new FindListener<Table>() {
                                                        @Override
                                                        public void done(List<Table> list, BmobException e) {
                                                            if (e == null) {
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    Table table = list.get(i);
                                                                    table.setState("3");
                                                                    table.update(new UpdateListener() {
                                                                        @Override
                                                                        public void done(BmobException e) {
                                                                            if (e == null) {
                                                                                tableList.get(position).setState("3");
                                                                                adapter.notifyItemChanged(position);
                                                                                Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                                                                            } else {
                                                                                Log.e(TAG, "done: " + e.getMessage());
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }
                                                    });
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.setCanceledOnTouchOutside(true);
                                } else if (state.equals("3")) {
                                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    sweetAlertDialog.setTitleText("确定顾客已经离开？");
                                    sweetAlertDialog.show();
                                    sweetAlertDialog.setCancelText("取消");
                                    sweetAlertDialog.setCancelable(true);
                                    sweetAlertDialog.setConfirmText("确定");
                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            BmobQuery<Table> tableBmobQuery = new BmobQuery<>();
                                            tableBmobQuery.findObjects(new FindListener<Table>() {
                                                @Override
                                                public void done(List<Table> list, BmobException e) {
                                                    if (e == null) {
                                                        for (int i = 0; i < list.size(); i++) {
                                                            if (list.get(i).getStoreID().equals(User.getCurrentUser().getObjectId()) && list.get(i).getTable_number() == num) {
                                                                Log.e(TAG, "done: " + list.get(i).getObjectId());
                                                                Table newTable = list.get(i);
                                                                newTable.setObjectId(list.get(i).getObjectId());
                                                                newTable.setState("1");
                                                                newTable.update(new UpdateListener() {
                                                                    @Override
                                                                    public void done(BmobException e) {
                                                                        if (e == null) {
                                                                            tableList.get(position).setState("1");
                                                                            adapter.notifyItemChanged(position);
                                                                            Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                                                                            //TODO: order表
                                                                            updataOrder();
                                                                        } else {
                                                                            Log.e(TAG, "done: " + e.getMessage());
                                                                        }
                                                                    }

                                                                    private void updataOrder() {
                                                                        BmobQuery<Order> orderBmobQuery=new BmobQuery<>();
                                                                        orderBmobQuery.findObjects(new FindListener<Order>() {
                                                                            @Override
                                                                            public void done(List<Order> list, BmobException e) {
                                                                                if (e==null){
                                                                                    for (int j = 0; j < list.size(); j++) {

                                                                                    }
                                                                                }else {
                                                                                    Log.e(TAG, "done: "+e.getMessage() );
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                                //TODO 客流量加1
                                                                BmobQuery<BilliardStore> billiardStoreBmobQuery = new BmobQuery<>();
                                                                billiardStoreBmobQuery.findObjects(new FindListener<BilliardStore>() {
                                                                    @Override
                                                                    public void done(List<BilliardStore> list, BmobException e) {
                                                                        if (e == null) {
                                                                            for (int j = 0; j < list.size(); j++) {
                                                                                if (list.get(j).getStoreID().equals(User.getCurrentUser().getObjectId())) {
                                                                                    BilliardStore newBilliardStore = list.get(j);
                                                                                    newBilliardStore.setNum_customer(newBilliardStore.getNum_customer() + 1);
                                                                                    newBilliardStore.update(new UpdateListener() {
                                                                                        @Override
                                                                                        public void done(BmobException e) {
                                                                                            if (e == null) {
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
                                                        }
                                                    } else {
                                                        Log.e(TAG, "done: " + e.getMessage());
                                                    }
                                                }
                                            });
                                            sweetAlertDialog.dismiss();
                                            sweetAlertDialog.setCanceledOnTouchOutside(true);
                                        }
                                    });
                                } else {
                                    final SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    dialog.setTitleText("确定开桌？")
                                            .setConfirmText("确定")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    BmobQuery<Table> query = new BmobQuery<>();
                                                    query.addWhereEqualTo("storeID", User.getCurrentUser().getObjectId());
                                                    query.addWhereEqualTo("table_number", num);
                                                    query.findObjects(new FindListener<Table>() {
                                                        @Override
                                                        public void done(List<Table> list, BmobException e) {
                                                            if (e == null) {
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    Table table=list.get(i);
                                                                    table.setState("3");
                                                                    table.update(new UpdateListener() {
                                                                        @Override
                                                                        public void done(BmobException e) {
                                                                            if (e==null){
                                                                                tableList.get(position).setState("3");
                                                                                adapter.notifyItemChanged(position);
                                                                                Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                                                                            }else {
                                                                                Log.e(TAG, "done: "+e.getMessage());
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }else {
                                                                Log.e(TAG, "done: "+e.getMessage() );
                                                            }
                                                        }
                                                    });
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    dialog.setCanceledOnTouchOutside(true);
                                }
                            }
                        });

                        adapter.openLoadAnimation();
                        //TODO: 设置滑动动画
                        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                        adapter.isFirstOnly(false);

                        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                        recyclerView_table.setLayoutManager(layoutManager);
                        recyclerView_table.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "球桌数为0", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }


    //TODO: 更新支付金额
    private void updataPay() {
        BmobQuery<BilliardStore> billiardStoreBmobQuery = new BmobQuery<>();
        billiardStoreBmobQuery.findObjects(new FindListener<BilliardStore>() {
            @Override
            public void done(List<BilliardStore> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (ShopKeeper.getCurrentUser().getObjectId().equals(list.get(i).getStoreID())) {
                            list.get(i).setPrice_vip(Double.valueOf(vipPay.getText().toString()));
                            list.get(i).setPrice_pt(Double.valueOf(noemalPay.getText().toString()));
                            list.get(i).update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(getContext(), "价格上传成功", Toast.LENGTH_SHORT).show();
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

    //TODO: 更新球桌数量
    private void updataTableNumber() {
        ShopKeeper shopKeeper = new ShopKeeper();
        shopKeeper.setObjectId(ShopKeeper.getCurrentUser().getObjectId());
        shopKeeper.setStore(true);
        shopKeeper.setTableNum(Integer.valueOf(tableNum.getText().toString()));
        shopKeeper.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getContext(), "桌球数上传成功", Toast.LENGTH_SHORT).show();
                    loadingView();
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    //TODO: 初始化布局
    private void initViews(View view) {
        noTable = view.findViewById(R.id.noo_table);
        haveTable = view.findViewById(R.id.have_table);
        noTable.setVisibility(View.GONE);
        haveTable.setVisibility(View.GONE);
        commitTextView = view.findViewById(R.id.shop_setting_table_commit);
        tableNum = view.findViewById(R.id.shop_setting_table_number);
        vipPay = view.findViewById(R.id.shop_setting_table_vip_pay);
        noemalPay = view.findViewById(R.id.shop_setting_table_normal_pay);
        recyclerView_table = view.findViewById(R.id.recycler_View_table_dianzhu);
        refreshLayout = view.findViewById(R.id.table_refresh);
        qiuzhuo = view.findViewById(R.id.qiuzhuo);
    }

    public static boolean isNumber(String str) {
        //是否数字
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

    public static boolean isInteger(String str) {
        //是否整数
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}
