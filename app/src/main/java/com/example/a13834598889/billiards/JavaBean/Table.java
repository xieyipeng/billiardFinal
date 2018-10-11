package com.example.a13834598889.billiards.JavaBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 13834598889 on 2018/4/29.
 * 台球桌信息表
 */

public class Table extends BmobObject {

    private String storeID;//店主
    private Integer table_number;  //球桌编号,即桌位号
    private String state;//球桌状态
    private String start_time;//开桌时间
    private String end_time;//结束计费时间
    private String appoint_time;//预约时间
    private Double money;//总金额
    private Boolean isReserve;  //是否已经被预定
    private Boolean isStart;  //是否已经开始计费



    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Table() {
        this.table_number = 0;
        this.isReserve = false;
        this.isStart = false;
        this.start_time = "00:00";
        this.end_time = "00:00";
    }

    public Integer getTable_number() {
        return table_number;
    }

    public void setTable_number(Integer table_number) {
        this.table_number = table_number;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAppoint_time() {
        return appoint_time;
    }

    public void setAppoint_time(String appoint_time) {
        this.appoint_time = appoint_time;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Boolean getReserve() {
        return isReserve;
    }

    public void setReserve(Boolean reserve) {
        isReserve = reserve;
    }

    public Boolean getStart() {
        return isStart;
    }

    public void setStart(Boolean start) {
        isStart = start;
    }
}
