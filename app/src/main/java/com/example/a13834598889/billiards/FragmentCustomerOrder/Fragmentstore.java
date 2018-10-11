package com.example.a13834598889.billiards.FragmentCustomerOrder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.example.a13834598889.billiards.JavaBean.Choose;
import com.example.a13834598889.billiards.JavaBean.Goods;
import com.example.a13834598889.billiards.JavaBean.Order;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.OrderInfoUtil2_0;
import com.example.a13834598889.billiards.Tool.PayResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.newim.db.MigrationHelper.TAG;
import static com.example.a13834598889.billiards.FragmentShopKeeperNo1.Fragment_qiuzhuo_dianzhu.isInteger;

/**
 * Created by Administrator on 2018/7/26.
 */

public class Fragmentstore extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Goods> mGoods = new ArrayList<>();
    private String id;
    private double money;
    private Button pay;
    private String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCM+4vsh5mskSWiwDwBrMc8dGsTmtQInVimEkPnNsYQQ1ENvRjTttkSU3cXDrmO+coZCDWYRQhS72G/xHhgdcHf5rH+oTT6j3Xenww5oD41azPmRGnCNXLvReb1gHfYD64josY08nwBakSt/cHlSMCfkNDez0O0in1sEucYILNYkFCbpHY5rhgvN0jEAqYg2z43piEvARhiSjRaKHb8ZwrNFye91HsHxV5dHUmqwkfUaPl5yfAaYm7dlr/JlBsYEsOOwV11PvKYEQuTI6bHt//2w/D9JBnAXXxfeXndhBnlHmo+lrRsIK7ZPTz0rBnOb3KUpqMxn+LLo3Us+gTD/QLFAgMBAAECggEAKD8jScnIKAhjmxuPxdaiJfMCIl2fzDnG9dnfAqGTV08wU2C5Nq9LNr0XEUEF3fgXJqA+VJLYdnyaBhm7V6YmS5nbFFrG+gR8XKpA3i6Ns8g/z6uWGXgSsJXfAhTDoa2QQ+IS/Uh/+BNzOcxoTuE/BA5eYkz/AgpLFdAroqqrKEvR8FXbhFjES4yklTtC2TweDF2xCwEoeMFA8lbBjLECc40lesRoncC49/mxVbAZOwEhcB7hkvnLZ4one03O/gE0wdwyEbJHm23YyK2FkksDPtcCMBx1+HC4uA7zn3go3ZIjrfpeqSGG3d19UXDkDSIVTSqlGdrhonbOAmEJMAaOgQKBgQD6DpCFb3MxukpLchH6p4PJuXWr5evO5ZIAvDSvliKGkUGR37dlCGUmIGAgvCL2ldOatjQcwUcyZTqkwNuPgTtMzl2WSs3aiR0lzf2UQTD4cUDWRtyBj25J69ZqoR/87IyWqqqd4G7tdWr9a7xIxOn+9Rtr1qjj5auop/+frneCJQKBgQCQVVXJUx0MWuMpw9d20+XaUSzJEnv1ra6WHVjw04VRh5OeNzFw/S6RJDkgPPB5TsCMaGxNE4WhykKWBs0e7t0CQ9vufjEz5OXbCQb0By7f3pYtrOXTl20YMlFOcl/d5RWpRIECA5Ieqc5mkv25xnw1onkHC+47UZ8t/xY7hvWMIQKBgQDq0ViH5aPwU7dG6ATYNAy/F0jYNt5c+RpFVHfJV5xub+N6P/KxjtOlnQuIUgQnOYVvqKCBTEM2oPcUFgNY3Iu6UaRy6SYsjUvw32K8oQeClp/DWOHjTLTN+AjvMwWd9ukC55u3DDY/CV+CQXSbhUcT5Epu1zLcaCXuCG01H5ocfQKBgCuzWsUZQCtUfYFQxbU51Vdzyo6a5SNu0fSrsBlCwhP8a8q0xWiDkAzsHcvQB7ODD7Ozjk8MASMKfXy1VHfwNMSRzU55sOYYgSv/oLZUUnIAEBKGThPxvltcKNgKs1IZIaTdk/4LHLviCBdwnBgaq9MFfYWPrDMTtJGVsaKWa1RhAoGBAMqCNJcNDYs3vj8BDMdpWl3pVq5hl2BieYzvS0ZSKU99E278ejYWs2cn1kPaXO3qfs/V/uufYeeRK5mMd6xepBdI0zpe5vy99yB+pdA2tS7QAN5Wbk1NvFmHDnrMDRRikDI3+trTGjWPyIKk+L1lBJ2sPqnCbHnQuerWiEOA/jts";
    public static final String APPID = "2016091800539811";
    private static final int SDK_PAY_FLAG = 1001;
    private FragmentManager fragmentManager;
    private ImageView back;
    //    private TextView good_info;
    private RecyclerView goodChooseRecycleView;
    private TextView goodMoney;
    private List<Choose> chooseList = new ArrayList<>();
    private ChooseAdapter chooseAdapter;
    private EditText inputTable;
    private int tableNum;
    private int num;

    //TODO: fragment_store
    public static Fragmentstore newInstance(String id, int table) {
        Fragmentstore fragmentstore = new Fragmentstore();
        fragmentstore.id = id;
        fragmentstore.tableNum = table;
        return fragmentstore;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atore, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initView(view);
        loadingData();
        loadingData2();
        initClick();
        return view;
    }

    private void loadingData2() {
        chooseList.clear();
        chooseAdapter = new ChooseAdapter(chooseList);
        goodChooseRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        goodChooseRecycleView.setAdapter(chooseAdapter);
    }

    private void initClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.findFragmentByTag("fragment_store") != null) {
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("fragment_store"))
                            .remove(fragmentManager.findFragmentByTag("fragment_store"))
                            .show(fragmentManager.findFragmentByTag("into"))
                            .commit();
                }
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: tableNum == " + tableNum);
                if (inputTable.getText().toString().equals("") ||
                        money == 0.0 || !isInteger(inputTable.getText().toString()) ||
                        Integer.valueOf(inputTable.getText().toString()) > tableNum) {
                    Toast.makeText(getContext(), "请输入正确的参数", Toast.LENGTH_SHORT).show();
                } else {
                    num = Integer.valueOf(inputTable.getText().toString());
                    //秘钥验证的类型 true:RSA2 false:RSA
                    OrderInfoUtil2_0.money = money;
                    boolean rsa = false;
                    //构造支付订单参数列表
                    Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa);
                    //构造支付订单参数信息
                    String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
                    //对支付参数信息进行签名
                    String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE, rsa);
                    //订单信息
                    final String orderInfo = orderParam + "&" + sign;
                    //异步处理
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            //新建任务
                            PayTask alipay = new PayTask(getActivity());
                            //获取支付结果
                            Map<String, String> result = alipay.payV2(orderInfo, true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }
        });
    }

    private void loadingData() {
        BmobQuery<Goods> query = new BmobQuery<>();
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStoreID().equals(id)) {
                            mGoods.add(list.get(i));
                        }
                    }
                    if (mGoods.size() != 0) {
                        DrinkAdapter adapter = new DrinkAdapter(mGoods);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "该店家没有上传任何商品", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    private void initView(View view) {
        back = view.findViewById(R.id.good_back);
        mRecyclerView = view.findViewById(R.id.recycler_view_goods);
        goodChooseRecycleView = view.findViewById(R.id.good_choose_recycle_view);
//        good_info = view.findViewById(R.id.goods_info);
        goodMoney = view.findViewById(R.id.money);
        pay = view.findViewById(R.id.pay);
        inputTable = view.findViewById(R.id.store_input_table_number);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //同步获取结果
                    String resultInfo = payResult.getResult();
                    Log.i("Pay", "Pay:" + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();

                        BmobQuery<Order> orderBmobQuery = new BmobQuery<>();
                        orderBmobQuery.findObjects(new FindListener<Order>() {
                            @Override
                            public void done(List<Order> list, BmobException e) {
                                if (e == null) {
                                    for (Order order : list) {
                                        if (order.getUserID().equals(User.getCurrentUser().getObjectId())
                                                && order.getTable_number() == num
                                                && order.getStatus().equals("已预约")) {
                                           Order order1 = new Order();
                                           order1.setMoneyGoods(money);
                                           order1.update(order.getObjectId(), new UpdateListener() {
                                               @Override
                                               public void done(BmobException e) {
                                                   if (e == null){

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
                    } else {
                        Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private class ChooseAdapter extends RecyclerView.Adapter<ChooseHolder> {

        private List<Choose> chooseList;

        ChooseAdapter(List<Choose> chooses) {
            chooseList = chooses;
        }

        @Override
        public ChooseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_choose_goods, parent, false);
            return new ChooseHolder(view);
        }

        @Override
        public void onBindViewHolder(ChooseHolder holder, final int position) {
            final Choose choose = chooseList.get(position);
            holder.bindView(choose);
            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseList.get(position).setNum(chooseList.get(position).getNum() - 1);
                    for (int i = 0; i < chooseList.size(); i++) {
                        if (chooseList.get(i).getNum() == 0) {
                            chooseList.remove(i);
                        }
                    }
                    chooseAdapter.notifyDataSetChanged();
                    resetMoney();
                }
            });
        }

        @Override
        public int getItemCount() {
            return chooseList.size();
        }
    }

    private class ChooseHolder extends RecyclerView.ViewHolder {

        private TextView choose_name;
        private TextView choose_num;
        private LinearLayout main;

        public ChooseHolder(View itemView) {
            super(itemView);
            choose_name = itemView.findViewById(R.id.good_choose_item_name);
            choose_num = itemView.findViewById(R.id.good_choose_item_num);
            main = itemView.findViewById(R.id.good_choose_item_LinearLayout);
        }

        public void bindView(Choose choose) {
            choose_num.setText("X " + String.valueOf(choose.getNum()));
            choose_name.setText(choose.getName());
        }

    }

    private class DrinkAdapter extends RecyclerView.Adapter<DrinkHolder> {
        private List<Goods> mGoodsList;

        DrinkAdapter(List<Goods> goods) {
            this.mGoodsList = goods;
        }

        @Override
        public DrinkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.drink_item, parent, false);
            final DrinkHolder holder = new DrinkHolder(view1);
            holder.good_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Goods goods = mGoodsList.get(position);

                    Choose choose = new Choose();
                    choose.setName(goods.getGood_name());
                    choose.setPrice(goods.getGood_price());

                    //TODO: 如果存在该商品，则数量加一
                    boolean have = false;
                    for (int i = 0; i < chooseList.size(); i++) {
                        if (chooseList.get(i).getName().equals(choose.getName())) {
                            have = true;
                            chooseList.get(i).setNum(chooseList.get(i).getNum() + 1);
                            chooseAdapter.notifyDataSetChanged();
                        }
                    }

                    //TODO: 如果list中没有该商品，设置数量为1，并添加
                    if (!have) {
                        choose.setNum(choose.getNum() + 1);
                        chooseList.add(choose);
                        chooseAdapter.notifyDataSetChanged();
                    }
                    resetMoney();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(DrinkHolder holder, int position) {
            Goods goods = mGoodsList.get(position);
            holder.bindView(goods);
        }

        @Override
        public int getItemCount() {
            return mGoodsList.size();
        }
    }

    private void resetMoney() {
        money = 0.0;
        for (int i = 0; i < chooseList.size(); i++) {
            money = money + chooseList.get(i).getNum() * chooseList.get(i).getPrice();
        }
        goodMoney.setText(String.valueOf(money));
    }

    private class DrinkHolder extends RecyclerView.ViewHolder {
        private ImageView good_image;
        private TextView good_name;
        private TextView good_price;
        private View good_view;


        public DrinkHolder(View itemView) {
            super(itemView);
            good_view = itemView;
            good_image = itemView.findViewById(R.id.good_image);
            good_name = itemView.findViewById(R.id.good_name);
            good_price = itemView.findViewById(R.id.good_price);

        }

        public void bindView(Goods goods) {
            good_price.setText(goods.getGood_price().toString());
            good_name.setText(goods.getGood_name());
            downloadFile_picture(goods.getPicture(), good_image);

        }
    }

    private void downloadFile_picture(BmobFile file, final ImageView view) {
        file.download(new DownloadFileListener() {
            @Override
            public void done(final String s, BmobException e) {
                if (e == null) {
                    if (s != null) {
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(getActivity().getApplication()).load(s).into(view);
                                }
                            });
                        } catch (Exception d) {
                            d.printStackTrace();
                        }
                    } else {
                    }
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }
}
