package com.example.a13834598889.billiards.Opengles;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a13834598889.billiards.FragmentCustomerTeach.Fragment_teach;
import com.example.a13834598889.billiards.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

import abak.tr.com.boxedverticalseekbar.BoxedVertical;
import cn.bmob.v3.b.V;

import static com.example.a13834598889.billiards.LoginActivity.mGLSurfaceView;

public class OpenglesA extends AppCompatActivity {


    public static SweetAlertDialog cantFight;


    static float screenWidth;//屏幕宽度
    static float screenHeight;//屏幕高度

    public static ArrayList<ImageView> imageViews = new ArrayList<>();


    public ImageView imageView1;
    public ImageView imageView2;
    public ImageView imageView3;
    public ImageView imageView4;
    public ImageView imageView5;
    public ImageView imageView6;
    public ImageView imageView7;
    public ImageView imageView8;


    public LinearLayout linearLayout_caozuo1;
    public RelativeLayout relativeLayout_caozuo2;
    public RelativeLayout relativeLayout_caozuo3;




    public static int ballNum = 0;
    public static int bagNum = 0;

    private BoxedVertical boxedVertical ;

    public static TextView textView_ballNum;
    public static TextView textView_bagNum;

    public FloatingActionButton button_show;
    public FloatingActionButton button_new;
    public FloatingActionButton button_photo;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //获得系统的宽度以及高度
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth=dm.widthPixels;
        screenHeight=dm.heightPixels;
        setContentView(R.layout.activity_openglesa);

        linearLayout_caozuo1 = findViewById(R.id.caozuo_jiemian1);
        relativeLayout_caozuo2 = findViewById(R.id.caozuo_jiemian2);
        relativeLayout_caozuo3 = findViewById(R.id.caozuo_jiemian3);

        if(Fragment_teach.flagChoose == 1){
            linearLayout_caozuo1.setVisibility(View.VISIBLE);
            relativeLayout_caozuo2.setVisibility(View.VISIBLE);
            relativeLayout_caozuo3.setVisibility(View.VISIBLE);
        }else if(Fragment_teach.flagChoose == 2){
            linearLayout_caozuo1.setVisibility(View.INVISIBLE);
            relativeLayout_caozuo2.setVisibility(View.INVISIBLE);
            relativeLayout_caozuo3.setVisibility(View.VISIBLE);
        }




        if(Fragment_teach.flagChoose == 1){

            Fragment_teach.ballObjects.add(new BallObject(37,8,65,0));
            Fragment_teach.ballObjects.add(new BallObject(0,8,-100,1));
            Fragment_teach.ballObjects.add(new BallObject(0,8,0,2));
            Fragment_teach.ballObjects.add(new BallObject(0,8,100,3));






            cantFight = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("温馨提示")
                    .setContentText("无法击打到目标球 ！")
                    .setConfirmText("Ok !");

            imageView1 =(ImageView) findViewById(R.id.ball_picture1);
            imageView2 =(ImageView) findViewById(R.id.ball_picture2);
            imageView3 =(ImageView)findViewById(R.id.ball_picture3);
            imageView4 =(ImageView) findViewById(R.id.ball_picture4);
            imageView5 =(ImageView) findViewById(R.id.ball_picture5);
            imageView6 =(ImageView) findViewById(R.id.ball_picture6);
            imageView7 =(ImageView)findViewById(R.id.ball_picture7);
            imageView8 =(ImageView)findViewById(R.id.ball_picture8);
            imageViews.add(imageView1);        imageViews.add(imageView2);        imageViews.add(imageView3);
            imageViews.add(imageView4);        imageViews.add(imageView5);        imageViews.add(imageView6);
            imageViews.add(imageView7);        imageViews.add(imageView8);





            textView_ballNum = (TextView)findViewById(R.id.textView_ballNum);
            textView_bagNum = (TextView)findViewById(R.id.textView_bagNum);

            button_show = (FloatingActionButton)findViewById(R.id.button_show);
            button_new = (FloatingActionButton)findViewById(R.id.button_new);
            button_photo = (FloatingActionButton)findViewById(R.id.button_photo);

            boxedVertical = (BoxedVertical)findViewById(R.id.boxed_vertical);


            boxedVertical.setOnBoxedPointsChangeListener(new BoxedVertical.OnValuesChangeListener() {
                @Override
                public void onPointsChanged(BoxedVertical boxedPoints, final int value) {

                    if(Fragment_teach.flagChoose == 1){
                        mGLSurfaceView.currSightDis = 130+value;
                        mGLSurfaceView.setCameraPostion();
                    }else if(Fragment_teach.flagChoose == 2){
                        mGLSurfaceView.currSightDis = 50+value;
                        mGLSurfaceView.setCameraPostion();
                    }

                }

                @Override
                public void onStartTrackingTouch(BoxedVertical boxedPoints) {

                }

                @Override
                public void onStopTrackingTouch(BoxedVertical boxedPoints) {

                }
            });


            button_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(TouchableObject.armBall == null || TouchableObject.armAt == null){
                        new SweetAlertDialog(OpenglesA.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("温馨提示")
                                .setContentText("请完成目标球以及目标袋的选取 !")
                                .setConfirmText("ok")
                                .show();
                    }else if(TouchableObject.armBall != null || TouchableObject.armAt != null){

                        Xpoint xpoint_mu_start = new Xpoint(MySurfaceView.balls.get(0).z,MySurfaceView.balls.get(0).x);
                        Xpoint xpoint_mu_end = new Xpoint(MySurfaceView.ballFinal.z,MySurfaceView.ballFinal.x);


                        Xpoint xpoint_biao_start = new Xpoint(TouchableObject.armBall.z,TouchableObject.armBall.x);
                        Xpoint xpoint_biao_end = new Xpoint(TouchableObject.armAt.z,TouchableObject.armAt.x);


                        Xpoint[] xpoint_muqiu_Start = TouchableObject.getPoints(xpoint_mu_start,xpoint_mu_end,100);
                        Xpoint[] xpoint_mubiaoqiu_End = TouchableObject.getPoints(xpoint_biao_start,xpoint_biao_end,100);
//                    Xpoint[] xpoint_muqiu_End = TouchableObject.getPoints();




                        for (Xpoint point1:xpoint_muqiu_Start
                                ) {
                            MySurfaceView.balls.get(0).x = point1.y;
                            MySurfaceView.balls.get(0).z = point1.x;
                            try{
                                Thread.sleep(10);
                            }catch (Exception e){

                            }
                        }


                        Ball armBall = MySurfaceView.balls.get(0);

                        for (Ball ball:MySurfaceView.balls) {

                            if(ball.number == TouchableObject.armBall.number){
                                armBall = ball;
                                break;
                            }
                        }


                        for (Xpoint point1:xpoint_mubiaoqiu_End
                                ) {
                            armBall.z = point1.x;
                            armBall.x = point1.y;
                            try{
                                Thread.sleep(10);
                            }catch (Exception e){

                            }
                        }



                    }



                }
            });


            button_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    mGLSurfaceView.cx = -230;
                    mGLSurfaceView.cy = 90;
                    mGLSurfaceView.cz = 0;

                    mGLSurfaceView.currSightDis = 230;
                    mGLSurfaceView.angdegElevation = 60;
                    mGLSurfaceView.angdegAzimuth = 90;

                    mGLSurfaceView.tx = 0;
                    mGLSurfaceView.ty = 0;
                    mGLSurfaceView.tz = 0;

                    mGLSurfaceView.point = new PointA(new float[1],new float[1]);

                    MySurfaceView.toruses.clear();

                    mGLSurfaceView.setCameraPostion();

                    MySurfaceView.ballFinal = null;
                    TouchableObject.armAt = null;
                    TouchableObject.armBall = null;


                    new SweetAlertDialog(OpenglesA.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("观察视角已重新修正")
                            .show();

                }
            });

            initBallPicture();
            textView_bagNum.setText(0+"");
            textView_ballNum.setText(0+"");
        }




        mGLSurfaceView = new MySurfaceView(OpenglesA.this,Fragment_teach.ballObjects);
        //将自定义的SurfaceView添加到外层LinearLayout中
        LinearLayout ll=(LinearLayout) findViewById(R.id.main_linerk1);
        ll.addView(mGLSurfaceView);
        mGLSurfaceView.requestFocus();// 获取焦点
        mGLSurfaceView.setPreserveEGLContextOnPause(true);
        mGLSurfaceView.setFocusableInTouchMode(true);// 设置为可触控
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


    }






    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }


    public boolean onKeyDown(int keyCode,KeyEvent e)
    {
        switch(keyCode)
        {
            case 4:
                System.exit(0);
                break;
        }
        return true;
    }




    public void initBallPicture(){
        int i = 0;

        int first = Fragment_teach.ballObjects.get(1).texID;

        if (first <=8){
            for (BallObject ballObject:Fragment_teach.ballObjects){

                switch (ballObject.texID){
                    case 1:
                        imageViews.get(i-1).setImageResource(R.drawable.b1);
                        break;
                    case 2:
                        imageViews.get(i-1).setImageResource(R.drawable.b2);
                        break;
                    case 3:
                        imageViews.get(i-1).setImageResource(R.drawable.b3);
                        break;
                    case 4:
                        imageViews.get(i-1).setImageResource(R.drawable.b4);
                        break;
                    case 5:
                        imageViews.get(i-1).setImageResource(R.drawable.b5);
                        break;
                    case 6:
                        imageViews.get(i-1).setImageResource(R.drawable.b6);
                        break;
                    case 7:
                        imageViews.get(i-1).setImageResource(R.drawable.b7);
                        break;
                    case 8:
                        imageViews.get(i-1).setImageResource(R.drawable.b8);
                        break;
                    default:
                        break;
                }
                i++;
            }
        }else{
            for (BallObject ballObject:Fragment_teach.ballObjects){

                switch (ballObject.texID){
                    case 8:
                        imageViews.get(i-1).setImageResource(R.drawable.b8);
                        break;
                    case 9:
                        imageViews.get(i-1).setImageResource(R.drawable.b9);
                        break;
                    case 10:
                        imageViews.get(i-1).setImageResource(R.drawable.b10);
                        break;
                    case 11:
                        imageViews.get(i-1).setImageResource(R.drawable.b11);
                        break;
                    case 12:
                        imageViews.get(i-1).setImageResource(R.drawable.b12);
                        break;
                    case 13:
                        imageViews.get(i-1).setImageResource(R.drawable.b13);
                        break;
                    case 14:
                        imageViews.get(i-1).setImageResource(R.drawable.b14);
                        break;
                    case 15:
                        imageViews.get(i-1).setImageResource(R.drawable.b15);
                        break;
                    default:
                          break;
                }
                i++;
            }
        }

    }

}
