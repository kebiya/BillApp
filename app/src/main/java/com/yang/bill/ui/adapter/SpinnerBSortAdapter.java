package com.yang.bill.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.bill.R;
import com.yang.bill.model.bean.local.BSort;
import com.yang.bill.utils.ImageUtils;

import java.util.List;

public class SpinnerBSortAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflate;
    private List<BSort> dataList;

    public SpinnerBSortAdapter(Context context, List<BSort> dataList) {
        this.context = context;
        this.inflate = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public BSort getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_spinner_sort, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();// get convertView's holder

        holder.tvTypeName.setText(dataList.get(position).getSortName());
        return convertView;
    }

    class ViewHolder {
        TextView tvTypeName;

        ViewHolder(View convertView) {
            tvTypeName = convertView.findViewById(R.id.tv_type_name);
            convertView.setTag(this);//set a viewholder
        }
    }

}
