package com.example.a13834598889.billiards.FragmentCustomerOrder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.example.a13834598889.billiards.JavaBean.BilliardStore;
import com.example.a13834598889.billiards.JavaBean.ShopKeeper;
import com.example.a13834598889.billiards.JavaBean.User;
import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.PoiOverlay;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.db.MigrationHelper;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.volley.VolleyLog.TAG;

/**
 * Created by Administrator on 2018/10/8.
 */

public class Fragment_map extends Fragment implements SensorEventListener,
        OnGetGeoCoderResultListener,
        OnGetPoiSearchResultListener,
        OnGetSuggestionResultListener {


    public static Fragment_map newInstance(){
        Fragment_map fragment_map = new Fragment_map();
        return fragment_map;
    }

    private EditText city;
    private TextView nickname;
    private TextView count;
    private Button bt_into;
    private Button mLocation;
    private AutoCompleteTextView key;
    private Button mSearch;
    private LinearLayout mLinearLayout;
    private TextView dis;

    //定位
    private TextureMapView mMapView = null;
    private BaiduMap mBaiduMap;
    private float mCurrentAccracy;
    private BitmapDescriptor mCurrentMark;
    private MyLocationConfiguration.LocationMode mLocationMode;
    private LocationClient mLocationClient;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    boolean isFirstLoc = true;
    private PoiSearch mPoiSearch;
    private GeoCoder mGeoCoder = null;
    private PoiSearch poiSearch = null;
    private SuggestionSearch suggestionSearch = null;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private MyLocationData locData;
    private List<String> suggest;
    private ArrayAdapter<String> sugAdapter = null;
    private int searchType = 0;
    private LatLng center = new LatLng(39.92235, 116.380338);
    private int radius = 500;
    private LatLng southwest = new LatLng(39.92235, 116.380338);
    private LatLng northeast = new LatLng(39.947246, 116.414977);
    private LatLngBounds searchbound = new LatLngBounds.Builder().include(southwest).include(northeast).build();

    public static String shopID;
    public static Integer integer;//客流量
    public static String shopNickName;

    private PopupWindow popupWindow = null;
    private LinearLayout pop;
    View view1;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.fragment_order, container, false);
        SDKInitializer.initialize(getActivity().getApplicationContext());
        initViews(view1);
        addMark();
        initClicks();
        suggestSearch();
        loadingPhoto();
        return view1;
    }

    //TODO: 设置界面修改头像之后刷新本界面UI
    public static void changeOrderPhoto(Bitmap bitmap){

    }

    //TODO: 头像
    public void loadingPhoto() {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getObjectId().equals(User.getCurrentUser().getObjectId())) {
                            if (list.get(i).getPicture_head() != null) {
                                list.get(i).getPicture_head().download(new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                                            Log.e(TAG, "done: fragment_order 头像下载成功" );
                                        } else {
                                            Log.e(MigrationHelper.TAG, "done: " + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onProgress(Integer integer, long l) {

                                    }
                                });
                            } else {
                            }
                        }
                    }
                } else {
                    Log.e(MigrationHelper.TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    private void initClicks() {
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));
            }
        });
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGeoCoder.geocode(new GeoCodeOption().city(city.getText().toString()).address(key.getText().toString()));
            }
        });
    }

    private void initViews(View view) {
        key = view.findViewById(R.id.geocodekey);
        city = view.findViewById(R.id.city);
        nickname = view.findViewById(R.id.name);
        count = view.findViewById(R.id.keliu);
        bt_into = view.findViewById(R.id.into);
        mLocation = view.findViewById(R.id.my_location);
        mLinearLayout = view.findViewById(R.id.info);
        mSearch = view.findViewById(R.id.search);
        dis = view.findViewById(R.id.juli);
        mLinearLayout.setVisibility(View.GONE);
        baiduLocation(view);
    }

    private void suggestSearch() {
        //初始化建议搜索模块，注册建议搜索事件监听
        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(this);
        sugAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line);
        key.setAdapter(sugAdapter);
        key.setThreshold(1);
        key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() <= 0) {
                    return;
                }
                suggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(s.toString()).city(city.getText().toString()));
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void baiduLocation(View view) {
        mMapView = view.findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
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
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.overlook(0);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        });

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1);
        mLocationClient.setLocOption(option);
        mLocationClient.start();


        //地理编码
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);
        //poi
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
    }

    private void addMark() {
        BmobQuery<ShopKeeper> query = new BmobQuery<>();
        query.setLimit(30);
        query.findObjects(new FindListener<ShopKeeper>() {
            @Override
            public void done(List<ShopKeeper> list, BmobException e) {
                if (e == null) {
                    List<String> latlngList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStore()) {
                            latlngList.add(list.get(i).getLatlng());
                        }
                    }
                    Log.e(TAG, "done: 共有店家 " + latlngList);
                    Toast.makeText(getActivity(), "共有" + latlngList.size() + "家店铺上传了位置", Toast.LENGTH_SHORT).show();
                    initOverlay(latlngList);
                } else {
                    Log.e(TAG, "done: " + e.getMessage());
                }
            }
        });
    }

    public void initOverlay(List<String> latlngList) {
        for (int i = 0; i < latlngList.size(); i++) {
            String s[] = latlngList.get(i).split(",");
            final String latitude = s[0];
            final String longitude = s[1];
            LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            OverlayOptions options = new MarkerOptions()
                    .position(latLng)
                    .icon(bitmapDescriptor)
                    .draggable(true)
                    .zIndex(9);
            mBaiduMap.addOverlay(options);
        }
        baiduMarkerClicks();
    }

    private void baiduMarkerClicks() {
        BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                showPop();
                mLinearLayout.setVisibility(View.VISIBLE);
                loadShopMessage(marker);
                mLocationClient = new LocationClient(getActivity());
                mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        LatLng l1 = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                        LatLng l2 = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                        double distance = ((int) (DistanceUtil.getDistance(l1, l2) * 100)) / 100;
                        dis.setText("" + String.valueOf(distance) + "米");
                    }
                });

                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true);
                option.setCoorType("bd09ll");
                option.setScanSpan(1);
                mLocationClient.setLocOption(option);
                mLocationClient.start();
                return false;
            }
        };
        mBaiduMap.setOnMarkerClickListener(markerClickListener);
    }


    private void showPop(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow, null);
        setPopView(view);



        if (popupWindow == null){
            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view1.findViewById(R.id.info), Gravity.BOTTOM, 0 , 0);
        popupWindow.setAnimationStyle(R.anim.in);


        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.6f;
        getActivity().getWindow().setAttributes(params);

        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                popupWindow.setAnimationStyle(R.anim.out);
            }
        });

        bt_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .addToBackStack(null)  //返回
                        .add(R.id.fragment_container, Fragment_billiards.newInstance(shopID, integer, shopNickName), "into")
                        .commit();
            }
        });


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                popupWindow = null;
                WindowManager.LayoutParams params1 = getActivity().getWindow().getAttributes();
                params1.alpha = 1f;
                getActivity().getWindow().setAttributes(params1);
            }
        });

    }

    private void setPopView(View view){
        pop = view.findViewById(R.id.pop);
        nickname = view.findViewById(R.id.name);
        count = view.findViewById(R.id.keliu);
        dis = view.findViewById(R.id.juli);
        bt_into = view.findViewById(R.id.into);

    }

    private void loadShopMessage(Marker marker) {
        final String latlng = marker.getPosition().latitude + "," + marker.getPosition().longitude;
        BmobQuery<ShopKeeper> shopKeeperBmobQuery = new BmobQuery<>();
        shopKeeperBmobQuery.findObjects(new FindListener<ShopKeeper>() {
            @Override
            public void done(List<ShopKeeper> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getStore() && list.get(i).getLatlng().equals(latlng)) {
                            nickname.setText("" + list.get(i).getNickName());
                            shopNickName = list.get(i).getNickName();
                            shopID = list.get(i).getObjectId();
                            //客流量
                            BmobQuery<BilliardStore> billiardStoreBmobQuery = new BmobQuery<>();
                            billiardStoreBmobQuery.findObjects(new FindListener<BilliardStore>() {
                                @Override
                                public void done(List<BilliardStore> list, BmobException e) {
                                    if (e == null) {
                                        for (int j = 0; j < list.size(); j++) {
                                            String temp = list.get(j).getStoreID();
                                            temp = temp.replace("\t", "");
                                            if (temp.equals(shopID)) {
                                                count.setText("" + list.get(j).getNum_customer());
                                                integer = list.get(j).getNum_customer();
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
        mLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        mMapView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(geoCodeResult.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(geoCodeResult.getLocation()));
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(reverseGeoCodeResult.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(reverseGeoCodeResult.getLocation()));
        Toast.makeText(getActivity(), reverseGeoCodeResult.getAddress() + " adcode: " + reverseGeoCodeResult.getAdcode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(poiResult);
            overlay.addToMap();
            overlay.zoomToSpan();
            switch (searchType) {
                case 2:
                    shpeNearbyArea(center, radius);
                    break;
                case 3:
                    showBound(searchbound);
                    break;
                default:
                    break;
            }
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            String info = "在";
            for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                info += cityInfo.city;
                info += ",";
            }
            info += "找到结果";
            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), poiDetailResult.getName() + ":" + poiDetailResult.getAddress(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<>();
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
            sugAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, suggest);
            key.setAdapter(sugAdapter);
            sugAdapter.notifyDataSetChanged();
        }
    }

    public void shpeNearbyArea(LatLng center, int radius) {
        BitmapDescriptor centerBitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        MarkerOptions markerOptions = new MarkerOptions().position(center).icon(centerBitmap);
        mBaiduMap.addOverlay(markerOptions);
        OverlayOptions options = new CircleOptions().fillColor(0xCCCCCC00)
                .center(center).stroke(new Stroke(5, 0xFFFF00FF))
                .radius(radius);
        mBaiduMap.addOverlay(options);
    }

    public void showBound(LatLngBounds bounds) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        OverlayOptions options = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bitmapDescriptor).transparency(0.8f);
        mBaiduMap.addOverlay(options);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(update);
        bitmapDescriptor.recycle();
    }

    private class MyPoiOverlay extends PoiOverlay {

        /**
         * 构造函数
         *
         * @param baiduMap 该 PoiOverlay 引用的 BaiduMap 对象
         */
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int i) {
            super.onPoiClick(i);
            PoiInfo poiInfo = getPoiResult().getAllPoi().get(i);
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poiInfo.uid));
            return true;
        }
    }
}
