package com.tomatofighters.Controllers;

import android.support.v7.widget.RecyclerView;

import com.tomatofighters.DB.PlayListDBHelper;
import com.tomatofighters.Views.TrackViewAdapter;

import java.util.Collections;

/**
 * Jianbin Li
 * Assignment
 * ${FILE_NAME}.
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
        recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
        PlayListDBHelper dbHelper = new PlayListDBHelper();
        dbHelper.swapTrack(playListId, fromPosition, toPosition);
        return true;
    }
}
