package com.aotuman.nbtable.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aotuman.nbtable.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XWTableRowViewAdapter extends RecyclerView.Adapter<XWTableRowViewAdapter.XWTableRowViewHolder> {
    private Context mContext;
    private List<XWTableData> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private List<XWTableColumn> columns;
    private List<View> columnViews;

    public XWTableRowViewAdapter(RecyclerView recyclerView, List<XWTableColumn> columns, List<View> columnViews) {
        this.columnViews = columnViews;
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
                ((TextView) view).setText(item.getName() + entry.getKey().getTitle());
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

}
