package com.aotuman.nbtable.table;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 表格单元格布局
 */
public class XWTableCellLayout extends RelativeLayout {

    public XWTableCellLayout(Context context) {
        super(context);
        init(context);
    }

    public XWTableCellLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XWTableCellLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
    }

}
