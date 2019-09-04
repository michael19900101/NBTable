package com.aotuman.nbtable.table;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aotuman.nbtable.R;

import java.util.ArrayList;
import java.util.List;

public class XWTableView extends RelativeLayout {

    private XWTableHeaderLayout headerLayout;
    private RecyclerView contentRV;
    private XWLayoutManager xwLayoutManager;
    private XWTableRowViewAdapter rowViewAdapter;
    private List<XWTableColumn> tableColumns;
    private int tableWidth;
    private int freezeColumns;
    private static final float Z_ORDER_VALUE = 3f;

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
        headerLayout = findViewById(R.id.headerLayout);
        contentRV = findViewById(R.id.recyclerView);
        rowViewAdapter = new XWTableRowViewAdapter(contentRV, tableColumns);
        headerLayout.addColumnViews(tableColumns, constructColumnViews(tableColumns));
        contentRV.addOnScrollListener(contentRVScrollListener);
    }

    public void setFreezeColumns(int freezeColumns) {
        this.freezeColumns = freezeColumns;
        xwLayoutManager.setFreezeColumns(freezeColumns);
    }

    public void setTableWidth(int tableWidth) {
        this.tableWidth = tableWidth;
        xwLayoutManager = new XWLayoutManager(getContext(), tableWidth);
        contentRV.setLayoutManager(xwLayoutManager);
    }

    public void setTableData() {
        contentRV.setAdapter(rowViewAdapter);
        List<XWTableData> beans = XWTableData.initDatas();
        rowViewAdapter.setData(beans);
    }

    private List<View> constructColumnViews(List<XWTableColumn> columns) {
        List<View> viewList = new ArrayList<>();
        if (columns != null && columns.size() > 0) {
            for (XWTableColumn tableColumn : columns) {
                TextView textView = new TextView(getContext());
                textView.setText(tableColumn.getTitle());
                textView.setGravity(Gravity.CENTER);
                viewList.add(textView);
            }
        }
        return viewList;
    }

    private int mHorizontalOffset;//横向偏移量

    private RecyclerView.OnScrollListener contentRVScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int realOffset = dx;//实际滑动的距离
            if (mHorizontalOffset + dx < 0) { //左边界
                realOffset = -mHorizontalOffset;
            } else {
                int maxScrollWidth = tableWidth - getWidth();
                if (mHorizontalOffset + dx >= maxScrollWidth) { // 右边界
                    realOffset = maxScrollWidth - mHorizontalOffset;
                }
            }
            mHorizontalOffset += realOffset;//累加实际滑动距离

            if (freezeColumns > 0) {
                offsetFreezeColumnHorizontal(realOffset);
            } else {
                headerLayout.scrollBy(realOffset, 0);
            }
        }
    };

    /**
     * 平移冻结列
     *
     * @param dx
     */
    private void offsetFreezeColumnHorizontal(int dx) {
        if (headerLayout != null) {
            for (int i = 0; i < freezeColumns; i++) {
                View childView = headerLayout.getChildAt(i);
                if (childView != null) {
                    // 设置z轴的值，才能覆盖其他view
                    childView.setZ(Z_ORDER_VALUE);
                }
            }
            for (int i = freezeColumns; i < headerLayout.getChildCount(); i++) {
                View view = headerLayout.getChildAt(i);
                view.offsetLeftAndRight(-dx);
            }
        }
    }
}
