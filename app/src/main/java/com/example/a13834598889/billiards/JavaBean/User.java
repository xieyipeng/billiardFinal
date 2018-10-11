package com.example.a13834598889.billiards.JavaBean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 13834598889 on 2018/4/29.
 * 整体用户表
 */

public class User extends BmobUser{


    private String nickName;//昵称
    private BmobFile picture_head;//头像
    private String sign;//个性签名
    private boolean isStore;//是否店家

    public boolean getStore() {
        return isStore;
    }

    public void setStore(boolean store) {
        isStore = store;
    }



    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BmobFile getPicture_head() {
        return picture_head;
    }

    public void setPicture_head(BmobFile picture_head) {
        this.picture_head = picture_head;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
