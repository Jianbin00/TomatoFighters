package com.tomatofighter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;


//import java.io.FileOutputStream;
//import java.io.IOException;

public class TimerActivity extends AppCompatActivity
{

    //FileOutputStream out;
    private ListView mLv;
    private List<TaskItem> mDatas;
    private Toolbar toolbar;
    private TextView timer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mLv = findViewById(R.id.tasks);
        timer=findViewById(R.id.remain_time);
        initDatas();

        mLv.setAdapter(new CommonAdapter<TaskItem>(this, mDatas, R.layout.item_swipe_for_timer)
        {
            @Override
            public void convert(final ViewHolder holder, TaskItem taskItem, final int position)
            {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉it效果
                holder.setText(R.id.activity, taskItem.name);
                //TODO:Set the listener.


                holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
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
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    //TODO:The fuction for add a new task.
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //TODO:Implement the loading method.
    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mDatas.add(new TaskItem("" + i));
        }
    }




}
