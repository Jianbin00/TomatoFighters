package com.tomatofighters.Controllers;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.tomatofighters.DB.PlayListDBHelper;
import com.tomatofighters.Views.TrackViewAdapter;

import java.util.Collections;

/**
 * Jianbin Li
 *
 */

public class TrackItemTouchHelperCallback extends ItemTouchHelperCallback
{
    private final TrackViewAdapter adapter;
    private int playListId;

    public TrackItemTouchHelperCallback(TrackViewAdapter adapter, int playListId)
    {
        super(adapter);
        this.adapter = adapter;
        this.playListId = playListId;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target)
    {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        adapter.getDataList().get(fromPosition).isLast = false;
        if (fromPosition < toPosition)
        {
            for (int i = fromPosition; i < toPosition; i++)
            {
                Collections.swap(adapter.getDataList(), i, i + 1);
            }
        } else
        {
            for (int i = fromPosition; i > toPosition; i--)
            {
                Collections.swap(adapter.getDataList(), i, i - 1);
            }
        }

        adapter.notifyItemMoved(fromPosition, toPosition);
        adapter.setLast();
        PlayListDBHelper dbHelper = new PlayListDBHelper();
        dbHelper.swapTrack(playListId, fromPosition, toPosition);

        return true;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.START)
        {

            adapter.removeItem(position);
            adapter.getDataList().remove(position);
            adapter.notifyItemRemoved(position);
            adapter.setLast();
        }
    }
}
