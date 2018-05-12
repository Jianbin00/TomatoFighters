package com.tomatofighters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tomatofighters.Controllers.MyItemDecoration;
import com.tomatofighters.Controllers.TrackItemTouchHelperCallback;
import com.tomatofighters.DB.PlayListDBHelper;
import com.tomatofighters.Models.PlayList;
import com.tomatofighters.Models.Track;
import com.tomatofighters.Views.TrackViewAdapter;

import java.util.List;

public class TrackActivity extends AppCompatActivity
{

    private PlayList playList;
    private int playListId;
    private Toolbar toolbar;
    private TrackViewAdapter adapter;
    private PlayListDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        adapter = new TrackViewAdapter();


        initDatas();
        RecyclerView mRecyclerView = findViewById(R.id.tracks);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new MyItemDecoration(this, MyItemDecoration.VERTICAL_LIST));


        ItemTouchHelper helper = new ItemTouchHelper(new TrackItemTouchHelperCallback(adapter, playListId));
        helper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        
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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo_editor, menu);
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
            case R.id.action_play:
                Intent i = new Intent(TrackActivity.this, TimerActivity.class);
                i.putExtra("playListId", playListId);
                startActivity(i);
                return true;
            case android.R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void initDatas() {
        playListId = getIntent().getIntExtra("playlistId", 0);
        dbHelper = new PlayListDBHelper();
        playList = dbHelper.queryPlayListById(playListId);
        if (playList != null)

        {
            setTitle(playList.getName());
            adapter.setDataList(setLast(dbHelper.queryTracksByPlayListId(playListId)));
        } else
        {
            Toast.makeText(this, "Can't find the ID.", Toast.LENGTH_SHORT).show();
        }


    }

    private List<Track> setLast(List<Track> tracks)
    {
        int dataNum = tracks.size();
        if (dataNum > 0)
        {
            tracks.get(dataNum - 1).isLast = true;
        }
        return tracks;
    }



}
