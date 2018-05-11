package com.tomatofighters;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;

import java.util.List;

import static com.tomatofighters.TimeConverter.millisToString;
import static com.tomatofighters.TimeConverter.timeStringToLong;


//import java.io.FileOutputStream;
//import java.io.IOException;

public class TimerActivity extends AppCompatActivity
{

    private final long TIMER_INTERVAL = 1000;
    //FileOutputStream out;
    private ListView mLv;
    private CommonAdapter<Track> CAdapter;
    private List<Track> mDatas;
    private Toolbar toolbar;
    private TextView remainTimeTV;
    private PlayList playList;
    private ImageButton playButton;
    private CountDownTimer cdTimer;
    private long remainTime;
    private int hour, min, sec;
    private String timeShow;
    private boolean isPlay, isLast;
    private int playListId;
    private PlayListDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mLv = findViewById(R.id.tasks);
        remainTimeTV = findViewById(R.id.remain_time);
        initDatas();

        CAdapter = new CommonAdapter<Track>(this, mDatas, R.layout.item_swipe_timer)
        {
            @Override
            public void convert(final ViewHolder holder, Track taskItem, final int position)
            {
                holder.setText(R.id.playListName, taskItem.getName());
                holder.setText(R.id.time_setter, taskItem.getTime());
            }
        };
        mLv.setAdapter(CAdapter);
        playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!isPlay)
                {
                    initCountDownTimer(remainTime);
                    cdTimer.start();
                    playButton.setImageResource(android.R.drawable.ic_media_pause);

                } else
                {
                    cdTimer.cancel();
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                }
                isPlay = !isPlay;
            }
        });
        if (mDatas.size() > 0)
        {
            remainTime = timeStringToLong(mDatas.get(0).getTime());
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (dbHelper != null)
        {
            dbHelper.close();
        }
    }


    protected void initCountDownTimer(long millisInFuture)
    {
        cdTimer = new CountDownTimer(millisInFuture, TIMER_INTERVAL)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                remainTime = millisUntilFinished;
                remainTimeTV.setText(millisToString(millisUntilFinished));
            }

            @Override
            public void onFinish()
            {
                remainTime = 0;
                remainTimeTV.setText(millisToString(remainTime));
                if (mDatas.size() > 0)
                {
                    if (mDatas.get(0).isLast)
                    {
                        remainTimeTV.setText("Done");
                    } else
                    {
                        Track movedItem = mDatas.get(0);
                        mDatas.remove(0);
                        mDatas.add(movedItem);
                        CAdapter.notifyDataSetChanged();
                        remainTime = timeStringToLong(mDatas.get(0).getTime());
                        initCountDownTimer(remainTime);
                        cdTimer.start();
                    }
                }

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
        playListId = getIntent().getIntExtra("playListId", 0);
        dbHelper = new PlayListDBHelper();
        playList = dbHelper.queryPlayListById(playListId);
        if (playList != null)

        {
            setTitle(playList.getName());
            mDatas = dbHelper.queryTracksByPlayListId(playListId);
            int mDatasNum = mDatas.size();
            for (int i = mDatasNum - 1; i >= 0; i--)
            {
                if (mDatas.get(i).getTime().equals("00:00:00"))
                {
                    mDatas.remove(i);
                }
            }
            setLast(mDatas);
        } else
        {
            Toast.makeText(this, "Can't find the ID.", Toast.LENGTH_SHORT).show();
        }


    }

    private void setLast(List<Track> tasks)
    {
        int dataNum = tasks.size();
        if (dataNum > 0)
        {
            tasks.get(dataNum - 1).isLast = true;
        }
    }


}
