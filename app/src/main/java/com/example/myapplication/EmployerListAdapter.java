package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Employers;
import com.example.myapplication.R;

import java.util.ArrayList;

import static android.graphics.BlendMode.COLOR;

public class EmployerListAdapter extends ArrayAdapter<Employers> {

    private Context mContext;
    private int mResource;
    private int  lastPosition = -1;


    static class ViewHolder {
        TextView ename;
        TextView ecount;
    }

    public EmployerListAdapter(Context context, int adapter_view_layout, ArrayList<Employers> employerArr) {
        super(context, adapter_view_layout, employerArr);
        mContext = context;
        mResource = adapter_view_layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String ename = getItem(position).getEname();
        String ecount = getItem(position).getCount();

        Employers employer = new Employers(ename, ecount);

        final View result;

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.ename = (TextView) convertView.findViewById(R.id.textViewEname);
            holder.ecount = (TextView) convertView.findViewById(R.id.textViewCount);

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

        holder.ename.setText(ename);
        holder.ecount.setText(ecount);

        return convertView;
    }
}
