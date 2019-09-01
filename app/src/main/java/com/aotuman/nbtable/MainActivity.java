package com.aotuman.nbtable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.aotuman.nbtable.util.DensityUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private XWLayoutManager xwLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        xwLayoutManager = new XWLayoutManager(this, DensityUtil.dip2px(this, 700));
        recyclerView.setLayoutManager(xwLayoutManager);
        final XWRecyclerViewAdapter adapter = new XWRecyclerViewAdapter(recyclerView);
        recyclerView.setAdapter(adapter);
        final List<HorizontalBean> beans = HorizontalBean.initDatas();
        adapter.setData(beans);
    }
}
