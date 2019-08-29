package com.aotuman.nbtable;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class XWHorizontalScrollView extends HorizontalScrollView {

    private ScrollViewListener scrollViewListener = null;

    public XWHorizontalScrollView(Context context) {
        super(context);
    }

    public XWHorizontalScrollView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    public XWHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

}