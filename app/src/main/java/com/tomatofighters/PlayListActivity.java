package com.tomatofighters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tomatofighters.Controllers.ItemTouchHelperCallback;
import com.tomatofighters.Controllers.MyItemDecoration;
import com.tomatofighters.Controllers.OnItemClickListener;
import com.tomatofighters.DB.PlayListDBHelper;
import com.tomatofighters.Models.PlayList;
import com.tomatofighters.Views.PlayListViewAdapter;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/*
Author: Jianbin Li
Name: PlayListActivity.java
This is the launcher activity.

 */
public class PlayListActivity extends AppCompatActivity
{


    private final static String SHAREDPREFERENCES_TAG = "mypreference";
    private InputMethodManager inputManager;
    private PlayListViewAdapter adapter;
    private PlayListDBHelper dbHelper;
    private Toolbar toolbar;
    private Intent i;
    private int maxPlayListId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //doFirst();
        initRealm();
        adapter = new PlayListViewAdapter();

        initDatas();
        RecyclerView mRecyclerView = findViewById(R.id.playLists);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new MyItemDecoration(this, MyItemDecoration.VERTICAL_LIST));

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener(mRecyclerView)
        {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, int position)
            {
                i = new Intent(PlayListActivity.this, TrackActivity.class);
                i.putExtra("playlistId", adapter.getDataList().get(position).getId());
                i.putExtra("color", adapter.getViewHolderColor(position));
                startActivity(i);
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder, int position)
            {

            }

            @Override
            public void onItemDoubleClick(RecyclerView.ViewHolder viewHolder, int position)
            {

                final EditText inputText = new EditText(PlayListActivity.this);
                inputManager = (InputMethodManager) inputText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputText.setMaxLines(1);
                inputText.setSingleLine(true);
                PlayList playList = adapter.getDataList().get(position);

                final AlertDialog.Builder inputDialog = new AlertDialog.Builder(PlayListActivity.this);
                inputDialog.setTitle(R.string.rename)
                        .setView(inputText)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                String name = inputText.getText().toString();

                                playList.setName(name);
                                dbHelper.setPlayListName(playList.getId(), name);
                                inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                                dialogInterface.dismiss();
                                //adapter.notifyDataSetChanged();
                                adapter.notifyItemChanged(position);
                                //dismissInputMethod(inputManager, inputText);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                                dialogInterface.dismiss();
                                //dismissInputMethod(inputManager, inputText);
                            }
                        });


                inputText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                inputDialog.show();
                inputText.setText(playList.getName());
                inputText.selectAll();
                //TODO:When click the view, the content of EditText is selected but no keyboard come out.
                //popUpInputMethod(inputManager, inputText);
                inputManager.showSoftInput(inputText, InputMethodManager.SHOW_FORCED);


            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
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
            if (maxPlayListId < 0)
            {
                maxPlayListId = dbHelper.findMaxPlayListId() + 1;
            } else
            {
                maxPlayListId++;
            }
            adapter.getDataList().add(dbHelper.insertNewPlayListAndTracks(maxPlayListId));
            //adapter.notifyDataSetChanged();
            adapter.notifyItemInserted(adapter.getItemCount());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initRealm()
    {
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("data.dbHelper")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

    }

    public void initDatas()
    {
        dbHelper = new PlayListDBHelper();
        adapter.setDataList(dbHelper.queryAllPlayLists());


    }

    private void doFirst()
    {
/*        SharedPreferences settings=getSharedPreferences(SHAREDPREFERENCES_TAG,0);
        if(settings.getBoolean("FIRST",true))
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("FIRST",false);
            editor.apply();
            initRealm();
        }*/

    }



}