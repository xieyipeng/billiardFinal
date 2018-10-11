package com.example.a13834598889.billiards.JavaBean;

/**
 * Created by 13834598889 on 2018/4/29.
 * 店家账号信息表
 */

public class ShopKeeper extends User{

    private String latlng;//店铺所在位置的经度
    private String location;//地址
    private Integer tableNum;

    public Integer getTableNum() {
        return tableNum;
    }

    public void setTableNum(Integer tableNum) {
        this.tableNum = tableNum;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
