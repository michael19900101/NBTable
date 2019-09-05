package com.aotuman.nbtable.table;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class XWTableRecyclerview extends RecyclerView {
    public XWTableRecyclerview(@NonNull Context context) {
        super(context);
    }

    public XWTableRecyclerview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public XWTableRecyclerview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 控制滑动速率
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX *= 0.5;
        velocityY *= 0.5;
        return super.fling(velocityX, velocityY);
    }
}
