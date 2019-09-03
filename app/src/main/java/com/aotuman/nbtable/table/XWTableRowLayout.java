package com.aotuman.nbtable.table;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

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
                XWTableCellLayout cellLayout = new XWTableCellLayout(getContext());
                cellLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.cell_background));
                // 宽度固定，高度内容填充
                cellLayout.setLayoutParams(new ViewGroup.LayoutParams(columns.get(i).getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
                if (columnViews.get(i) != null) {
                    if (columnViews.get(i).getParent() != null) {
                        ((ViewGroup) (columnViews.get(i).getParent())).removeView(columnViews.get(i));
                    }
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
}
