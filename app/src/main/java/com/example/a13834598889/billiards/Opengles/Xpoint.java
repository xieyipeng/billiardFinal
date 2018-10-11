package com.example.a13834598889.billiards.Opengles;

/**
 * Created by 97110 on 2018/9/16.
 */

public class Xpoint {
    public float x;
    public float y;

    public Xpoint (double x,double y){
        this.y =(float) y;
        this.x =(float) x;
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
