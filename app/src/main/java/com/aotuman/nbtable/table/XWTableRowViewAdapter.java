package com.aotuman.nbtable.table;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aotuman.nbtable.R;
import com.aotuman.nbtable.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XWTableRowViewAdapter extends RecyclerView.Adapter<XWTableRowViewAdapter.XWTableRowViewHolder> {
    private Context mContext;
    private List<XWTableData> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private List<XWTableColumn> columns;

    public XWTableRowViewAdapter(RecyclerView recyclerView, List<XWTableColumn> columns) {
        this.columns = columns;
        this.recyclerView = recyclerView;
        this.mContext = recyclerView.getContext();
    }

    public void setData(List<XWTableData> data) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public XWTableRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        XWTableRowLayout tableRowLayout = (XWTableRowLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.table_row_view, parent, false);
        List<View> columnViews = testAddColumnViews(columns);
        tableRowLayout.addColumnViews(columns, columnViews);
        return new XWTableRowViewHolder(tableRowLayout);
    }

    @Override
    public void onBindViewHolder(XWTableRowViewHolder holder, int position) {
        XWTableData item = dataList.get(position);
        XWTableRowLayout tableRowLayout = (XWTableRowLayout) holder.itemView;
        Map<XWTableColumn, View> columnViewMap = tableRowLayout.getColumnViewMap();
        for (Map.Entry<XWTableColumn, View> entry : columnViewMap.entrySet()) {
            View view = entry.getValue();
            if (view instanceof TextView) {
                ((TextView) view).setText(item.getName() +  "\n" + entry.getKey().getTitle());
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class XWTableRowViewHolder extends RecyclerView.ViewHolder {

        private XWTableRowViewHolder(View itemView) {
            super(itemView);
        }
    }

    private List<View> testAddColumnViews(List<XWTableColumn> columns) {
        List<View> viewList = new ArrayList<>();
        if (columns != null && columns.size() > 0) {
            for (XWTableColumn tableColumn : columns) {
                EditText textView = new EditText(mContext);
                textView.setGravity(Gravity.CENTER);
                viewList.add(textView);
            }
        }
        return viewList;
    }

    private List<View> testAddColumnViews2(List<XWTableColumn> columns) {
        List<View> viewList = new ArrayList<>();
        if (columns != null && columns.size() > 0) {
            for (int i = 0; i < columns.size(); i++) {
                TextView textView = new TextView(mContext);
                int height = DensityUtil.dip2px(mContext, 50);
                if (i == 0) {
                    height = DensityUtil.dip2px(mContext, 30);
                }
                if (i == 3) {
                    height = DensityUtil.dip2px(mContext, 40);
                }
                if (i == 5) {
                    height = DensityUtil.dip2px(mContext, 90);
                }
                textView.setHeight(height);
                textView.setGravity(Gravity.CENTER);
                viewList.add(textView);
            }
        }
        return viewList;
    }

    public List<XWTableData> getDataList() {
        return dataList;
    }
}
