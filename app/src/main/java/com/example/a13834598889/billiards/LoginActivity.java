package com.example.a13834598889.billiards;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.a13834598889.billiards.Opengles.BallObject;
import com.example.a13834598889.billiards.Opengles.LoadUtil;
import com.example.a13834598889.billiards.Opengles.LoadedObjectVertexNormal;
import com.example.a13834598889.billiards.Opengles.MySurfaceView;
import com.example.a13834598889.billiards.Tool.Adapter.SignInUpAdapter;
import com.example.a13834598889.billiards.Tool.App;
import com.flyco.tablayout.SlidingTabLayout;
//import com.example.a13834598889.lovepets.JavaBean.User;
//import com.netease.nimlib.sdk.NIMClient;
//import com.netease.nimlib.sdk.RequestCallback;
//import com.netease.nimlib.sdk.auth.AuthService;
//import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;


public class LoginActivity extends AppCompatActivity {



    public static float[] datas1;
    public static float[] datas2;

    public static float[] datas3;
    public static float[] datas4;


    public static MySurfaceView mGLSurfaceView;

    private String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<String> mPermissionList = new ArrayList<>();
    private final int mRequestCode = 100;

    private SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //得到当前界面的装饰视图
        View decorView = getWindow().getDecorView();
//        SYSTEM_UI_FLAG_FULLSCREEN表示全屏的意思，也就是会将状态栏隐藏
        //设置系统UI元素的可见性
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.fragment_sign_in_up);
        BmobConfig config = new BmobConfig.Builder(this)
                .setApplicationId("fef642bee9678388a478d8b5b25bafa0")
                .setConnectTimeout(30)
                .setUploadBlockSize(1024 * 1024)
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
            initPermission();
        }

        mGLSurfaceView = new MySurfaceView(App.getContext(),new ArrayList<BallObject>());

        initData();
        initViews();
    }

    //权限判断和申请
    private void initPermission() {

        mPermissionList.clear();//清空没有通过的权限

        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        } else {
            Toast.makeText(this, "所有权限均通过", Toast.LENGTH_SHORT).show();
        }
    }

    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (!hasPermissionDismiss) {
                Toast.makeText(this, "权限申请完成", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "存在权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onRestart() {
        initViews();
        super.onRestart();
        initViews();
    }

    @Override
    protected void onResume() {
        initViews();
        super.onResume();
        initViews();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    public void initViews() {


        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager_container_kaikai);
        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_kaikai);

        SignInUpAdapter adapter = new SignInUpAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        slidingTabLayout.setViewPager(viewPager);
    }



    public static void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<float[]> dataList1 = LoadUtil.loadFromFileVertexOnlyAverage("final.obj", mGLSurfaceView.getResources());
                datas1 = dataList1.get(0);
                datas2 = dataList1.get(1);

                mGLSurfaceView.queueEvent(new Runnable() {       //  以下的所有的绘制的任务  必须在   GL的 线程中创建
                    @Override
                    public void run() {
                        MySurfaceView.lovo1 = new LoadedObjectVertexNormal(mGLSurfaceView,datas1,datas2);
                    }
                });

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<float[]> dataList2 = LoadUtil.loadFromFileVertexOnlyAverage("robot0.obj",mGLSurfaceView.getResources());
                datas3 = dataList2.get(0);
                datas4 = dataList2.get(1);
                mGLSurfaceView.queueEvent(new Runnable() {       //  以下的所有的绘制的任务  必须在   GL的 线程中创建
                    @Override
                    public void run() {
                        mGLSurfaceView.lovo2 = new LoadedObjectVertexNormal(mGLSurfaceView,datas3,datas4);
                    }
                });
            }
        }).start();


    }



}
