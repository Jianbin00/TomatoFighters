package com.tomatofighters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.LinkedList;
import java.util.List;

/*
Author: Jianbin Li
Name: PlayListActivity.java
This is the launcher activity.

 */
public class PlayListActivity extends AppCompatActivity
{
    private final static String DEFAULT_DB_NAME = "data.db";
    private ListView mLv;
    private CommonAdapter mAdapter;
    private List<PlayList> mDatas;
    //private LiveData<List<PlayList>> lDatas;
    private Toolbar toolbar;
    private Intent i;
    private AppDatabaseHelper dbHelper;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLv = findViewById(R.id.tasks);
        initDatas();
        mAdapter = new CommonAdapter<PlayList>(this, mDatas, R.layout.item_swipe_play_list)
        {


            @Override
            public void convert(final ViewHolder holder, final PlayList playlist, final int position)
            {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
                holder.setText(R.id.activity, playlist.getName());

                final GestureDetector gestureDetector = new GestureDetector(PlayListActivity.this, new GestureDetector.SimpleOnGestureListener()
                {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e)
                    {

                        i = new Intent(PlayListActivity.this, TodoEditorActivity.class);
                        i.putExtra("playlistId", position);
                        startActivity(i);

                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e)
                    {
                        final EditText inputText = new EditText(PlayListActivity.this);
                        InputMethodManager inputManager = (InputMethodManager) inputText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputText.setMaxLines(1);
                        inputText.setSingleLine(true);

                        final AlertDialog.Builder inputDialog = new AlertDialog.Builder(PlayListActivity.this);
                        inputDialog.setTitle(R.string.rename)
                                .setView(inputText)
                                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        playlist.setName(inputText.getText().toString());
                                        dialogInterface.dismiss();
                                        dismissInputMethod(inputManager, inputText);
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        dialogInterface.dismiss();
                                        dismissInputMethod(inputManager, inputText);
                                    }
                                });


                        inputText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                        inputDialog.show();
                        inputText.setText(playlist.getName());
                        inputText.selectAll();
                        //TODO:When click the view, the content of EditText is selected but no keyboard come out.
                        popUpInputMethod(inputManager, inputText);



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
    public void onDestroy()
    {
        super.onDestroy();
        if (db != null)
        {
            db.close();
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
            List<Track> newTracks = new LinkedList<>();
            PlayList newplayList = new PlayList("TODO");
            /*toBeAdded.add(newplayList);
            mAdapter.addDatas(toBeAdded);*/

            AsyncTask.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        int playListId = (int) db.PlayListDAO().insertPlayList(newplayList);
                        for (int i = 0; i < 5; i++)
                        {
                            newTracks.add(new Track(0, "TODO " + i, "00:00:00", playListId));

                        }
                        db.PlayListDAO().insertTracks(newTracks);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });



            return true;
        }
        return super.onOptionsItemSelected(item);
    }



/*    private void initDatas() {
        mDatas = new ArrayList<>();
        TodoList newTodoList=new TodoList("TODO");
        newTodoList.addNewTasks(new ArrayList<TaskItem>(),5);
        mDatas.add(newTodoList);
    }*/

    private void initDatas()

    {
        db = AppDatabase.getDatabase(getApplicationContext());
        dbHelper = new AppDatabaseHelper(db);
        mDatas = dbHelper.getPlayLists().getValue();
        Log.d("Test", "" + (mDatas == null));



/*        mDatas=new ArrayList<>();
        try
        {
            stream = getResources().openRawResource(R.raw.initplaylist);
            doc = TimerXmlParser.getDocument(stream);
            Node root=TimerXmlParser.getRootNode(doc);
            int num=root.getChildNodes().getLength();
            for(int i=0;i<num;i++)
            {
                mDatas.add(new PlayList(TimerXmlParser.getPlayListName(doc,i)));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, getResources().getText(R.string.xml_read_error), Toast.LENGTH_SHORT).show();
        }*/

    }

    private void popUpInputMethod(InputMethodManager im, EditText inputText)
    {
        if (im != null)
        {
            im.showSoftInput(inputText, InputMethodManager.SHOW_FORCED);
        }
    }

    private void dismissInputMethod(InputMethodManager im, EditText inputText)
    {
        if (im != null)
        {
            im.showSoftInput(inputText, InputMethodManager.SHOW_IMPLICIT);
        }
    }


}