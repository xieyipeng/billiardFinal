package com.example.a13834598889.billiards.Opengles;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by 13834598889 on 2018/7/9.
 */

public class Point {


    int muMVPMatrixHandle;
    int maPositionHandle;
    int maColorHandle;
    String mVertexShader;
    String mFragmentShader;
    FloatBuffer mVertexBuffer;
    FloatBuffer mColorBuffer;

    public int vCounts;

    private static int programId;


    static float[] mMMatrix = new float[16];



    public Point(float [] vertices, float [] colorArray){

        mVertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colorArray);
        mColorBuffer.position(0);
        intShader();
    }


    public void intShader(){

        mVertexShader = "uniform mat4 uMVPMatrix;" +
                "attribute vec3 aPosition;" +
                "attribute vec4 aColor;" +
                "varying vec4 vColor;" +
                "void main(){" +
                "   gl_Position = uMVPMatrix * vec4 (aPosition,1);" +
                "   vColor = aColor;" +
                "}";
        mFragmentShader = "precision mediump float;" +
                "varying vec4 vColor;" +
                "void main(){" +
                "    gl_FragColor = vColor;" +

                "}";


        programId = ShaderUtil.createProgram(mVertexShader,mFragmentShader);



        maPositionHandle = GLES20.glGetAttribLocation(programId,"aPosition");
        maColorHandle = GLES20.glGetAttribLocation(programId,"aColor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(programId,"uMVPMatrix");
    }


    public void drawSelf(int lineWidth,int way,int vCount){

        GLES20.glUseProgram(programId);

        Matrix.setRotateM(mMMatrix,0,0,0,1,0);
        Matrix.rotateM(mMMatrix,0,0,0,1,0);
        Matrix.rotateM(mMMatrix,0,0,1,0,0);


        GLES20.glUniformMatrix4fv(muMVPMatrixHandle,1,false,MatrixState.getFinalMatrix(mMMatrix),0);
        GLES20.glVertexAttribPointer(maPositionHandle,3,GLES20.GL_FLOAT,false,3*4,mVertexBuffer);
        GLES20.glVertexAttribPointer(maColorHandle,4,GLES20.GL_FLOAT,false,4*4,mColorBuffer);

        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);

//        GLES20.glEnable(GLES20.GL_BLEND);
//        GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glLineWidth(lineWidth);
        GLES20.glDrawArrays(way,0,vCount);
    }

}
