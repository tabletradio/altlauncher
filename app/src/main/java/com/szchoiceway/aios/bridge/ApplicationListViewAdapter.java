package com.szchoiceway.aios.bridge;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ApplicationListViewAdapter extends ArrayAdapter<App> {
    Context ctx;

    public ApplicationListViewAdapter(Context context, int resourceId, List<App> data){
        super(context, resourceId, data);
        this.ctx = context;
    }
    private class ViewHolder {
        ImageView imageView;
        TextView nameView;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        App item = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.selectedapp, null);
            holder = new ViewHolder();
            holder.nameView = convertView.findViewById(R.id.appName);
            holder.imageView = convertView.findViewById(R.id.iconView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String nm = item.getName();
        if (null != item.getService() && item.getService().length() > 0){
            nm = nm + " - " + item.getService();
        }
        holder.nameView.setText(nm);
        holder.imageView.setImageDrawable(item.getImage());

        return convertView;
    }
}
