package com.aotuman.nbtable;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class XWRecyclerViewAdapter extends RecyclerView.Adapter<XWRecyclerViewAdapter.RecyclerHolder> {
    private Context mContext;
    private List<HorizontalBean> dataList = new ArrayList<>();
    private RecyclerView recyclerView;

    public XWRecyclerViewAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.mContext = recyclerView.getContext();
    }

    public void setData(List<HorizontalBean> data) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("jbjb","onCreateViewHolder");
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_horizon, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        Log.e("jbjb","onBindViewHolder position:"+position);
        HorizontalBean item = dataList.get(position);
        holder.tv1.setText(item.getName()+"第0列");
        holder.tv2.setText(item.getName()+"第1列");
        holder.tv3.setText(item.getName()+"第2列");
        holder.tv4.setText(item.getName()+"第3列");
        holder.tv5.setText(item.getName()+"第4列");
        holder.tv6.setText(item.getName()+"第5列");
        holder.tv7.setText(item.getName()+"第6列");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView tv5;
        TextView tv6;
        TextView tv7;


        private RecyclerHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
            tv5 = (TextView) itemView.findViewById(R.id.tv5);
            tv6 = (TextView) itemView.findViewById(R.id.tv6);
            tv7 = (TextView) itemView.findViewById(R.id.tv7);

            tv7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"点击了tv7",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
