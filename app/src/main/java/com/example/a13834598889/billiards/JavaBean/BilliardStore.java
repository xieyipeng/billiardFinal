package com.example.a13834598889.billiards.JavaBean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Yangyulin on 2018/7/24.
 * 店面信息表
 */

public class BilliardStore extends BmobObject {

    private String storeID;//店主ID
    private BmobFile picture_1;//广告图片1
    private BmobFile picture_2;//广告图片2
    private BmobFile picture_3;//广告图片3
    private Double price_pt;//普通用户开台单价
    private Double price_vip;//VIP用户开台单价
    private Integer num_customer;//客流量

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public BmobFile getPicture_1() {
        return picture_1;
    }

    public void setPicture_1(BmobFile picture_1) {
        this.picture_1 = picture_1;
    }

    public BmobFile getPicture_2() {
        return picture_2;
    }

    public void setPicture_2(BmobFile picture_2) {
        this.picture_2 = picture_2;
    }

    public BmobFile getPicture_3() {
        return picture_3;
    }

    public void setPicture_3(BmobFile picture_3) {
        this.picture_3 = picture_3;
    }

    public Double getPrice_pt() {
        return price_pt;
    }

    public void setPrice_pt(Double price_pt) {
        this.price_pt = price_pt;
    }

    public Double getPrice_vip() {
        return price_vip;
    }

    public void setPrice_vip(Double price_vip) {
        this.price_vip = price_vip;
    }

    public Integer getNum_customer() {
        return num_customer;
    }

    public void setNum_customer(Integer num_customer) {
        this.num_customer = num_customer;
    }

}
