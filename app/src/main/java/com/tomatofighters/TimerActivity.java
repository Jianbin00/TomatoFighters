package com.tomatofighters;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
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
    private RecyclerView mRecyclerView;
    private SoundPool sp;
    private int startSP, bellSP;//ID for sounds


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

        playListId = getIntent().getIntExtra("playListId", 0);
        adapter = new TimerViewAdapter(getIntent().getStringExtra("color"));

        initDatas();
        //initSound();
        if (!adapter.getDataList().isEmpty())
        {
            remainTimeTV.setText(adapter.getDataList().get(0).getTime());
        }
        mRecyclerView = findViewById(R.id.tracks);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //mRecyclerView.addItemDecoration(new MyItemDecoration(this, MyItemDecoration.VERTICAL_LIST));


        /*ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        helper.attachToRecyclerView(mRecyclerView);*/
        mRecyclerView.setAdapter(adapter);
        playButton = findViewById(R.id.play_button);
        if (adapter.getItemCount() > 0)
        {
            playButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (!isPlay)
                    {
                        sp.play(startSP, 1, 1, 1, 0, 1);
                        initCountDownTimer(remainTime);
                        cdTimer.start();
                        playButton.setImageResource(android.R.drawable.ic_media_pause);
                        highlight(true);


                    } else
                    {
                        cdTimer.cancel();
                        playButton.setImageResource(android.R.drawable.ic_media_play);
                        downplay();
                    }
                    isPlay = !isPlay;
                }
            });
        }

        if (adapter.getDataList().size() > 0)
        {
            remainTime = timeStringToLong(adapter.getDataList().get(0).getTime());
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
        if (sp != null)
        {
            sp.release();
        }
        if (cdTimer != null)
        {
            cdTimer.cancel();
        }
    }

    private void initSound()
    {
        SoundPool.Builder spb = new SoundPool.Builder();
        spb.setMaxStreams(1);
        spb.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_ALARM).build());
        sp = spb.build();
        startSP = sp.load(this, R.raw.starting_pistol_stephan_schutze, 1);
        bellSP = sp.load(this, R.raw.service_bell_daniel_simion, 1);
    }


    protected void initCountDownTimer(long millisInFuture)
    {
        cdTimer = new CountDownTimer(millisInFuture + 500, TIMER_INTERVAL)
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
                sp.play(bellSP, 1, 1, 1, 0, 1);
                remainTime = 0;
                if (adapter.getDataList().size() > 0)
                {
                    if (adapter.getDataList().get(0).isLast)
                    {

                        remainTimeTV.setText("Done");
                        playButton.setImageResource(android.R.drawable.ic_media_play);
                        isPlay = !isPlay;
                    } else
                    {
                        highlight(false);
                        //downplay();
                        //Track movedItem = adapter.getDataList().get(0);
                        adapter.getDataList().remove(0);
                        //adapter.getDataList().add(movedItem);
                        remainTimeTV.setText(adapter.getDataList().get(0).getTime());
                        remainTime = timeStringToLong(adapter.getDataList().get(0).getTime());
                        initCountDownTimer(remainTime);
                        cdTimer.start();
                        highlight(true);
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

        dbHelper = new PlayListDBHelper();
        playList = dbHelper.queryPlayListById(playListId);
        if (playList != null)

        {
            setTitle(playList.getName());
            adapter.setDataList(dbHelper.queryTracksByPlayListId(playListId));
            int dataNum = adapter.getItemCount();
            for (int i = dataNum - 1; i >= 0; i--)
            {
                if (adapter.getDataList().get(i).getTime().equals("00:00:00"))
                {
                    adapter.getDataList().remove(i);
                }
            }
            setLast(adapter.getDataList());
        } else
        {
            Toast.makeText(this, "Can't find the ID.", Toast.LENGTH_SHORT).show();
        }
        if (adapter.getDataList().isEmpty())
        {
            remainTimeTV.setText(R.string.no_timer_msg);
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

    private void highlight(boolean activated)
    {
       /* mRecyclerView.getChildAt(0).findViewById(R.id.trackItem).setBackground(
                getResources().getDrawable(R.drawable.highlight_background)
        );
        ((TextView)mRecyclerView.getChildAt(0).findViewById(R.id.trackName)).
                setTextColor(getResources().getColor(R.color.textColorPrimary));
        ((TextView)mRecyclerView.getChildAt(0).findViewById(R.id.trackTime)).
                setTextColor(getResources().getColor(R.color.textColorPrimary));*/
        mRecyclerView.getChildAt(0).findViewById(R.id.trackItem).setActivated(activated);
    }

    private void downplay()
    {
        mRecyclerView.getChildAt(0).findViewById(R.id.trackItem).setBackground(null);
        ((TextView) mRecyclerView.getChildAt(0).findViewById(R.id.trackName)).
                setTextColor(getResources().getColor(R.color.white));
        ((TextView) mRecyclerView.getChildAt(0).findViewById(R.id.trackTime)).
                setTextColor(getResources().getColor(R.color.white));
    }

}
