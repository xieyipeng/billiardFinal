package com.example.a13834598889.billiards.FragmentCustomerOrder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.example.a13834598889.billiards.JavaBean.BilliardStore;
import com.example.a13834598889.billiards.JavaBean.Table;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.GlideImageLoader;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/7/24.
 */

public class Fragment_billiards extends Fragment {

    private String shopID;//店家id
    private Integer shopInteger;//店家客流量
    private String shopNickName;//店家昵称
    private Banner mBanner;
    private Table selectTable;
    private TextView time;
    private List<String> list_image = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private List<Table> mTables = new ArrayList<>();
    private TextView shopName;

    //TODO: into
    public static Fragment_billiards newInstance(String id, Integer count, String name) {
        Fragment_billiards fragment_billiards = new Fragment_billiards();
        fragment_billiards.shopID = id;
        fragment_billiards.shopInteger = count;
        fragment_billiards.shopNickName = name;
        return fragment_billiards;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        View view = inflater.inflate(R.layout.fragment_billiards, container, false);
        selectTable = new Table();
        initViews(view);
        setBanner();
        addData();
        initClicks();
        return view;
    }

    private void addData() {
        BmobQuery<Table> query = new BmobQuery<>();
        query.findObjects(new FindListener<Table>() {
            @Override
            public void done(List<Table> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStoreID().equals(shopID)) {
                            mTables.add(list.get(i));
                        }
                    }
                    StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(manager);
                    TableAdapter adapter = new TableAdapter(mTables);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    private void initClicks() {
        shopName.setText("" + shopNickName);
    }

    private void initViews(View view) {

//        mScrollView = view.findViewById(R.id.billiards_scrollView);
        mBanner = view.findViewById(R.id.banner);
        mRecyclerView = view.findViewById(R.id.recycler_view_table);
        TextView custom_count = view.findViewById(R.id.count);
        time = view.findViewById(R.id.time);
        custom_count.setText("客流量：" + shopInteger);
        shopName = view.findViewById(R.id.shop_name);
    }


    private void setBanner() {
        BmobQuery<BilliardStore> query = new BmobQuery<>();
        query.findObjects(new FindListener<BilliardStore>() {
            @Override
            public void done(List<BilliardStore> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStoreID().equals(shopID)) {
                            if (list.get(i).getPicture_1() != null) {
                                list_image.add(list.get(i).getPicture_1().getFileUrl());
                            } else {
                                list_image.add("https://s1.ax1x.com/2018/08/14/P2Q75D.jpg");
                            }
                            if (list.get(i).getPicture_2() != null) {
                                list_image.add(list.get(i).getPicture_2().getFileUrl());
                            } else {
                                list_image.add("https://s1.ax1x.com/2018/08/14/P2QOxA.jpg");
                            }
                            if (list.get(i).getPicture_3() != null) {
                                list_image.add(list.get(i).getPicture_3().getFileUrl());
                            } else {
                                list_image.add("https://s1.ax1x.com/2018/08/14/P2QxqP.jpg");
                            }
                            mBanner.setImages(list_image);
                            mBanner.setImageLoader(new GlideImageLoader());
                            mBanner.isAutoPlay(true);
                            mBanner.setDelayTime(2000);
                            mBanner.start();
                        }
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }


    public class TableAdapter extends RecyclerView.Adapter<TableHolder> {

        private List<Table> mTables;

        public TableAdapter(List<Table> tables) {
            this.mTables = tables;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public TableHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.table_item, parent, false);
            final TableHolder holder = new TableHolder(view1);
            holder.tableView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    final Table table = mTables.get(position);


                    final SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("您已选择" + table.getTable_number() + "号球桌")
                            .setConfirmText("饮料配送")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                            .addToBackStack(null)  //返回
                                            .add(R.id.fragment_container, Fragmentstore.newInstance(shopID, mTables.size()), "fragment_store")
                                            .commit();
                                    dialog.dismiss();
                                }
                            })
                            .setCancelText("预约打球")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        if (Integer.valueOf(selectTable.getState()) == 1) {
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            fragmentManager.beginTransaction()
                                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                                    .addToBackStack(null)
                                                    .add(R.id.fragment_container, FragmentPay.newInstance(shopID, shopNickName, selectTable.getTable_number()),"fragment_pay")
                                                    .commit();
                                            dialog.dismiss();
                                        }else {
                                            Toast.makeText(getContext(), "该桌已被预约", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            })
                            .show();
                    dialog.setCanceledOnTouchOutside(true);

                    table.setStoreID(shopID);
                    BmobQuery<Table> query = new BmobQuery<>();
                    query.findObjects(new FindListener<Table>() {
                        @Override
                        public void done(List<Table> list, BmobException e) {
                            if (e == null) {
                                for (int i = 0; i < list.size(); i++) {
                                    loadingTable(list, i, table);
                                }
                            }
                        }
                    });
                }
            });
            return holder;
        }

        private void loadingTable(List<Table> list, int i, Table table) {
            if (list.get(i).getStoreID().equals(shopID) && list.get(i).getTable_number().equals(table.getTable_number())) {
                table.setObjectId(list.get(i).getObjectId());
                Date date = new Date();
                list.get(i).setAppoint_time(date.toString());
                table.setAppoint_time(date.toString());
                switch (list.get(i).getState()) {
                    case "1":
                        break;
                    case "2":
                        break;
                    case "3":
                        break;
                    default:
                        break;
                }
                selectTable = table;
                table.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.d(TAG, "done: 预约时间更新成功");
                        } else {
                            Log.e(TAG, "done: " + e.getMessage());
                        }
                    }
                });
            }
        }

        @Override
        public void onBindViewHolder(TableHolder holder, int position) {
            Table table = mTables.get(position);
            holder.bindView(table);

        }

        @Override
        public int getItemCount() {
            return mTables.size();
        }
    }

    public class TableHolder extends RecyclerView.ViewHolder {
        private TextView table_number;
        private TextView table_state;
        private RelativeLayout mRelativeLayout;
        private View tableView;

        private TableHolder(View itemView) {
            super(itemView);
            table_number = itemView.findViewById(R.id.table_number);
            table_state = itemView.findViewById(R.id.table_state);
            mRelativeLayout = itemView.findViewById(R.id.fight_state);
            tableView = itemView;
        }

        public void bindView(Table table) {
            table_number.setText(String.valueOf(table.getTable_number()));
            switch (Integer.valueOf(table.getState())) {
                case 1:
                    table_state.setText("空闲");
                    break;
                case 2:
                    table_state.setText("已预约");
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    table_state.setText("已开始");
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
