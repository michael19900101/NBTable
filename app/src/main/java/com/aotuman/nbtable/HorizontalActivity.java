package com.aotuman.nbtable;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HorizontalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private XWHorizontalScrollView horizontalScrollView;
    LinearLayoutManager linearLayoutManager;
    int screenwidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizontal);
        horizontalScrollView = (XWHorizontalScrollView)findViewById(R.id.horizontalScrollView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        final List<HorizontalBean> beans = HorizontalBean.initDatas();
        final MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(recyclerView,horizontalScrollView);
        recyclerView.setAdapter(adapter);
        adapter.setData(beans);


        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        final int screenwidth = wm.getDefaultDisplay().getWidth();


        ScrollViewListener scrollViewListener = new ScrollViewListener() {

            @Override
            public void onScrollChanged(XWHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                Log.e("jbjb","x:"+x+",oldx:"+oldx+",y:"+y+",oldy:"+oldy);
                int[] location = new int[2];
                scrollView.getLocationOnScreen(location);
                int leftMin = location[0];
                int firstVisiblePos = linearLayoutManager.findFirstVisibleItemPosition();
                int LastVisiblePos = linearLayoutManager.findLastVisibleItemPosition();
                for(int i = firstVisiblePos; i <= LastVisiblePos;i ++){
                    MyRecyclerViewAdapter.RecyclerHolder viewHolder = (MyRecyclerViewAdapter.RecyclerHolder) recyclerView.findViewHolderForAdapterPosition(i);

                    if(viewHolder != null){
                        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
                        View firstLeftView = viewGroup.getChildAt(0);
                        View firstRightView = viewGroup.getChildAt(viewGroup.getChildCount() - 1);

                        // 第X行左边第一个冻结列view左上角的坐标
                        int[] freezeView1Pos = new int[2];
                        firstLeftView.getLocationOnScreen(freezeView1Pos);

                        // 第X行最右边的view左上角的坐标
                        int[] rightViewPos = new int[2];
                        firstRightView.getLocationOnScreen(rightViewPos);
                        // 到达最右边就不要移动
                        if(scrollView.getScrollX() + screenwidth >= recyclerView.getMeasuredWidth()){
                            return;
                        }
                        // 向右移
                        if(x > oldx){
                            // 冻结列第一列已经到达最左边，就不用再向右移动
                            if(freezeView1Pos[0] > leftMin){
                                return;
                            }
                            int deltaX = x - oldx;
                            Log.e("jbjb","x:"+x+",oldx:"+oldx+",getScrollX:"+scrollView.getScrollX());
                            firstLeftView.setZ(3f);
                            firstLeftView.offsetLeftAndRight(Math.abs(deltaX));
                        }
                        // 向左移
                        if(x < oldx){
                            // 到达最左边就不要移动
                            if(freezeView1Pos[0] <= leftMin){
                                return;
                            }
                            int deltaX = x - oldx;
                            Log.e("jbjb","x:"+x+",oldx:"+oldx+",getScrollX:"+scrollView.getScrollX());
                            firstLeftView.setZ(3f);
                            firstLeftView.offsetLeftAndRight(-Math.abs(deltaX));
                        }
                    }

                }
            }
        };
        horizontalScrollView.setScrollViewListener(scrollViewListener);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.e("jbjb","addOnScrollListener dx:"+dx+",dy:"+dy+",scrollx:"+horizontalScrollView.getScrollX());
                int firstVisiblePos = linearLayoutManager.findFirstVisibleItemPosition();
                int LastVisiblePos = linearLayoutManager.findLastVisibleItemPosition();
                for(int i = firstVisiblePos; i <= LastVisiblePos;i ++){
                    MyRecyclerViewAdapter.RecyclerHolder viewHolder = (MyRecyclerViewAdapter.RecyclerHolder) recyclerView.findViewHolderForAdapterPosition(i);

                    if(viewHolder != null){
                        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
                        View firstLeftView = viewGroup.getChildAt(0);
                        // 向下滑动新的视图出现在屏幕内，但这部分冻结列是没有平移的，需要将他们向右平移
                        if(firstLeftView.getLeft() != horizontalScrollView.getScrollX()){
                            Log.e("jbjb","firstLeftView.getLeft() != horizontalScrollView.getScrollX() 平移");
                            firstLeftView.offsetLeftAndRight(horizontalScrollView.getScrollX());
                        }

                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
