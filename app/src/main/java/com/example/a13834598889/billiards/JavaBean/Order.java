package com.example.a13834598889.billiards.JavaBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 13834598889 on 2018/4/29.
 * 结算账单表
 */

public class Order extends BmobObject {
    private String storeId;//店面ID（店主）
    private String userID;//结账发起人ID（顾客）
    private String time;//结账时间
    private String time_order;
    private Integer table_number;//结账桌位号
//    private Double moneyTable;//桌费 预约金额里面包含桌费
    private Double moneyGoods;//饮品零食费
    private Double money_order;//预约金额
    private String status; //状态

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Double getMoney_order() {
        return money_order;
    }

    public void setMoney_order(Double money_order) {
        this.money_order = money_order;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getTable_number() {
        return table_number;
    }

    public void setTable_number(Integer table_number) {
        this.table_number = table_number;
    }

//    public Double getMoneyTable() {
//        return moneyTable;
//    }
//
//    public void setMoneyTable(Double moneyTable) {
//        this.moneyTable = moneyTable;
//    }

    public Double getMoneyGoods() {
        return moneyGoods;
    }

    public void setMoneyGoods(Double moneyGoods) {
        this.moneyGoods = moneyGoods;
    }

    public String getTime_order() {
        return time_order;
    }

    public void setTime_order(String time_order) {
        this.time_order = time_order;
    }
}
