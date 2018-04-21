package com.tomatofighter;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;

import java.util.List;


//import java.io.FileOutputStream;
//import java.io.IOException;

public class TimerActivity extends AppCompatActivity
{

    private final long TIMER_INTERVAL = 1000;
    //FileOutputStream out;
    private ListView mLv;
    private List<TaskItem> mDatas;
    private Toolbar toolbar;
    private TextView remainTimeTV;
    private TodoList todoList;
    private ToggleButton playButton;
    private CountDownTimer cdTimer;
    private long remainTime = 0;
    private int hour, min, sec;
    private String timeShow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        toolbar = findViewById(R.id.toolbar);
        playButton = findViewById(R.id.play_button);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mLv = findViewById(R.id.tasks);
        remainTimeTV = findViewById(R.id.remain_time);
        initDatas();

        mLv.setAdapter(new CommonAdapter<TaskItem>(this, mDatas, R.layout.item_swipe_timer)
        {
            @Override
            public void convert(final ViewHolder holder, TaskItem taskItem, final int position)
            {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉it效果
                holder.setText(R.id.activity, taskItem.getName());
                holder.setText(R.id.time_setter, taskItem.getTime());



                /*holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        mDatas.remove(position);
                        notifyDataSetChanged();
                    }
                });*/
            }
        });


    }

    public void PlayOrPause(View v)
    {

        if (playButton.isChecked())
        {
            cdTimer.cancel();
        } else
        {
            initCountDownTimer(remainTime);
            cdTimer.start();

        }
        playButton.setChecked(!playButton.isChecked());
    }

    protected void initCountDownTimer(long millisInFuture)
    {
        cdTimer = new CountDownTimer(millisInFuture, TIMER_INTERVAL)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                remainTime = millisUntilFinished;
                //The format of remainTimeTV is "hh:mm:ss".
                hour = (int) millisUntilFinished / 3600000;
                min = (int) (millisUntilFinished % 3600000) / 60000;
                sec = (int) (millisUntilFinished % 60000) / 1000;
                timeShow = String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);
                remainTimeTV.setText(timeShow);
            }

            @Override
            public void onFinish()
            {
                remainTimeTV.setText("Done");
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }


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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    private void initDatas()
    {
        todoList = getIntent().getParcelableExtra("todolist");
        setTitle(todoList.getName());
        mDatas = todoList.getTasks();
        int mDatasNum = mDatas.size();
        for (int i = mDatasNum - 1; i >= 0; i--)
        {
            if (mDatas.get(i).getTime().equals("00:00:00"))
            {
                mDatas.remove(i);
            }
        }
    }


}
