package com.tomatofighters;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatofighters.Controllers.MyItemDecoration;
import com.tomatofighters.DB.PlayListDBHelper;
import com.tomatofighters.Models.PlayList;
import com.tomatofighters.Models.Track;
import com.tomatofighters.Views.TimerViewAdapter;

import java.util.List;

import static com.tomatofighters.TimeConverter.millisToString;
import static com.tomatofighters.TimeConverter.timeStringToLong;


//import java.io.FileOutputStream;
//import java.io.IOException;

public class TimerActivity extends AppCompatActivity
{

    private final long TIMER_INTERVAL = 1000;
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
    private TimerViewAdapter adapter;

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
        remainTimeTV = findViewById(R.id.remain_time);

        adapter = new TimerViewAdapter();
        mDatas = adapter.getDataList();
        initDatas();
        if (!mDatas.isEmpty())
        {
            remainTimeTV.setText(mDatas.get(0).getTime());
        }
        RecyclerView mRecyclerView = findViewById(R.id.tracks);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new MyItemDecoration(this, MyItemDecoration.VERTICAL_LIST));


        /*ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        helper.attachToRecyclerView(mRecyclerView);*/
        mRecyclerView.setAdapter(adapter);
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
                        remainTimeTV.setText(mDatas.get(0).getTime());
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
            adapter.setDataList(dbHelper.queryTracksByPlayListId(playListId));
            int dataNum = adapter.getItemCount();
            mDatas = adapter.getDataList();
            for (int i = dataNum - 1; i >= 0; i--)
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
