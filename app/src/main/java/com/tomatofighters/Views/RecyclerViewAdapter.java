package com.tomatofighters.Views;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Jianbin Li
 *
 */

public abstract class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    public abstract List getDataList();

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public abstract int getItemCount();

    public abstract void removeItem(int position);

}
