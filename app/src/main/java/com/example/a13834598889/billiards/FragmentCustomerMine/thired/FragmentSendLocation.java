package com.example.a13834598889.billiards.FragmentCustomerMine.thired;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.a13834598889.billiards.R;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.listener.MessageSendListener;

/**
 * Created by Administrator on 2018/9/19.
 */

public class FragmentSendLocation extends android.support.v4.app.Fragment {

    private ImageView back;
    FragmentManager mFragmentManager;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private BitmapDescriptor mCurrentMark;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    boolean isFirstLoc = true;
    private MyLocationConfiguration.LocationMode mLocationMode;
    private static  BmobIMConversation mBmobIMConversation;
    private static  MessageSendListener mListener;
    private Button send;
    private TextView myLocationText;
    private LocationClientOption mLocationClientOption;

    public static FragmentSendLocation newInstance(BmobIMConversation bmobIMConversation,  MessageSendListener listener){
        FragmentSendLocation fragmentSendLocation = new FragmentSendLocation();
        mBmobIMConversation = bmobIMConversation;
        mListener = listener;
        return fragmentSendLocation;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_location, container, false);
        SDKInitializer.initialize(getActivity().getApplicationContext());

        mFragmentManager = getActivity().getSupportFragmentManager();
        back = view.findViewById(R.id.fragment_card_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager.beginTransaction()
                        .remove(FragmentSendLocation.this)
                        .commit();
            }
        });
        mMapView = (MapView)view.findViewById(R.id.send_map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        myLocationText = view.findViewById(R.id.location);
        //定位到当前位置
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getActivity());

        mCurrentMark = BitmapDescriptorFactory.fromResource(R.drawable.mylocation);
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null || mMapView == null) {
                    return;
                }
                mCurrentLat = bdLocation.getLatitude();
                mCurrentLon = bdLocation.getLongitude();
                mCurrentAccracy = bdLocation.getRadius();
                MyLocationData locationData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        .direction(0).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locationData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
                mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
//                MyLocationConfiguration configuration = new MyLocationConfiguration
//                        (mLocationMode, true, mCurrentMark);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.overlook(0);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                StringBuilder myLocation = new StringBuilder();
                myLocation.append(bdLocation.getAddrStr());
                myLocationText.setText(myLocation);
            }
        });

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        send = view.findViewById(R.id.fragment_card_add);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BmobIMTextMessage message = new BmobIMTextMessage();
                message.setContent(myLocationText.getText().toString());
                Map<String, Object> map = new HashMap<>();
                map.put("level", "1");
                message.setExtraMap(map);
                message.setExtra("OK");
                mBmobIMConversation.sendMessage(message, mListener);
                mFragmentManager.beginTransaction()
                        .remove(FragmentSendLocation.this)
                        .commit();

            }
        });

        return view;
    }
}
