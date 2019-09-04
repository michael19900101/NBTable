package com.aotuman.nbtable.table;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class XWLayoutManager extends RecyclerView.LayoutManager {
    private static final float Z_ORDER_VALUE = 3f;
    private int mHorizontalOffset;//横向偏移量
    private int mVerticalOffset;//竖直偏移量 每次换行时，要根据这个offset判断
    private int mFirstVisiPos;//屏幕可见的第一个View的Position
    private int mLastVisiPos;//屏幕可见的最后一个View的Position
    private int rowWidth;//行宽
    private int freezeColumns = 0;//冻结列数
    private SparseArray<Rect> mItemRects;//key 是View的position，保存View的bounds 和 显示标志，

    public XWLayoutManager(Context context, int rowWidth) {
        this.rowWidth = rowWidth;
        mItemRects = new SparseArray<>();
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {//没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (state.isPreLayout()) {//state.isPreLayout()是支持动画的
            return;
        }
        if (getChildCount() == 0){
            //onLayoutChildren方法在RecyclerView 初始化时 会执行两遍
            detachAndScrapAttachedViews(recycler);

            //初始化区域
            mHorizontalOffset = 0;
            mVerticalOffset = 0;
            mFirstVisiPos = 0;
            mLastVisiPos = getItemCount();

            //初始化时调用 填充childView
            fill(recycler, state);
        }
    }

    /**
     * 初始化时调用 填充childView
     *
     * @param recycler
     * @param state
     */
    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        fill(recycler, state, 0, 0);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 位移0、没有子View 当然不移动
        if (dy == 0 || getChildCount() == 0) {
            return 0;
        }

        int realOffset = dy; //实际滑动的距离， 可能会在边界处被修复
        // 边界修复代码
        if (mVerticalOffset + realOffset < 0) { //上边界
            realOffset = -mVerticalOffset;
        } else if (realOffset > 0) { // 下边界
            // 利用最后一个子View比较修正
            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    realOffset = -gap;
                } else if (gap == 0) {
                    realOffset = 0;
                } else {
                    realOffset = Math.min(realOffset, -gap);
                }
            }
        }

        realOffset = fill(recycler, state, 0, realOffset); //先填充，再位移。

        mVerticalOffset += realOffset; //累加实际滑动距离

        offsetChildrenVertical(-realOffset); //滑动

        if (freezeColumns > 0) {
            offsetFreezeColumnVertical(dy);
        }

        return realOffset;
    }

    /**
     * 行宽大于表格宽度才可以横向滑动
     *
     * @return
     */
    @Override
    public boolean canScrollHorizontally() {
        return rowWidth > getWidth();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 位移0、没有子View 当然不移动
        if (dx == 0 || getChildCount() == 0) {
            return 0;
        }

        int realOffset = dx;//实际滑动的距离
        // 边界修复代码
        if (mHorizontalOffset + realOffset < 0) { //左边界
            realOffset = -mHorizontalOffset;
        } else {
            int maxScrollWidth = rowWidth - getWidth();
            if (mHorizontalOffset + realOffset >= maxScrollWidth) {
                realOffset = maxScrollWidth - mHorizontalOffset;
            }
        }

        fill(recycler, state, realOffset, 0);//先填充，再位移。

        mHorizontalOffset += realOffset;//累加实际滑动距离

        offsetChildrenHorizontal(-realOffset);//滑动

        if (freezeColumns > 0) {
            offsetFreezeColumnHorizontal(realOffset);
        }

        return dx;
    }


    /**
     * 平移冻结列
     *
     * @param dx
     */
    private void offsetFreezeColumnHorizontal(int dx) {
        if (getChildCount() > 0) {
            int leftStandard = 0;
            View firstVisibleRowView = getChildAt(0);
            if (firstVisibleRowView != null) {
                View childView = ((ViewGroup) firstVisibleRowView).getChildAt(0);
                leftStandard = childView.getLeft();
            }
            for (int i = 0; i < getChildCount(); i++) {
                View rowView = getChildAt(i);
                if (rowView != null) {
                    for (int j = 0; j < freezeColumns; j++) {
                        int offsetChildWidth = 0;
                        for (int k = j - 1; k >= 0; k--) {
                            View childView = ((ViewGroup) rowView).getChildAt(k);
                            if (childView != null) {
                                offsetChildWidth += childView.getMeasuredWidth();
                            }
                        }
                        View childView = ((ViewGroup) rowView).getChildAt(j);
                        if (childView != null) {
                            // 设置z轴的值，才能覆盖其他view
                            childView.setZ(Z_ORDER_VALUE);
                            int horizontalOffset = leftStandard - childView.getLeft() + offsetChildWidth;
                            childView.offsetLeftAndRight(dx + horizontalOffset);
                        }
                    }
                }
            }
        }
    }

    /**
     * 竖直方向滚动，平移冻结列
     */
    private void offsetFreezeColumnVertical(int dy) {
        if (dy == 0) {
            return;
        }
        if (getChildCount() > 0) {
            int leftStandard = 0;
            // 向上滑，加载更多的行
            if (dy > 0) {
                View firstVisibleRowView = getChildAt(0);
                if (firstVisibleRowView != null) {
                    View childView = ((ViewGroup) firstVisibleRowView).getChildAt(0);
                    leftStandard = childView.getLeft();
                }
            } else {
                // 向下滑，显示之前的行
                View lastVisibleRowView = getChildAt(getChildCount() - 1);
                if (lastVisibleRowView != null) {
                    View childView = ((ViewGroup) lastVisibleRowView).getChildAt(0);
                    leftStandard = childView.getLeft();
                }
            }
            for (int i = 0; i < getChildCount(); i++) {
                View rowView = getChildAt(i);
                if (rowView != null) {
                    for (int j = 0; j < freezeColumns; j++) {
                        int offsetChildWidth = 0;
                        for (int k = j - 1; k >= 0; k--) {
                            View childView = ((ViewGroup) rowView).getChildAt(k);
                            if (childView != null) {
                                offsetChildWidth += childView.getMeasuredWidth();
                            }
                        }
                        View childView = ((ViewGroup) rowView).getChildAt(j);
                        if (childView != null) {
                            // 设置z轴的值，才能覆盖其他view
                            childView.setZ(Z_ORDER_VALUE);
                            int horizontalOffset = leftStandard - childView.getLeft() + offsetChildWidth;
                            childView.offsetLeftAndRight(horizontalOffset);
                        }
                    }
                }
            }
        }
    }


    /**
     * 填充childView的核心方法,应该先填充，再移动。
     * 在填充时，预先计算dy的在内，如果View越界，回收掉。
     * 一般情况是返回dy，如果出现View数量不足，则返回修正后的dy.
     *
     * @param recycler
     * @param state
     * @param dy       RecyclerView给我们的位移量,+,显示底端， -，显示头部
     * @return 修正以后真正的dy（可能剩余空间不够移动那么多了 所以return <|dy|）
     */
    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dx, int dy) {

        int topOffset = getPaddingTop();

        //回收越界子View
        if (getChildCount() > 0) {//滑动时进来的
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View child = getChildAt(i);
                if (dy > 0) {//需要回收当前屏幕，上越界的View
                    if (getDecoratedBottom(child) - dy < topOffset) {
                        removeAndRecycleView(child, recycler);
                        mFirstVisiPos++;
                        continue;
                    }
                } else if (dy < 0) {//回收当前屏幕，下越界的View
                    if (getDecoratedTop(child) - dy > getHeight() - getPaddingBottom()) {
                        removeAndRecycleView(child, recycler);
                        mLastVisiPos--;
                        continue;
                    }
                }
            }
            //detachAndScrapAttachedViews(recycler);
        }

        int leftOffset = getPaddingLeft();
        int lastViewHeight = 0;
        //布局子View阶段
        if (dy >= 0) {
            int minPos = mFirstVisiPos;
            mLastVisiPos = getItemCount() - 1;
            if (getChildCount() > 0) {
                View lastView = getChildAt(getChildCount() - 1);
                minPos = getPosition(lastView) + 1;//从最后一个View+1开始吧
                topOffset = getDecoratedTop(lastView);
                leftOffset += lastView.getLeft();
                lastViewHeight = getDecoratedMeasurementVertical(lastView);
            }
            //顺序addChildView
            for (int i = minPos; i <= mLastVisiPos; i++) {
                //找recycler要一个childItemView,我们不管它是从scrap里取，还是从RecyclerViewPool里取，亦或是onCreateViewHolder里拿。
                View child = recycler.getViewForPosition(i);
                if (child != null) {
                    child.getLayoutParams().width = rowWidth;
                }
                addView(child);
                measureChildWithMargins(child, 0, 0);

                //新起一行的时候要判断一下边界
                if (topOffset - dy > getHeight() - getPaddingBottom()) {
                    //越界了 就回收
                    removeAndRecycleView(child, recycler);
                    mLastVisiPos = i - 1;
                } else {
                    topOffset += lastViewHeight;
                    layoutDecoratedWithMargins(child,
                            leftOffset,
                            topOffset,
                            leftOffset + getDecoratedMeasurementHorizontal(child),
                            topOffset + getDecoratedMeasurementVertical(child));

                    //保存Rect供逆序layout用
                    Rect rect = new Rect(leftOffset + mHorizontalOffset,
                            topOffset + mVerticalOffset,
                            leftOffset + getDecoratedMeasurementHorizontal(child) + mHorizontalOffset,
                            topOffset + getDecoratedMeasurementVertical(child) + mVerticalOffset);
                    mItemRects.put(i, rect);

                    //改变lineHeight
                    topOffset += getDecoratedMeasurementVertical(child);
                }
            }
            //添加完后，判断是否已经没有更多的ItemView，并且此时屏幕仍有空白，则需要修正dy
            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    dy -= gap;
                }
            }

        } else {
            /**
             * ##  利用Rect保存子View边界
             正序排列时，保存每个子View的Rect，逆序时，直接拿出来layout。
             */
            int maxPos = getItemCount() - 1;
            mFirstVisiPos = 0;
            if (getChildCount() > 0) {
                View firstView = getChildAt(0);
                maxPos = getPosition(firstView) - 1;
            }
            for (int i = maxPos; i >= mFirstVisiPos; i--) {
                Rect rect = mItemRects.get(i);

                if (rect != null) {
                    if (rect.bottom - mVerticalOffset - dy < getPaddingTop()) {
                        mFirstVisiPos = i + 1;
                        break;
                    } else {
                        View child = recycler.getViewForPosition(i);
                        if (child != null) {
                            child.getLayoutParams().width = rowWidth;
                        }
                        addView(child, 0);//将View添加至RecyclerView中，childIndex为1，但是View的位置还是由layout的位置决定
                        measureChildWithMargins(child, 0, 0);

                        layoutDecoratedWithMargins(child, rect.left - mHorizontalOffset,
                                rect.top - mVerticalOffset,
                                rect.right - mHorizontalOffset,
                                rect.bottom - mVerticalOffset);
                    }
                }
            }
        }


        Log.d("TAG", "count= [" + getChildCount() + "]" + ",[recycler.getScrapList().size():"
                + recycler.getScrapList().size() + ", dy:" + dy + ",  mVerticalOffset:" + mVerticalOffset + ", "
                + ", dx:" + dx + ", mHorizontalOffset:" + mHorizontalOffset);

        return dy;
    }

    //模仿LLM Horizontal 源码

    /**
     * 获取某个childView在水平方向所占的空间
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementHorizontal(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }

    public void setFreezeColumns(int freezeColumns) {
        this.freezeColumns = freezeColumns;
    }

    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
}
