package com.tomatofighter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private List<TaskItem> mDatas;
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
        mLv.setAdapter(new CommonAdapter<TaskItem>(this, mDatas, R.layout.item_swipe)
        {
            @Override
            public void convert(final ViewHolder holder, TaskItem taskItem, final int position)
            {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
                holder.setText(R.id.content, taskItem.name);
                //TODO:Set the listener.
                holder.setOnClickListener(R.id.content, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        i=new Intent(PlayListActivity.this,TodoEditorActivity.class);
                        startActivity(i);
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play_list, menu);
        return true;
    }

    //TODO:The fuction for add a new task.
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
            i=new Intent(PlayListActivity.this,TodoEditorActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //TODO:Implement the loading method.
    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(new TaskItem("" + i));
        }
    }
}