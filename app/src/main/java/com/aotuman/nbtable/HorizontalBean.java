package com.aotuman.nbtable;

import java.util.ArrayList;
import java.util.List;


public class HorizontalBean {

    private int position;
    private String name;

    public HorizontalBean(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public static List<HorizontalBean> initDatas() {
        List<HorizontalBean> datas = new ArrayList<>();
        for (int i = 0;i < 100;i ++){
            datas.add(new HorizontalBean(i, "小明"+i));
        }

        return datas;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
