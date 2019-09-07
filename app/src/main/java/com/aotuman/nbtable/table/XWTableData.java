package com.aotuman.nbtable.table;

import java.util.ArrayList;
import java.util.List;

public class XWTableData {
    private int position;
    private String name;

    public XWTableData(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public static List<XWTableData> initDatas() {
        List<XWTableData> datas = new ArrayList<>();
        for (int i = 0;i < 1000;i ++){
            datas.add(new XWTableData(i, "第"+i+"行1234567890123456787901234567679"));
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
