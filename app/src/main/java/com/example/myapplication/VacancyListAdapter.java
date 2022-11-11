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

public class VacancyListAdapter extends ArrayAdapter<Vacancy> {

    private Context mContext;
    private int mResource;
    private int  lastPosition = -1;


    static class ViewHolder {
        TextView vacancy;
        TextView employer;
        TextView location;
    }

    public VacancyListAdapter(Context context, int adapter_view_layout, ArrayList<Vacancy> vacancyList) {
        super(context, adapter_view_layout, vacancyList);
        mContext = context;
        mResource = adapter_view_layout;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String vacancy = getItem(position).getVacancy();
        String employer = getItem(position).getEmployer();
        String location = getItem(position).getLocation();

        Vacancy vacancy1 = new Vacancy(vacancy,employer,location);

        final View result;

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.vacancy = (TextView) convertView.findViewById(R.id.textView111);
            holder.employer = (TextView) convertView.findViewById(R.id.textView222);
            holder.location = (TextView) convertView.findViewById(R.id.textView333);

            result = convertView;

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position> lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.vacancy.setText(vacancy);
        holder.employer.setText(employer);
        holder.location.setText(location);

        return convertView;
    }
}
