package com.aotuman.nbtable;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.aotuman.nbtable.table.XWTableColumn;
import com.aotuman.nbtable.table.XWTableView;
import com.aotuman.nbtable.util.DensityUtil;

import java.util.ArrayList;

public class TableMainActivity extends AppCompatActivity {

    private ArrayList<XWTableColumn> columns;
    private XWTableView tableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        constrcutColumnDatas();
        setContentView(R.layout.table_mian);
        LinearLayout parent = findViewById(R.id.parentLayout);
        tableView = new XWTableView(this, columns);
        parent.addView(tableView);

        int totalWidth = 0;
        for(XWTableColumn tableColumn:columns){
            totalWidth += tableColumn.getWidth();
        }
        tableView.setTableWidth(totalWidth);
        tableView.setTableData();
    }

    private void constrcutColumnDatas(){
        columns = new ArrayList<>();
        columns.add(new XWTableColumn("列1",DensityUtil.dip2px(this,100),DensityUtil.dip2px(this,50)));
        columns.add(new XWTableColumn("列2",DensityUtil.dip2px(this,120),DensityUtil.dip2px(this,50)));
        columns.add(new XWTableColumn("列3",DensityUtil.dip2px(this,130),DensityUtil.dip2px(this,50)));
        columns.add(new XWTableColumn("列4",DensityUtil.dip2px(this,90),DensityUtil.dip2px(this,50)));
        columns.add(new XWTableColumn("列5",DensityUtil.dip2px(this,110),DensityUtil.dip2px(this,50)));
        columns.add(new XWTableColumn("列6",DensityUtil.dip2px(this,80),DensityUtil.dip2px(this,50)));
        columns.add(new XWTableColumn("列7",DensityUtil.dip2px(this,100),DensityUtil.dip2px(this,50)));
    }
}
