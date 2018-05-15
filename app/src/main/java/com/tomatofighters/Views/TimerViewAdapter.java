package com.tomatofighters.Views;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.tomatofighters.DB.PlayListDBHelper;
import com.tomatofighters.Models.Track;
import com.tomatofighters.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Jianbin Li
 * Assignment
 * ${FILE_NAME}.
 */

public class TimerViewAdapter extends RecyclerViewAdapter
{
    private static final short MAX_HOUR = 9;
    private final static String DEFAULT_TIME = "00:00:00";
    private final static String SELECT_TIME = "Select the time";
    private List<Track> dataList = new ArrayList<>();
    private PlayListDBHelper dbHelper;
    private OptionsPickerView tpview;
    private int maxTrackId = -1;
    private List<Short> hourList;
    private List<Short> minAndSecList;
    private String color;


    public TimerViewAdapter(String color)
    {
        super();
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
        ((MyViewHolder) holder).mainView.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    @Override
    public void removeItem(int position)
    {
        dataList.remove(position);
    }


    private List<Track> setLast(List<Track> tracks)
    {
        int dataNum = tracks.size();
        if (dataNum > 0)
        {
            tracks.get(dataNum - 1).isLast = true;
        }
        return tracks;
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
