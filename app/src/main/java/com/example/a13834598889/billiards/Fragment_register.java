package com.example.a13834598889.billiards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.example.a13834598889.billiards.JavaBean.Customer;
import com.example.a13834598889.billiards.JavaBean.DianZan;
import com.example.a13834598889.billiards.JavaBean.ShopKeeper;
import com.example.a13834598889.billiards.JavaBean.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by xieyipeng on 2018/10/6.
 */

public class Fragment_register extends Fragment {


    private CheckBox checkBox_Shangjia;

    private EditText register_account;
    private EditText register_phone;
    private EditText register_emile;
    private EditText register_passWord;
    private EditText register_location;
    private Button register_button;


    private String Latitude;
    private String Longitude;
    private String location;

    private LinearLayout hintLinearLayout;
    private LocationClient locationClient;

    private View view;

    public static Fragment_register newInstance(){
        Fragment_register fragment_register = new Fragment_register();
        return fragment_register;
    }


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);


        locationClient = new LocationClient(getActivity());
        locationClient.registerLocationListener(new MyLocationListener2());
        locationClient.start();

        /**
         * 点赞
         */
        final DianZan dianZan = new DianZan();
        dianZan.setmDianZanShu(0);
        dianZan.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });

        initViews();
        initClicks();


        return view;
    }



    private void initClicks() {
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText("提示信息 ：")
                        .setContentText("注册后将使用注册账号登陆，且注册账号将不可修改。\n 是否确认注册 ？")
                        .setCancelText("Cancle")
                        .setConfirmText("Ok")
                        .showCancelButton(false)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if (register_account.getText().toString().equals("") || register_emile.
                                        getText().toString().equals("") || register_passWord.getText().toString
                                        ().equals("") || register_phone.getText().toString().equals("")) {
                                    Toast.makeText(getActivity(), "所有注册信息均不能为空", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else if (register_account.getText().toString().length() < 6 || register_passWord.getText().length() < 6) {
                                    Toast.makeText(getActivity(), "账号或密码长度不能小于六位", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    /**
                                     * 实现网上注册:
                                     */
                                    if (checkBox_Shangjia.isChecked()) {
                                        ShopKeeper shopKeeper = new ShopKeeper();
                                        shopKeeper.setStore(true);
                                        shopKeeper.setLatlng(Latitude + "," + Longitude);
                                        shopKeeper.setLocation(location);
                                        zhuce(shopKeeper);
                                        dialog.dismiss();

                                    } else {
                                        Customer customer = new Customer();
                                        customer.setStore(false);
                                        zhuce(customer);
                                        dialog.dismiss();
                                    }
                                }
                            }
                        })
                        .show();
                dialog.setCanceledOnTouchOutside(true);
            }
        });
    }

    private void zhuce(User user) {
        user.setUsername(register_account.getText().toString());
        user.setEmail(register_emile.getText().toString());
        user.setPassword(register_passWord.getText().toString());
        user.setMobilePhoneNumber(register_phone.getText().toString());
        user.setNickName("Century");
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(final User user, BmobException e) {
                if (e == null) {
                    /**
                     * 即时通讯
                     */
                    Toast.makeText(getActivity(), "注册成功" + user.getObjectId(), Toast.LENGTH_SHORT).show();


                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText("温馨提醒")
                            .setContentText("您已成功注册账号，请使用账号和密码进行登陆 ")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .show();

                } else {
                    Toast.makeText(getActivity(), "注册失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initViews() {


        checkBox_Shangjia = view.findViewById(R.id.checkbox_shangjia);
        hintLinearLayout = view.findViewById(R.id.register_hint_LinearLayout);
        register_account =  view.findViewById(R.id.register_name_editText);
        register_phone =  view.findViewById(R.id.register_phone_editText);
        register_emile =  view.findViewById(R.id.register_emile_editText);
        register_passWord =  view.findViewById(R.id.register_mima_editText);
        register_location = view.findViewById(R.id.register_location_editText);
        register_button =  view.findViewById(R.id.register_account_button);

        checkBox_Shangjia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    hintLinearLayout.setVisibility(View.VISIBLE);
                }else{
                    hintLinearLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    public class MyLocationListener2 implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Longitude = String.valueOf(bdLocation.getLongitude());
            Latitude = String.valueOf(bdLocation.getLatitude());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String urlString = "http://gc.ditu.aliyun.com/regeocoding?l="
                            + Latitude + "," + Longitude + "&type=100";
                    BufferedReader reader = null;
                    StringBuilder response = new StringBuilder();
                    HttpURLConnection conn = null;
                    try {
                        URL url = new URL(urlString);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(8000);
                        conn.setReadTimeout(8000);
                        InputStream inputStream = conn.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        inputStream.close();
                    } catch (Exception e) {
                        Log.e(TAG, "onReceiveLocation: " + "error in wapaction,and e is " + e.getMessage());
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                Log.e(TAG, "run: IOException", e);
                            }
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                    }

                    JsonParser parser = new JsonParser();
                    JsonObject object = (JsonObject) parser.parse(String.valueOf(response));
                    JsonArray add = object.get("addrList").getAsJsonArray();
                    JsonObject addList = add.get(0).getAsJsonObject();
                    location = addList.get("admName").getAsString() + "," + addList.get("name").getAsString();
                    for (int i = 0; i < location.length(); i++) {
                        if (location.charAt(i) == ',') {
                            location = location.substring(i + 1, location.length());
                            break;
                        }
                    }
                    register_location.post(new Runnable() {
                        @Override
                        public void run() {
                            register_location.setText(location);
                        }
                    });
                }
            }).start();
        }
    }


}