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

    private static final float Z_ORDER_VALUE = 3f;
    private XWTableHeaderLayout headerLayout;
    private XWTableRecyclerview contentRV;
    private XWTableBottomLayout bottomLayout;
    private XWLayoutManager xwLayoutManager;
    private XWTableRowViewAdapter rowViewAdapter;
    private List<XWTableColumn> tableColumns;
    private int tableWidth;
    private int freezeColumns;
    private int mHorizontalOffset;//横向偏移量

    /**
     * 监听内容布局滑动
     */
    private RecyclerView.OnScrollListener contentRVScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            scrollHeadAndBottomLayout(headerLayout, bottomLayout, dx);
        }
    };
    /**
     * 头部布局变化监听器
     * 键盘弹起会引起重新布局，此时头部布局要与内容布局保持一致的宽度偏移
     */
    private OnLayoutChangeListener headerLayoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (Math.abs(mHorizontalOffset) > 0) {
                int realOffset = 0;//实际滑动的距离
                if (mHorizontalOffset < 0) { //左边界
                    realOffset = -mHorizontalOffset;
                } else {
                    int maxScrollWidth = tableWidth - getWidth();
                    if (mHorizontalOffset >= maxScrollWidth) { // 右边界
                        realOffset = maxScrollWidth - mHorizontalOffset;
                    }
                }
                if (freezeColumns > 0) {
                    offsetFreezeColumnHorizontal(headerLayout, mHorizontalOffset);
                } else {
                    offsetColumnHorizontal(headerLayout, realOffset);
                }
            }
        }
    };
    /**
     * 底部布局变化监听器
     * 键盘弹起会引起重新布局，此时底部布局要与内容布局保持一致的宽度偏移
     */
    private OnLayoutChangeListener bottomLayoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (Math.abs(mHorizontalOffset) > 0) {
                int realOffset = 0;//实际滑动的距离
                if (mHorizontalOffset < 0) { //左边界
                    realOffset = -mHorizontalOffset;
                } else {
                    int maxScrollWidth = tableWidth - getWidth();
                    if (mHorizontalOffset >= maxScrollWidth) { // 右边界
                        realOffset = maxScrollWidth - mHorizontalOffset;
                    }
                }
                if (freezeColumns > 0) {
                    offsetFreezeColumnHorizontal(bottomLayout, mHorizontalOffset);
                } else {
                    offsetColumnHorizontal(bottomLayout, realOffset);
                }
            }
        }
    };

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
        bottomLayout = findViewById(R.id.bottomLayout);
        rowViewAdapter = new XWTableRowViewAdapter(contentRV, tableColumns);
        headerLayout.addColumnViews(tableColumns, constructHeaderColumnViews(tableColumns));
        bottomLayout.addColumnViews(tableColumns, constructBottomColumnViews(tableColumns));
        contentRV.addOnScrollListener(contentRVScrollListener);
        headerLayout.addOnLayoutChangeListener(headerLayoutChangeListener);
        bottomLayout.addOnLayoutChangeListener(bottomLayoutChangeListener);
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

    private List<View> constructHeaderColumnViews(List<XWTableColumn> columns) {
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

    private List<View> constructBottomColumnViews(List<XWTableColumn> columns) {
        List<View> viewList = new ArrayList<>();
        if (columns != null && columns.size() > 0) {
            for (int i = 0; i < columns.size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setText("统计" + i);
                textView.setGravity(Gravity.CENTER);
                viewList.add(textView);
            }
        }
        return viewList;
    }

    /**
     * 横向滑动表头和底部布局
     * @param head 表头布局
     * @param bottom 底部布局
     * @param dx
     */
    private void scrollHeadAndBottomLayout(ViewGroup head, ViewGroup bottom, int dx) {
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
            offsetFreezeColumnHorizontal(head, realOffset);
            offsetFreezeColumnHorizontal(bottom, realOffset);
        } else {
            offsetColumnHorizontal(head, realOffset);
            offsetColumnHorizontal(bottom, realOffset);
        }
    }

    /**
     * 平移冻结列
     * @param viewGroup
     * @param dx
     */
    private void offsetFreezeColumnHorizontal(ViewGroup viewGroup, int dx) {
        if (viewGroup != null && viewGroup.getVisibility() == VISIBLE) {
            for (int i = 0; i < freezeColumns; i++) {
                View childView = viewGroup.getChildAt(i);
                if (childView != null) {
                    // 设置z轴的值，才能覆盖其他view
                    childView.setZ(Z_ORDER_VALUE);
                }
            }
            for (int i = freezeColumns; i < viewGroup.getChildCount(); i++) {
                View view = viewGroup.getChildAt(i);
                view.offsetLeftAndRight(-dx);
            }
        }
    }

    /**
     * 平移非冻结列
     * @param viewGroup
     * @param dx
     */
    private void offsetColumnHorizontal(ViewGroup viewGroup, int dx) {
        if (viewGroup != null && viewGroup.getVisibility() == VISIBLE) {
            viewGroup.scrollBy(dx, 0);
        }
    }
}
