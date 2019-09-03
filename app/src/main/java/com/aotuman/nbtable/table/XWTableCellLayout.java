package com.aotuman.nbtable.table;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.aotuman.nbtable.R;

/**
 * 表格单元格布局
 */
public class XWTableCellLayout extends RelativeLayout {

    private View contentView;

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
        setBackground(AppCompatResources.getDrawable(context, R.drawable.cell_background));
    }

}
