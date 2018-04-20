package com.tomatofighter;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;

/*
Author: Jianbin Li
Name: PlayListActivity.java
This is the launcher activity.

 */
public class PlayListActivity extends AppCompatActivity
{
    private ListView mLv;
    private CommonAdapter mAdapter;
    private List<TodoList> mDatas;
    private Toolbar toolbar;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLv = findViewById(R.id.tasks);

        initDatas();
        mAdapter = new CommonAdapter<TodoList>(this, mDatas, R.layout.item_swipe_play_list)
        {


            @Override
            public void convert(final ViewHolder holder, final TodoList tdlist, final int position)
            {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
                holder.setText(R.id.activity, tdlist.getName());

                final GestureDetector gestureDetector = new GestureDetector(PlayListActivity.this, new GestureDetector.SimpleOnGestureListener()
                {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e)
                    {//单击事件

                        i = new Intent(getApplicationContext(), TodoEditorActivity.class);
                        i.putExtra("todolist", tdlist);
                        startActivity(i);

                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e)
                    {
                        final EditText inputText = new EditText(PlayListActivity.this);
                        final AlertDialog.Builder inputDialog = new AlertDialog.Builder(PlayListActivity.this);
                        inputDialog.setTitle(R.string.rename)
                                .setView(inputText)
                                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        tdlist.setName(inputText.getText().toString());
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        dialogInterface.dismiss();
                                    }
                                });
                        inputDialog.show();

                        return super.onDoubleTap(e);
                    }


                });

                holder.setOnTouchListener(R.id.activity, new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent)
                    {
                        return gestureDetector.onTouchEvent(motionEvent);
                    }
                });



                holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(PlayListActivity.this, "删除:" + position, Toast.LENGTH_SHORT).show();
                        //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时关闭，调用这句话
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        mDatas.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }


        };
        mLv.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add)
        {
            List<TodoList> toBeAdded = new ArrayList<TodoList>();
            toBeAdded.add(new TodoList("TODO"));
            mAdapter.addDatas(toBeAdded);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initDatas() {
        mDatas = new ArrayList<>();
        mDatas.add(new TodoList("TODO"));
    }




}