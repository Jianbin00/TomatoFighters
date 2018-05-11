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
    InputMethodManager inputManager;
    /*    private ListView mLv;
        private CommonAdapter mAdapter;
        private List<PlayList> mDatas;*/
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
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        //mLv = findViewById(R.id.playLists);
        doFirst();
        adapter = new PlayListViewAdapter();
        dbHelper = new PlayListDBHelper();
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
                i = new Intent(PlayListActivity.this, TodoEditorActivity.class);
                i.putExtra("playlistId", adapter.getDataList().get(position).getId());
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


        /*mAdapter = new CommonAdapter<PlayList>(this, mDatas, R.layout.item_swipe_play_list)
        {


            @Override
            public void convert(final ViewHolder holder, final PlayList playlist, final int position)
            {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
                holder.setText(R.id.playListName, playlist.getName());

                final GestureDetector gestureDetector = new GestureDetector(PlayListActivity.this, new GestureDetector.SimpleOnGestureListener()
                {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e)
                    {

                        i = new Intent(PlayListActivity.this, TodoEditorActivity.class);
                        i.putExtra("playlistId", playlist.getId());
                        startActivity(i);

                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e)
                    {
                        final EditText inputText = new EditText(PlayListActivity.this);
                        inputManager = (InputMethodManager) inputText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                                        String name = inputText.getText().toString();
                                        playlist.setName(name);
                                        dbHelper.setPlayListName(playlist.getId(), name);
                                        inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                                        dialogInterface.dismiss();
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
                        inputText.setText(playlist.getName());
                        inputText.selectAll();
                        //TODO:When click the view, the content of EditText is selected but no keyboard come out.
                        //popUpInputMethod(inputManager, inputText);
                        inputManager.showSoftInput(inputText, InputMethodManager.SHOW_FORCED);


                        return super.onDoubleTap(e);
                    }


                });

                holder.setOnTouchListener(R.id.playListName, new View.OnTouchListener()
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
                        //Toast.makeText(PlayListActivity.this, "删除:" + position, Toast.LENGTH_SHORT).show();
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        dbHelper.deletePlayList(playlist.getId());
                        mDatas.remove(position);

                        notifyDataSetChanged();
                    }
                });
            }


        };


        mLv.setAdapter(mAdapter);*/

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
            //mAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initRealm()
    {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("data.dbHelper")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

    }

    public void initDatas()
    {

        adapter.setDataList(dbHelper.queryAllPlayLists());
/*        mDatas.addChangeListener(new RealmChangeListener<RealmList<PlayList>>()
        {
            @Override
            public void onChange(RealmList<PlayList> playLists)
            {
                if(mAdapter!=null)
                {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });*/

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
        initRealm();
    }



/*    private void initDatas() {
        mDatas = new ArrayList<>();
        TodoList newTodoList=new TodoList("TODO");
        newTodoList.addNewTasks(new ArrayList<TaskItem>(),5);
        mDatas.add(newTodoList);
    }*/

   /* private void initDatas()

    {
        db = AppDatabase.getDatabase(getApplicationContext());
        dbHelper = new AppDatabaseHelper(db);
        mDatas = dbHelper.getPlayLists().getValue();
        Log.d("Test", "" + (mDatas == null));*/



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