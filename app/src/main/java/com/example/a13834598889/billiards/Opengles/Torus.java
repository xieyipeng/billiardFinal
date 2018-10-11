package com.example.a13834598889.billiards.Opengles;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.a13834598889.billiards.LoginActivity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static com.example.a13834598889.billiards.Opengles.ShaderUtil.createProgram;


/*
 * Բ��
 */
public class Torus
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������
    int maTexCoorHandle; //��������������������
    
    String mVertexShader;//������ɫ������ű�  	 
    String mFragmentShader;//ƬԪ��ɫ������ű�
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���

    int vCount=0;   
    float xAngle=0;//��x����ת�ĽǶ�
    float yAngle=0;//��y����ת�ĽǶ�
    float zAngle=0;//��z����ת�ĽǶ�

	public float x;
	public float y;
	public float z;
	public int myTexID;


	public float[] myVertices;
    
    public Torus(MySurfaceView mv, float rBig, float rSmall, int nCol , int nRow, float x, float y, float z, int texID)
    {


    	//���ó�ʼ���������ݵ�initVertexData����
    	initVertexData(rBig,rSmall,nCol,nRow);
    	//���ó�ʼ����ɫ����intShader����
    	initShader(mv);

		this.x = x;
		this.y = y;
		this.z = z;
		this.myTexID = initTexture(texID);
    }
    
    //�Զ���ĳ�ʼ���������ݵķ���
    public void initVertexData(
			float rBig, float rSmall,//Բ���⾶���ھ�
			int nCol ,int nRow) {//����������
		//��Ա������ʼ��
		float angdegColSpan=360.0f/nCol;
		float angdegRowSpan=360.0f/nRow;
		float A=(rBig-rSmall)/2;//������ת��СԲ�뾶
		float D=rSmall+A;//��ת�켣�γɵĴ�Բ�ܰ뾶
		vCount=3*nCol*nRow*2;//�������������nColumn*nRow*2�������Σ�ÿ�������ζ�����������
		//�������ݳ�ʼ��
		ArrayList<Float> alVertix=new ArrayList<Float>();//ԭ�����б�δ���ƣ�
		ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();//��֯����Ķ��������ֵ�б�����ʱ����ƣ�		
		//����
		for(float angdegCol=0;Math.ceil(angdegCol)<360+angdegColSpan;
		angdegCol+=angdegColSpan)	{
			double a=Math.toRadians(angdegCol);//��ǰСԲ�ܻ���
			for(float angdegRow=0;Math.ceil(angdegRow)<360+angdegRowSpan;angdegRow+=angdegRowSpan)//�ظ���һ�ж��㣬�����������ļ���
			{
				double u=Math.toRadians(angdegRow);//��ǰ��Բ�ܻ���
				float y=(float) (A*Math.cos(a));
				float x=(float) ((D+A*Math.sin(a))*Math.sin(u));
				float z=(float) ((D+A*Math.sin(a))*Math.cos(u));
				//�����������XYZ��������Ŷ��������ArrayList
        		alVertix.add(x); alVertix.add(y); alVertix.add(z);
			}
		}				
		//����
		for(int i=0;i<nCol;i++){
			for(int j=0;j<nRow;j++){
				int index=i*(nRow+1)+j;
				//��������
				alFaceIndex.add(index+1);//��һ��---1
				alFaceIndex.add(index+nRow+1);//��һ��---2
				alFaceIndex.add(index+nRow+2);//��һ����һ��---3
				
				alFaceIndex.add(index+1);//��һ��---1
				alFaceIndex.add(index);//��ǰ---0
				alFaceIndex.add(index+nRow+1);//��һ��---2
			}
		}
		float[] vertices=new float[vCount*3];
		//������ƺ�Ķ�������
		cullVertex(alVertix, alFaceIndex, vertices);


		//����
		ArrayList<Float> alST=new ArrayList<Float>();//ԭ���������б�δ���ƣ�
		for(float angdegCol=0;Math.ceil(angdegCol)<360+angdegColSpan;angdegCol+=angdegColSpan)
		{
			float t=angdegCol/360;//t����
			for(float angdegRow=0;Math.ceil(angdegRow)<360+angdegRowSpan;angdegRow+=angdegRowSpan)//�ظ���һ���������꣬�������ļ���
			{
				float s=angdegRow/360;//s����
				//�����������ST��������Ŷ��������ArrayList
				alST.add(s); alST.add(t);
			}
		}
		//������ƺ���������
		float[] textures=cullTexCoor(alST, alFaceIndex);

		myVertices = vertices;

		//�����������ݳ�ʼ��
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//���������������ݻ���
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��

        //st�������ݳ�ʼ��		
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);//���������������ݻ���
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��Ϊ���ز���ϵͳ˳��
        mTexCoorBuffer = tbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mTexCoorBuffer.put(textures);//�򻺳����з��붥����������
        mTexCoorBuffer.position(0);//���û�������ʼλ��

	}
    
	//ͨ��ԭ������������ֵ���õ��ö�����Ƶ�����
	public static void cullVertex(
			ArrayList<Float> alv,//ԭ�����б�δ���ƣ�
			ArrayList<Integer> alFaceIndex,//��֯����Ķ��������ֵ�б�����ʱ����ƣ�
			float[] vertices//�ö�����Ƶ����飨����������������У����鳤��Ӧ���������б��ȵ�3����
		){
		//���ɶ��������
		int vCount=0;
		for(int i:alFaceIndex){
			vertices[vCount++]=alv.get(3*i);
			vertices[vCount++]=alv.get(3*i+1);
			vertices[vCount++]=alv.get(3*i+2);
		}
	}
	//����ԭ���������������������ƺ������ķ���
	public static float[] cullTexCoor(
			ArrayList<Float> alST,//ԭ���������б�δ���ƣ�
			ArrayList<Integer> alTexIndex//��֯������������������ֵ�б�����ʱ����ƣ�
			)
	{
		float[] textures=new float[alTexIndex.size()*2];
		//���ɶ��������
		int stCount=0;
		for(int i:alTexIndex){
			textures[stCount++]=alST.get(2*i);
			textures[stCount++]=alST.get(2*i+1);
		}
		return textures;
	}

    //�Զ����ʼ����ɫ��initShader����
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_tex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_tex.sh", mv.getResources());
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������id
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");


    }
    
    public void drawSelf()
    {

    	MatrixState.translate(x,y,z);

    	 //�ƶ�ʹ��ĳ��shader����
    	 GLES20.glUseProgram(mProgram);        
         
         //�����ձ任������shader����
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         
         //���Ͷ���λ������
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //���Ͷ���������������
         GLES20.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         ); 
         
         //���ö���λ������
         GLES20.glEnableVertexAttribArray(maPositionHandle);
         //���ö�����������
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         
         //������
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, myTexID);
         
         //�����������
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
         
    }


	public int initTexture(int drawableId)//textureId
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
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);

		//通过输入流加载图片===============begin===================
		InputStream is = LoginActivity.mGLSurfaceView.getResources().openRawResource(drawableId);
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
