package com.example.a13834598889.billiards.FragmentShopKepperMine.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.example.a13834598889.billiards.JavaBean.ShopKeeper;
import com.example.a13834598889.billiards.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;
import static cn.bmob.v3.Bmob.getApplicationContext;

public class FragmentShopLocation extends Fragment {

    private CircleImageView locationBack;
    private TextView locationBmobMessage;
    private TextView locationBmobLongitude;
    private TextView locationBmobLatitude;
    private TextView locationNowMessage;
    private TextView locationNowLongitude;
    private TextView locationNowLatitude;
    private Button okButton;

    private Fragment fragmentTest;
    private FragmentManager fragmentManager;

    private String Latitude;
    private String Longitude;
    private String location;

    public static FragmentShopLocation newInstance() {
        return new FragmentShopLocation();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_location, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        initViews(view);
        initInitialize();
        initClicks();
        return view;
    }

    private void initClicks() {
        locationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTest = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .remove(fragmentTest)
                        .show(fragmentManager.findFragmentByTag("shop_fragment_mine"))
                        .commit();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commitMessage = locationNowMessage.getText().toString();
                String commitLongitude = locationNowLatitude.getText().toString() + "," + locationNowLongitude.getText().toString();
                Log.e(TAG, "onClick: " + commitLongitude + " " + commitMessage);
                //提交到bmob
                ShopKeeper shopKeeper = new ShopKeeper();
                shopKeeper.setStore(true);
                shopKeeper.setLocation(commitMessage);
                shopKeeper.setLatlng(commitLongitude);
                shopKeeper.setObjectId(ShopKeeper.getCurrentUser().getObjectId());
                shopKeeper.update(ShopKeeper.getCurrentUser().getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            fragmentTest = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .remove(fragmentTest)
                                    .add(R.id.fragment_container, FragmentShopLocation.newInstance(), "shop_keeper_mine_store_location")
                                    .commit();
                        } else {
                            Log.e(TAG, "done: 位置信息界面，提交bmob失败" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void initInitialize() {
        BmobQuery<ShopKeeper> userBmobQuery = new BmobQuery<>();
        userBmobQuery.getObject(ShopKeeper.getCurrentUser().getObjectId(), new QueryListener<ShopKeeper>() {
            @Override
            public void done(ShopKeeper shopKeeper, BmobException e) {
                if (e == null) {
                    String s[] = shopKeeper.getLatlng().split(",");
                    final String latitude = s[0];
                    final String longitude = s[1];
                    locationBmobMessage.setText(shopKeeper.getLocation());
                    locationBmobLongitude.setText(latitude);
                    locationBmobLatitude.setText(longitude);
                    setNowMessage();
                } else {
                    Log.e(TAG, "done: 修改位置界面，获取店铺User出错 " + e.getMessage());
                }
            }
        });
    }

    private void setNowMessage() {
        LocationClient locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener2());
        locationClient.start();
    }

    private void initViews(View view) {
        locationBack = view.findViewById(R.id.shop_location_back);
        locationBmobMessage = view.findViewById(R.id.shop_location_bmob);
        locationBmobLongitude = view.findViewById(R.id.shop_location_bmob_longitude);
        locationBmobLatitude = view.findViewById(R.id.shop_location_bmob_latitude);
        locationNowMessage = view.findViewById(R.id.shop_location_now);
        locationNowLongitude = view.findViewById(R.id.shop_location_now_longitude);
        locationNowLatitude = view.findViewById(R.id.shop_location_now_latitude);
        okButton = view.findViewById(R.id.shop_location_change_Button);
    }

    public class MyLocationListener2 implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Longitude = String.valueOf(bdLocation.getLongitude());
            Latitude = String.valueOf(bdLocation.getLatitude());
            locationNowLongitude.setText(Longitude);
            locationNowLatitude.setText(Latitude);
            Log.e(TAG, "onReceiveLocation: " + Latitude + " " + Longitude);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + Latitude + "," + Longitude + "&type=100";
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
                        Log.e(Constraints.TAG, "onReceiveLocation: " + "error in wapaction,and e is " + e.getMessage());
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                Log.e(Constraints.TAG, "run: IOException", e);
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
                    locationNowMessage.post(new Runnable() {
                        @Override
                        public void run() {
                            locationNowMessage.setText(location);
                        }
                    });
                }
            }).start();
        }
    }
}
