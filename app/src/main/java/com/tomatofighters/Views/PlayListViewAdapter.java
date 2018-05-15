package com.tomatofighters.Views;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomatofighters.DB.PlayListDBHelper;
import com.tomatofighters.Models.PlayList;
import com.tomatofighters.R;

import java.util.ArrayList;
import java.util.List;

public class PlayListViewAdapter extends RecyclerViewAdapter
{

    private List<PlayList> dataList = new ArrayList<>();
    private String[] colors = {"#D09AE7", "#FD9927", "#6ACA6B", "#6BCDFD", "#FD9BCB"};
    private String color;


    @Override
    public List<PlayList> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<PlayList> dataList)
    {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe_play_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ((MyViewHolder) holder).tv.setText(dataList.get(position).getName());
        color = colors[position % colors.length];
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
        PlayListDBHelper dbHelper = new PlayListDBHelper();
        dbHelper.deletePlayList(dataList.get(position).getId());
        dbHelper.close();
    }

    public String getViewHolderColor(int position)
    {

        return color;
    }


    private static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tv;
        RelativeLayout mainView;
        //String color;

        MyViewHolder(View itemView)
        {
            super(itemView);
            tv = itemView.findViewById(R.id.playListName);
            mainView = itemView.findViewById(R.id.playListItem);
        }


    }

}
