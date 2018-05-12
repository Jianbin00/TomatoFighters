package com.tomatofighters.Controllers;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class OnItemClickListener extends GestureDetector.SimpleOnGestureListener
        implements RecyclerView.OnItemTouchListener
{

    private final RecyclerView recyclerView;
    private GestureDetectorCompat mGestureDetector;

    public OnItemClickListener(final RecyclerView recyclerView)
    {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), this);
    }

    public abstract void onItemLongClick(RecyclerView.ViewHolder viewHolder, int position);

    public abstract void onItemClick(RecyclerView.ViewHolder viewHolder, int position);

    public abstract void onItemDoubleClick(RecyclerView.ViewHolder viewHolder, int position);

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e)
    {
        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null)
        {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(childView);
            onItemClick(viewHolder, recyclerView.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null)
        {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(childView);
            onItemLongClick(viewHolder, recyclerView.getChildAdapterPosition(childView));
        }
    }

    @Override
    public boolean onDoubleTap(MotionEvent e)
    {
       /* int action = e.getAction();
        if (action == MotionEvent.ACTION_UP) {*/
        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null)
        {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(childView);
            onItemDoubleClick(viewHolder, recyclerView.getChildLayoutPosition(childView));
            return true;
        }
        //}
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
    {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e)
    {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
    {

    }

}
