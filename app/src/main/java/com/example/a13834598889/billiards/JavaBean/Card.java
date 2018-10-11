package com.example.a13834598889.billiards.JavaBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 13834598889 on 2018/4/29.
 * 用户发表的帖子信息表
 */

public class Card extends BmobObject {
    private Customer customer;//发表人(顾客)
    private BmobFile picture;//配图
    private String text;//帖子内容
    private List<String> dianzan;
    private List<String> shoucang;

    public List<String> getShoucang() {
        return shoucang;
    }

    public void setShoucang(List<String> shoucang) {
        this.shoucang = shoucang;
    }

    public List<String> getDianzan() {
        return dianzan;
    }

    public void setDianzan(List<String> dianzan) {
        this.dianzan = dianzan;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public String getText() {
        return text;
    }

    public void setText(String context) {
        this.text = context;
    }
}
