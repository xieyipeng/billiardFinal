

package com.example.a13834598889.billiards.Opengles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import com.example.a13834598889.billiards.R;
import com.example.a13834598889.billiards.Tool.App;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.example.a13834598889.billiards.FragmentCustomerTeach.Fragment_teach.flagChoose;


public class MySurfaceView extends GLSurfaceView
{




    private static boolean flag = false;
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器

    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标
    //关于摄像机的变量
    public static float cx=-230;//摄像机x位置
    public static float cy=90;//摄像机y位置
    public static float cz=0;//摄像机z位置

    public float tx=0;//目标点x位置
    public float ty=0;//目标点y位置
    public float tz=0;//目标点z位置
    public float currSightDis=230;//摄像机和目标的距离
    public float angdegElevation=60;//仰角
    public float angdegAzimuth=90;//方位角
    float left;
    float right;
    float top;
    float bottom;
    float near;
    float far;

    int count;

    int textureIdFire;//系统火焰分配的纹理id

    public static ArrayList<Ball> balls = new ArrayList<>();
    public ArrayList<BallObject> ballObjects = new ArrayList<>();

    public static ArrayList<Torus> toruses = new ArrayList<>();


    public static PointA point;



    //可触控物体列表
    public static ArrayList<TouchableObject> lovnList=new ArrayList<>();
    //被选中物体的索引值，即id，没有被选中时索引值为-1
    int checkedIndex=-1;
    public MySurfaceView(Context context,ArrayList<BallObject> ballObjects) {
        super(context);
        this.ballObjects = ballObjects;
        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0


        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }

    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //计算仿射变换后AB两点的位置
                float[] AB=IntersectantUtil.calculateABPosition
                        (
                                x, //触控点X坐标
                                y, //触控点Y坐标
                                OpenglesA.screenWidth, //屏幕宽度
                                OpenglesA.screenHeight, //屏幕长度
                                left, //视角left、top值
                                top,
                                near, //视角near、far值
                                far
                        );
                //射线AB
                Vector3f start = new Vector3f(AB[0], AB[1], AB[2]);//起点
                Vector3f end = new Vector3f(AB[3], AB[4], AB[5]);//终点
                Vector3f dir = end.minus(start);//长度和方向
			/*
			 * 计算AB线段与每个物体包围盒的最佳交点(与A点最近的交点)，
			 * 并记录有最佳交点的物体在列表中的索引值
			 */
                //记录列表中时间最小的索引值
                checkedIndex = -1;//标记为没有选中任何物体
                int tmpIndex=-1;//记录与A点最近物体索引的临时值
                float minTime=1;//记录列表中所有物体与AB相交的最短时间
                for(int i=0;i<lovnList.size();i++){//遍历列表中的物体
                    AABB3 box = lovnList.get(i).getCurrBox(); //获得物体AABB包围盒
                    float t = box.rayIntersect(start, dir, null);//计算相交时间
                    if (t <= minTime) {
                        minTime = t;//记录最小值
                        tmpIndex = i;//记录最小值索引
                    }
                }
                checkedIndex=tmpIndex;//将索引保存在checkedIndex中
                changeObj(checkedIndex);//改变被选中物体

                System.out.println(" checkedIndex ：&&&&&&&&&&&&&&&&&&&&&&&&&&          "   +   checkedIndex);

                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//计算触控笔Y位移
                float dx = x - mPreviousX;//计算触控笔X位移
                //不超过阈值不移动摄像机
                if(Math.abs(dx)<0.5f && Math.abs(dy)<0.5f){
                    break;
                }
                angdegAzimuth += dx * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度
                angdegElevation += dy * TOUCH_SCALE_FACTOR;//设置沿z轴旋转角度
                //将仰角限制在5～90度范围内
                angdegElevation = Math.max(angdegElevation, 5);
                angdegElevation = Math.min(angdegElevation, 90);
                //设置摄像机的位置


                setCameraPostion();
                break;
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置

        return true;
    }
    //设置摄像机位置的方法
    public void setCameraPostion() {
        //计算摄像机的位置
        double angradElevation = Math.toRadians(angdegElevation);//仰角（弧度）
        double angradAzimuth = Math.toRadians(angdegAzimuth);//方位角
        cx = (float) (tx - currSightDis * Math.cos(angradElevation)	* Math.sin(angradAzimuth));
        cy = (float) (ty + currSightDis * Math.sin(angradElevation));
        cz = (float) (tz - currSightDis * Math.cos(angradElevation) * Math.cos(angradAzimuth));
    }
    //改变列表中下标为index的物体
    public void changeObj(int index){
        if(index != -1){//如果有物体被选中
            for(int i=0;i<lovnList.size();i++){
                if(i>6){   // 选中的是球
                    if(i==index){//改变选中的物体
                        lovnList.get(i).changeOnTouch(true,lovnList.get(i));
                    }
                    else{//恢复其他物体
                        lovnList.get(i).changeOnTouch(false,lovnList.get(i));
                    }
                }else{   //选中的是球袋 或者是母球
                    if(i>6){
                        lovnList.get(i).changeOnTouch(false,lovnList.get(i));
                    }
                }
            }
            TouchableObject.armTarget(lovnList.get(index),index);
        }
        else{//如果没有物体被选中
            for (int i = 7;i<lovnList.size();i++){
                lovnList.get(i).changeOnTouch(false,lovnList.get(i));
            }
        }
    }


    int angle = 0;


    public static ArrayList<TextureRect> baskets = new ArrayList<>();

    public static LoadedObjectVertexNormal lovo1;
    public static LoadedObjectVertexNormal lovo2;

    public static boolean hasChoose = false;

    public static Ball ballFinal;



    private class SceneRenderer implements Renderer
    {




        public void onDrawFrame(GL10 gl)
        {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            MatrixState.setCamera(cx, cy, cz, tx, ty, tz, 0, 1, 0);
            //初始化光源位置
            MatrixState.setLightLocation(0, 170, 0);


            if(flagChoose == 1){
                if(ballFinal!=null){
                    MatrixState.pushMatrix();
                    ballFinal.drawSelf();
                    MatrixState.popMatrix();
                }


                for(Ball ball:balls){
                    MatrixState.pushMatrix();
                    ball.drawSelf();
                    MatrixState.popMatrix();
                }

                for (Torus torus:toruses){
                    MatrixState.pushMatrix();
                    torus.drawSelf();
                    MatrixState.popMatrix();
                }





                MatrixState.pushMatrix();
                if(lovo1 !=null){
                    lovo1.drawSelf();
                }
                MatrixState.popMatrix();

                MatrixState.pushMatrix();
                point.drawSelf(20,GLES20.GL_LINES,point.vCounts);
                MatrixState.popMatrix();


                if(hasChoose){
                    for (TextureRect baskets:baskets){
                        MatrixState.pushMatrix();
                        baskets.drawSelf();
                        MatrixState.popMatrix();
                    }
                }

            }else if(flagChoose == 2){
                MatrixState.pushMatrix();
                if(lovo2 !=null){
                    lovo2.drawSelf();
                }
                MatrixState.popMatrix();
            }


        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            float ratio = (float) width / height;
            //调用此方法计算产生透视投影矩阵
            left=right=ratio;
            top=bottom=1;
            near=2;
            far=500;
            MatrixState.setProjectFrustum(-left, right, -bottom, top, near, far);
            //计算摄像机的位置
            setCameraPostion();


        }


        boolean flag1 = false;

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {


            //设置屏幕背景色RGBA
            GLES20.glClearColor(1,1,1,1.0f);
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//            textureIdFire=initTexture(R.drawable.fire);
            //关闭背面剪裁
            GLES20.glDisable(GLES20.GL_CULL_FACE);
            //初始化变换矩阵
            MatrixState.setInitStack();
            //加载要绘制的物体


            if (!flag1){

                flag1 = true;

                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");

                baskets.add(new TextureRect(MySurfaceView.this,1,1,100,14,-200,2.4f,initTexture(true, R.drawable.n1),1));
                baskets.add(new TextureRect(MySurfaceView.this,1,1,100,14,0,2.4f,initTexture(true,R.drawable.n2),2));
                baskets.add(new TextureRect(MySurfaceView.this,1,1,100,14,200,2.4f,initTexture(true,R.drawable.n3),3));
                baskets.add(new TextureRect(MySurfaceView.this,1,1,-100,14,-200,2.4f,initTexture(true,R.drawable.n4),4));
                baskets.add(new TextureRect(MySurfaceView.this,1,1,-100,14,0,2.4f,initTexture(true,R.drawable.n5),5));
                baskets.add(new TextureRect(MySurfaceView.this,1,1,-100,14,200,2.4f,initTexture(true,R.drawable.n6),6));


                creatBalls(ballObjects);
                addBallsAABB();
            }



            point = new PointA(new float[1],new float[1]);



        }
    }

    public void creatBalls(ArrayList<BallObject> ballObjects){


        for (BallObject ballObject:ballObjects){

            switch (ballObject.texID){
                case 1:
                    Ball ball = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball1,1);
                    ball.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+1,ballObject.z,R.drawable.ball1);
                    balls.add(ball);
                    break;
                case 2:
                    Ball ball1 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball2,2);
                    ball1.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball2);
                    balls.add(ball1);
                    break;
                case 3:
                    Ball ball2 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball3,3);
                    ball2.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball3);
                    balls.add(ball2);
                    break;
                case 4:
                    Ball ball3 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball4,4);
                    ball3.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball4);
                    balls.add(ball3);
                    break;
                case 5:
                    Ball ball4 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball5,5);
                    ball4.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball5);
                    balls.add(ball4);
                    break;
                case 6:
                    Ball ball5 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball6,6);
                    ball5.torus= new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball6);
                    balls.add(ball5);
                    break;
                case 7:
                    Ball ball6 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball7,7);
                    ball6.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball7);
                    balls.add(ball6);
                    break;
                case 8:
                    Ball ball7 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball8,8);
                    ball7.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball8);
                    balls.add(ball7);
                    break;
                case 9:
                    Ball ball8 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball9,9);
                    ball8.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball9);
                    balls.add(ball8);
                    break;
                case 10:
                    Ball ball9 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball10,10);
                    ball9.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball10);
                    balls.add(ball9);
                    break;
                case 11:
                    Ball ball10 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball11,11);
                    ball10.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball11);
                    balls.add(ball10);
                    break;
                case 12:
                    Ball ball11 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball12,12);
                    ball11.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball12);
                    balls.add(ball11);
                    break;
                case 13:
                    Ball ball12 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball13,13);
                    ball12.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball13);
                    balls.add(ball12);
                    break;
                case 14:
                    Ball ball13 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball14,14);
                    ball13.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball14);
                    balls.add(ball13);
                    break;
                case 15:
                    Ball ball14 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball15,15);
                    ball14.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball15);
                    balls.add(ball14);
                    break;
                case 0:
                    Ball ball0 = new Ball(this,10f,ballObject.x,ballObject.y,ballObject.z,R.drawable.ball0,0);
                    ball0.torus = new Torus(this,12.8f, 12, 10, 30,ballObject.x,ballObject.y+0.5f,ballObject.z,R.drawable.ball15);
                    balls.add(ball0);
                    break;
                default:
                    break;

            }
        }

    }


    public void addBallsAABB(){

        if(balls.size() != 0){
            lovnList.add(balls.get(0));
        }

        for (TextureRect textureRect:baskets){
            lovnList.add(textureRect);
        }

        for(int i=0;i<balls.size();i++){
            Ball ball = balls.get(i);
            lovnList.add(ball);
        }

        System.out.println(" ==========================    现在的球的数 " + balls.size());
        System.out.println(" ==========================    现在的球袋的数 " + baskets.size());



    }



    //初始化纹理的方法
    public int initTexture(boolean isRepeat,int drawId)//textureId
    {
        //生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId=textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);


        if(isRepeat)
        {
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        }
        else
        {
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        }

        //通过输入流加载图片===============begin===================
        InputStream is = this.getResources().openRawResource(drawId);
        Bitmap bitmapTmp;
        try
        {
            bitmapTmp = BitmapFactory.decodeStream(is);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end=====================

        //实际加载纹理
        GLUtils.texImage2D
                (
                        GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                        0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
                        bitmapTmp, 			  //纹理图像
                        0					  //纹理边框尺寸
                );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        return textureId;
    }

}
