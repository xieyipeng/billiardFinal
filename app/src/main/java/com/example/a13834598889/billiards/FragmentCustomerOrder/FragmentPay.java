package com.example.a13834598889.billiards.FragmentCustomerOrder;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.a13834598889.billiards.JavaBean.Customer;
import com.example.a13834598889.billiards.JavaBean.Order;
import com.example.a13834598889.billiards.JavaBean.Table;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.OrderInfoUtil2_0;
import com.example.a13834598889.billiards.Tool.PayResult;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.volley.VolleyLog.TAG;

/**
 * Created by Administrator on 2018/8/5.
 */

public class FragmentPay extends Fragment {
    private String id;
    private String name;
    private Integer table_num;
    private TextView title_pay;
    private Button pay;
    private TimePicker mTimePicker;
    private TextView pay_money;
    private TextView time_show;
    private ImageView back;
    private android.support.v4.app.FragmentManager fragmentManager;
    private String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCM+4vsh5mskSWiwDwBrMc8dGsTmtQInVimEkPnNsYQQ1ENvRjTttkSU3cXDrmO+coZCDWYRQhS72G/xHhgdcHf5rH+oTT6j3Xenww5oD41azPmRGnCNXLvReb1gHfYD64josY08nwBakSt/cHlSMCfkNDez0O0in1sEucYILNYkFCbpHY5rhgvN0jEAqYg2z43piEvARhiSjRaKHb8ZwrNFye91HsHxV5dHUmqwkfUaPl5yfAaYm7dlr/JlBsYEsOOwV11PvKYEQuTI6bHt//2w/D9JBnAXXxfeXndhBnlHmo+lrRsIK7ZPTz0rBnOb3KUpqMxn+LLo3Us+gTD/QLFAgMBAAECggEAKD8jScnIKAhjmxuPxdaiJfMCIl2fzDnG9dnfAqGTV08wU2C5Nq9LNr0XEUEF3fgXJqA+VJLYdnyaBhm7V6YmS5nbFFrG+gR8XKpA3i6Ns8g/z6uWGXgSsJXfAhTDoa2QQ+IS/Uh/+BNzOcxoTuE/BA5eYkz/AgpLFdAroqqrKEvR8FXbhFjES4yklTtC2TweDF2xCwEoeMFA8lbBjLECc40lesRoncC49/mxVbAZOwEhcB7hkvnLZ4one03O/gE0wdwyEbJHm23YyK2FkksDPtcCMBx1+HC4uA7zn3go3ZIjrfpeqSGG3d19UXDkDSIVTSqlGdrhonbOAmEJMAaOgQKBgQD6DpCFb3MxukpLchH6p4PJuXWr5evO5ZIAvDSvliKGkUGR37dlCGUmIGAgvCL2ldOatjQcwUcyZTqkwNuPgTtMzl2WSs3aiR0lzf2UQTD4cUDWRtyBj25J69ZqoR/87IyWqqqd4G7tdWr9a7xIxOn+9Rtr1qjj5auop/+frneCJQKBgQCQVVXJUx0MWuMpw9d20+XaUSzJEnv1ra6WHVjw04VRh5OeNzFw/S6RJDkgPPB5TsCMaGxNE4WhykKWBs0e7t0CQ9vufjEz5OXbCQb0By7f3pYtrOXTl20YMlFOcl/d5RWpRIECA5Ieqc5mkv25xnw1onkHC+47UZ8t/xY7hvWMIQKBgQDq0ViH5aPwU7dG6ATYNAy/F0jYNt5c+RpFVHfJV5xub+N6P/KxjtOlnQuIUgQnOYVvqKCBTEM2oPcUFgNY3Iu6UaRy6SYsjUvw32K8oQeClp/DWOHjTLTN+AjvMwWd9ukC55u3DDY/CV+CQXSbhUcT5Epu1zLcaCXuCG01H5ocfQKBgCuzWsUZQCtUfYFQxbU51Vdzyo6a5SNu0fSrsBlCwhP8a8q0xWiDkAzsHcvQB7ODD7Ozjk8MASMKfXy1VHfwNMSRzU55sOYYgSv/oLZUUnIAEBKGThPxvltcKNgKs1IZIaTdk/4LHLviCBdwnBgaq9MFfYWPrDMTtJGVsaKWa1RhAoGBAMqCNJcNDYs3vj8BDMdpWl3pVq5hl2BieYzvS0ZSKU99E278ejYWs2cn1kPaXO3qfs/V/uufYeeRK5mMd6xepBdI0zpe5vy99yB+pdA2tS7QAN5Wbk1NvFmHDnrMDRRikDI3+trTGjWPyIKk+L1lBJ2sPqnCbHnQuerWiEOA/jts";
    public static final String APPID = "2016091800539811";
    private static final int SDK_PAY_FLAG = 1001;
    private int my_hour, my_minute;
    private static final int TIMEPICKER_DIALOG_1 = 0;//设置Dialog的id
    private static double money1;

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
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");

                        Order order = new Order();
                        order.setTime(dateFormat.format(date));
                        order.setMoney_order(money1);
                        order.setUserID(Customer.getCurrentUser(Customer.class).getObjectId());
                        order.setStoreId(id);
                        order.setTable_number(table_num);
                        order.setStatus("已预约");
                        order.setTime_order("" + dateFormat.format(date) + time_show.getText().toString());

                        order.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {

                                } else {
                                    Log.e(TAG, "done: " + e.getMessage());
                                }
                            }
                        });

                        BmobQuery<Table> query = new BmobQuery<>();
                        query.addWhereEqualTo("table_number", table_num);
                        query.addWhereEqualTo("storeID", id);
                        query.setLimit(200);
                        query.findObjects(new FindListener<Table>() {
                            @Override
                            public void done(List<Table> list, BmobException e) {
                                if (e == null) {
                                    for (Table table : list) {
                                            table.setState("2");
                                            table.update(table.getObjectId(), new UpdateListener() {
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
                                }

                        });
                    } else {
                        Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public static FragmentPay newInstance(String id, String name, Integer num) {
        FragmentPay fragmentPay = new FragmentPay();
        fragmentPay.id = id;
        fragmentPay.name = name;
        fragmentPay.table_num = num;
        return fragmentPay;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        fragmentManager=getActivity().getSupportFragmentManager();
        initViews(view);
        initClicks();
        return view;
    }

    private void initViews(View view) {
        title_pay = view.findViewById(R.id.title_name);
        title_pay.setText(name);
        time_show = view.findViewById(R.id.time_show);
        pay_money = view.findViewById(R.id.pay_money);
        mTimePicker = view.findViewById(R.id.time_choose);
        mTimePicker.setIs24HourView(true);
        pay = view.findViewById(R.id.pay);
        back=view.findViewById(R.id.pay_back_ImageView);
    }

    private void initClicks() {
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Date date = new Date();
                time_show.setText("" + hourOfDay + ":" + minute);
                Timestamp timestamp = new Timestamp(new Date().getTime());
                int hour = timestamp.getHours();
                int min = timestamp.getMinutes();
                int gas = (hourOfDay - hour) * 60 * 60 * 1000 + (minute - min) * 60 * 1000;
                double money = gas * (1.0 / 3600000.0);
                money1 = (double) Math.round(money * 100) / 100;
                pay_money.setText(money1 + "元");

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentManager.findFragmentByTag("fragment_pay") != null) {
                    fragmentManager.beginTransaction()
                            .remove(fragmentManager.findFragmentByTag("fragment_pay"))
                            .show(fragmentManager.findFragmentByTag("into"))
                            .commit();
                }
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<Order> orderBmobQuery=new BmobQuery<>();
                orderBmobQuery.findObjects(new FindListener<Order>() {
                    @Override
                    public void done(List<Order> list, BmobException e) {
                        //秘钥验证的类型 true:RSA2 false:RSA
                        OrderInfoUtil2_0.money = money1;
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
                });
            }
        });
    }
}
