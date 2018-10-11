
package com.example.a13834598889.billiards.Opengles;

import com.example.a13834598889.billiards.R;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.a13834598889.billiards.LoginActivity.mGLSurfaceView;


/*
 * 可以被触控到的抽象类，
		 * 物体继承了该类可以被触控到
		 */
public abstract class TouchableObject {
	AABB3 preBox;//仿射变换之前的包围盒
	float[] m = new float[16];//仿射变换的矩阵
	//顶点颜色
	float[] color=new float[]{1,1,1,1};
	float size = 1.5f;;//放大的尺寸
	//获得中心点位置和长宽高的方法



	public static Ball armBall = null;
	public static TextureRect armAt = null;

	public static int currentArmNum = 0;

	public static Xpoint xpoint;




	public AABB3 getCurrBox(){
		return preBox.setToTransformedBox(m);//获取变换后的包围盒
	}

	public static void armTarget(TouchableObject touchThing,int dex){

		int a = 0 ;

		if(touchThing instanceof Ball){
			armBall = (Ball)touchThing;
			currentArmNum = 1;
			OpenglesA.textView_ballNum.setText(armBall.number+"");
			MySurfaceView.hasChoose = true;
		}

		if(touchThing instanceof TextureRect){
			if(currentArmNum == 1){
				currentArmNum ++; //当前被选中的目标数加1
			}
			if (currentArmNum == 2){





				armAt = (TextureRect)touchThing;


				Xpoint ballMu = new Xpoint(MySurfaceView.balls.get(0).z,MySurfaceView.balls.get(0).x);
				Xpoint ballArm = new Xpoint(armBall.z,armBall.x);

				ArrayList<Xpoint> ballData = new ArrayList<>();

				for (Ball ball:MySurfaceView.balls
					 ) {
					if(ball.number != 0 && ball.number != armBall.number){
						Xpoint point = new Xpoint(ball.z,ball.x);
						ballData.add(point);
					}
				}

				Boolean canFight = fightOk(ballMu,ballArm,10f,ballData);


				if (canFight){

					mGLSurfaceView.queueEvent(new Runnable() {       //  以下的所有的绘制的任务  必须在   GL的 线程中创建
						@Override
						public void run() {


							mGLSurfaceView.tx = TouchableObject.armBall.x;
							mGLSurfaceView.ty = TouchableObject.armBall.y;
							mGLSurfaceView.tz = TouchableObject.armBall.z;
							mGLSurfaceView.setCameraPostion();



							Xpoint x1 = new Xpoint(armAt.z,armAt.x);
							Xpoint x2 = new Xpoint(armBall.z,armBall.x);


							xpoint = getPoint(x1,x2,10f);


							float [] vertex = new float[12];
							float [] color = new float[16];

							vertex[0] = ((Ball)MySurfaceView.lovnList.get(0)).x;
							vertex[1] = ((Ball)MySurfaceView.lovnList.get(0)).y;
							vertex[2] = ((Ball)MySurfaceView.lovnList.get(0)).z;

							vertex[3] = xpoint.y;
							vertex[4] = armBall.y;
							vertex[5] = xpoint.x;

							vertex[6] = xpoint.y;
							vertex[7] = armBall.y;
							vertex[8] = xpoint.x;

							vertex[9] =armAt.x-6;
							vertex[10] = armAt.y-6;
							vertex[11] = armAt.z-6;


							color[0] = 0;
							color[1] = 1;
							color[2] = 0;
							color[3] = 0.8f;


							color[4] = 0;
							color[5] = 1;
							color[6] = 0;
							color[7] = 0.05f;


							color[8] = 0;
							color[9] = 0;
							color[10] = 1;
							color[11] = 0.8f;



							color[12] = 0;
							color[13] = 0;
							color[14] = 1;
							color[15] = 0.05f;





							PointA point = new PointA(vertex,color);
							point.vCounts = 4;
							MySurfaceView.point = point;   // 目标袋被选中   添加射线


							MySurfaceView.ballFinal = new Ball(mGLSurfaceView,10f,xpoint.y,armBall.y,xpoint.x, R.drawable.ball0,0);
						}
					});

					OpenglesA.textView_bagNum.setText(armAt.myNum+"");
					MySurfaceView.hasChoose = false;


				}else {
					OpenglesA.cantFight.show();
				}
			}
		}
	}


	//触控后的动作，根据需要要做相应改动
	public void changeOnTouch(boolean flag,TouchableObject touchThing){
		if(touchThing instanceof Ball){
			if (flag) {
				mGLSurfaceView.toruses.add(((Ball)touchThing).torus);
			} else {
				mGLSurfaceView.toruses.remove(((Ball)touchThing).torus);
				MySurfaceView.hasChoose = false;
			}
		}
	}
	//复制变换矩阵
	public void copyM(){
		for(int i=0;i<16;i++){
			m[i]=MatrixState.getMMatrix()[i];
		}
	}


	static public Xpoint getPoint(Xpoint p1,Xpoint p2,double dis){
		double x1=p1.x;
		double x2=p2.x;
		double y1=p1.y;
		double y2=p2.y;
		if(x1==x2&&y1==y2){
			return new Xpoint(99999,99999);
		}
		if(x1==x2){
			double y_1=y2+dis;
			double y_2=y2-dis;
			y_1=(y_1-y1>0)?(y_1-y1):-(y_1-y1);
			y_2=(y_2-y1>0)?(y_2-y1):-(y_2-y1);
			if(y_2<y_1)
				return new Xpoint(x1,y2+dis);
			else
				return new Xpoint(x1,y2-dis);

		}
		else if(y1==y2){
			double x_1=x2+dis;
			double x_2=x2-dis;
			x_1=(x_1-x1>0)?(x_1-x1):-(x_1-x1);
			x_2=(x_2-x1>0)?(x_2-x1):-(x_2-x1);
			if(x_2<x_1)
				return new Xpoint(x2+dis,y2);
			else
				return new Xpoint(x2-dis,y2);
		}else{
			double k=(y2-y1)/(x2-x1);
			double b=y1-k*x1;
			double x_1=x2+dis/Math.sqrt(1+k*k);
			double x_2=x2-dis/Math.sqrt(1+k*k);
			double y_1=y2+k*dis/Math.sqrt(1+k*k);
			double y_2=y2-k*dis/Math.sqrt(1+k*k);
			if((x_1-x1)*(x_1-x1)+(y_1-y1)*(y_1-y1)>(x_2-x1)*(x_2-x1)+(y_2-y1)*(y_2-y1))
				return new Xpoint(x_1,y_1);
			else
				return new Xpoint(x_2,y_2);
		}
	}

	static public Xpoint [] getPoints(Xpoint p1,Xpoint p2,double N){
		if(!(N==(int)N)){
			return null;
		}

		double x1=p1.x;
		double x2=p2.x;
		double y1=p1.y;
		double y2=p2.y;

		if(x1==x2&&y1==y2){
			return null;
		}
		if(x1==x2){
			double y_1;
			double y_2;
			double dis;
			double unit;
			ArrayList<Xpoint> list=new ArrayList<Xpoint>();
			y_1=y1;
			y_2=y2;
			dis=y_2-y_1;
			unit=dis/(N+1);
			for(int i=0;i<N;i++)
			{
				y_1=y_1+unit;
				list.add(new Xpoint(x1,y_1));
			}
			return list.toArray(new Xpoint[list.size()]);
		}
		else if(y1==y2){
			double x_1;
			double x_2;
			double dis;
			double unit;
			ArrayList<Xpoint> list=new ArrayList<Xpoint>();
			x_1=x1;
			x_2=x2;
			dis=x_2-x_1;
			unit=dis/(N+1);
			for(int i=0;i<N;i++)
			{
				x_1=x_1+unit;
				list.add(new Xpoint(x_1,y1));
			}
			return list.toArray(new Xpoint[list.size()]);
		}else{
			double k=(y2-y1)/(x2-x1);
			double b=y1-k*x1;
			double x_1;
			double x_2;
			double dis;
			double unit;
			ArrayList<Xpoint> list=new ArrayList<Xpoint>();
			x_1=x1;
			x_2=x2;
			dis=x_2-x_1;
			unit=dis/(N+1);
			for(int i=0;i<N;i++)
			{
				x_1=x_1+unit;
				list.add(new Xpoint(x_1,k*(x_1)+b));
			}
			return list.toArray(new Xpoint[list.size()]);
		}
	}




	static public boolean fightOk(Xpoint point_1,Xpoint point_2,double radius, ArrayList<Xpoint> point_list){
		double r=radius;
		double x1,x2,y1,y2;

		if(point_1==null||point_2==null){//不足两颗球
			return false;
		}else{
			x1=point_1.x;
			x2=point_2.x;
			y1=point_1.y;
			y2=point_2.y;
			if((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)<4*r*r){//两颗球有重合部分
				return false;
			}
		}
		if(point_list==null){//场上只有两颗球
			return true;
		}
		if(point_list.isEmpty()){//场上只有两颗球
			return true;
		}



		if(x1==x2){
			for(Iterator<Xpoint> list = point_list.iterator(); list.hasNext();){
				Xpoint p0=list.next();
				double x0=p0.x;
				double y0=p0.y;
				if((y0>y1&&y0>y2)||(y0<y1&&y0<y2)){
					continue;
				}
				if((x0-x1)*(x0-x1)<=4*r*r){
					return false;
				}
			}
			return true;


		}else if(y1==y2){
			for(Iterator<Xpoint> list=point_list.iterator();list.hasNext();){
				Xpoint p0=list.next();
				double x0=p0.x;
				double y0=p0.y;
				if((x0>x1&&x0>x2)||(x0<x1&&x0<x2)){
					continue;
				}
				if((y0-y1)*(y0-y1)<=4*r*r){
					return false;
				}
			}
			return true;


		}else{
			double k=(y2-y1)/(x2-x1);
			double b=y1-k*x1;
			double k_=-1/k;
			double distacne=Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
			for(Iterator<Xpoint> list=point_list.iterator();list.hasNext();){
				Xpoint p0=list.next();
				double x0=p0.x;
				double y0=p0.y;
				double dis1=Math.abs((k_*x0-y0-k_*x1+y1)/Math.sqrt(k_*k_+1));
				double dis2=Math.abs((k_*x0-y0-k_*x2+y2)/Math.sqrt(k_*k_+1));
				double disx=Math.abs((k*x0-y0+b)/Math.sqrt(k*k+1));
				if(Math.abs(dis1-dis2)>=distacne){
					continue;
				}
				if(disx<=2*r){
					return false;
				}
			}
			return true;
		}

	}



}

