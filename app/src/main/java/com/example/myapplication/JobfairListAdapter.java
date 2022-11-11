package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class JobfairListAdapter extends ArrayAdapter<Jobfair> {

    private Context mContext;
    private int mResource;
    private int  lastPosition = -1;

    static class ViewHolder {
        TextView jfname;
        TextView jflocation;
        TextView jfdate;
    }

    public JobfairListAdapter(Context context, int adapter_view_layout, ArrayList<Jobfair> upcomingList) {
        super(context, adapter_view_layout, upcomingList);
        mContext = context;
        mResource = adapter_view_layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String jfname = getItem(position).getJfname();
        String jflocation = getItem(position).getJflocation();
        String jfdate = getItem(position).getJfdate();

        Jobfair jobfair = new Jobfair(jfname,jflocation,jfdate);

        final View result;

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.jfname = (TextView) convertView.findViewById(R.id.jftextView111);
            holder.jflocation = (TextView) convertView.findViewById(R.id.jftextView222);
            holder.jfdate = (TextView) convertView.findViewById(R.id.jftextView333);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position> lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.jfname.setText(jfname);
        holder.jflocation.setText(jflocation);
        holder.jfdate.setText(jfdate);

        return convertView;
    }
}
