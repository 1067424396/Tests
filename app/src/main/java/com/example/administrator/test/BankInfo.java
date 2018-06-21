package com.example.administrator.test;

/**
 * Created by Administrator on 2018/6/14.
 */
public class BankInfo implements IRollItem{

    public String ocpsId;
    public int title;
    @Override
    public String getImageUrl() {
        return ocpsId;
    }
    public void setImageUrl(String ocpsId) {
        this.ocpsId=ocpsId;
    }

    @Override
    public int getTitle() {
        return title;
    }
    public void setTitle(int title) {
        this.title=title;
    }

}
