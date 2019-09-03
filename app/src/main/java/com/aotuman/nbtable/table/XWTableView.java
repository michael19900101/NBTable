package com.aotuman.nbtable.table;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aotuman.nbtable.HorizontalBean;
import com.aotuman.nbtable.R;
import com.aotuman.nbtable.XWLayoutManager;
import com.aotuman.nbtable.XWRecyclerViewAdapter;
import com.aotuman.nbtable.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class XWTableView extends RelativeLayout {

    private LinearLayout titleLayout;
    private RecyclerView recyclerView;
    private XWLayoutManager xwLayoutManager;
    private XWTableRowViewAdapter adapter;
    private List<XWTableColumn> tableColumns;

    public XWTableView(Context context, List<XWTableColumn> tableColumns) {
        super(context);
        this.tableColumns = tableColumns;
        init(context);
    }

    public XWTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XWTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.tableview, this);
        titleLayout = findViewById(R.id.titleLayout);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new XWTableRowViewAdapter(recyclerView,tableColumns,testAddColumnViews());
        addTitleColumns(tableColumns);
    }

    private List<View> testAddColumnViews(){
        List<View> viewList = new ArrayList<>();
        if (tableColumns != null && tableColumns.size() > 0) {
            for (XWTableColumn tableColumn : tableColumns) {
                TextView textView = new TextView(getContext());
                textView.setHeight(DensityUtil.dip2px(getContext(),50));
                viewList.add(textView);
            }
        }
        return viewList;
    }

    public void setTableWidth(int tableWidth) {
        xwLayoutManager = new XWLayoutManager(getContext(), tableWidth);
        recyclerView.setLayoutManager(xwLayoutManager);
    }

    public void setTableData() {
        recyclerView.setAdapter(adapter);
        List<XWTableData> beans = XWTableData.initDatas();
        adapter.setData(beans);
    }

    private void addTitleColumns(List<XWTableColumn> tableColumns) {
        if (tableColumns != null && tableColumns.size() > 0) {
            for (XWTableColumn tableColumn : tableColumns) {
                TextView textView = new TextView(getContext());
                textView.setText(tableColumn.getTitle());
                titleLayout.addView(textView, new LinearLayout.LayoutParams(tableColumn.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }

}
