package com.aotuman.nbtable.table;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.aotuman.nbtable.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XWTableRowLayout extends LinearLayout {

    private Map<XWTableColumn, View> columnViewMap = new HashMap<>();

    public XWTableRowLayout(Context context) {
        super(context);
        init(context);
    }

    public XWTableRowLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XWTableRowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
    }

    public void addColumnViews(List<XWTableColumn> columns, List<View> columnViews) {
        if (columns != null && columns.size() > 0) {
            removeAllViews();
            for (int i = 0; i < columns.size(); i++) {
                // 表格单元格外层布局
                XWTableCellLayout cellLayout = (XWTableCellLayout) LayoutInflater.from(getContext()).inflate(R.layout.table_cell_view, this, false);
                // 宽度固定，高度填充父布局(后面遍历单元格，算出最大的单元格高度为行高)
                cellLayout.setLayoutParams(new ViewGroup.LayoutParams(columns.get(i).getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
                if (columnViews.get(i) != null) {
                    if (columnViews.get(i).getParent() != null) {
                        ((ViewGroup) (columnViews.get(i).getParent())).removeView(columnViews.get(i));
                    }
                    // 单元格内容view，宽高都是填满父布局
                    columnViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    // 在单元格里面添加内容view
                    cellLayout.addView(columnViews.get(i));
                }
                // 单元格外层布局添加到横布局
                addView(cellLayout);
                columnViewMap.put(columns.get(i), columnViews.get(i));
            }
        }
    }

    public Map<XWTableColumn, View> getColumnViewMap() {
        return columnViewMap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        // 以最大的cell高度为行高
        int maxCellHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View cellLayout = getChildAt(i);
            int cellHeight = cellLayout.getMeasuredHeight();
            maxCellHeight = Math.max(cellHeight, maxCellHeight);
        }

        for (int i = 0; i < getChildCount(); i++) {
            View cellLayout = getChildAt(i);
            if (cellLayout instanceof ViewGroup && ((ViewGroup) cellLayout).getChildAt(0) != null) {
                View cellContentView = ((ViewGroup) cellLayout).getChildAt(0);
                // 重设cellContentView的宽度，填充父布局宽度
                cellContentView.getLayoutParams().width = cellLayout.getMeasuredWidth();
                // 重设cellContentView的高度，填充父布局高度
                cellContentView.getLayoutParams().height = maxCellHeight;
                // 记得调用一下measure(This is called to find out how big a view should be)，
                // 这样cellContentView的布局参数才会发生改变
                cellLayout.measure(cellLayout.getMeasuredWidth(), maxCellHeight);
            }
        }

        // 设置行布局的宽、高
        setMeasuredDimension(sizeWidth, maxCellHeight);
    }
}
