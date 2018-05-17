package com.tomatofighters.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomatofighters.Models.Track;
import com.tomatofighters.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Jianbin Li
 *
 */

public class TimerViewAdapter extends RecyclerViewAdapter
{
    private List<Track> dataList = new ArrayList<>();
    private String color;
    private Context context;


    public TimerViewAdapter(Context context, String color)
    {
        super();
        this.context = context;
        this.color = color;
    }
    @Override
    public List<Track> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<Track> dataList)
    {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe_timer, parent, false);
        return new TimerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Track data = dataList.get(position);
        ((MyViewHolder) holder).nameView.setText(data.getName());

        ((MyViewHolder) holder).timeView.setText(data.getTime());


        ((MyViewHolder) holder).mainView.setBackground(createDrawableSelector(context, color));
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    @Override
    public void removeItem(int position)
    {

    }

    private StateListDrawable createDrawableSelector(Context context, String color)
    {
        GradientDrawable activated = (GradientDrawable) context.getResources().getDrawable(R.drawable.highlight_background);
        activated.setColor(Color.parseColor(color));
        GradientDrawable unactivated = (GradientDrawable) context.getResources().getDrawable(R.drawable.normal_background);
        unactivated.setColor(Color.parseColor(color));
        StateListDrawable stateList = new StateListDrawable();
        stateList.addState(new int[]{android.R.attr.state_activated}, activated);
        stateList.addState(new int[]{}, unactivated);

        return stateList;
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView nameView;
        TextView timeView;
        RelativeLayout mainView;

        MyViewHolder(View itemView)
        {
            super(itemView);
            nameView = itemView.findViewById(R.id.trackName);


            timeView = itemView.findViewById(R.id.trackTime);

            mainView = itemView.findViewById(R.id.trackItem);
        }


    }
}
