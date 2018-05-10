package com.tomatofighters;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;

public class TodoEditorActivity extends AppCompatActivity
{
    private static final short MAX_HOUR = 24;
    private ListView mLv;
    private PlayList playList;
    private int playListId;
    private List<Track> mDatas;
    private Toolbar toolbar;
    private OptionsPickerView tpview;
    private List<Short> hourList;
    private List<Short> minAndSecList;

    private PlayListDBHelper dbHelper;
    private int maxTrackId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_editor);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mLv = findViewById(R.id.tasks);

        initDatas();
        initOptions();
        mLv.setAdapter(new CommonAdapter<Track>(this, mDatas, R.layout.item_swipe_todo_editor)
        {
            @Override
            public void convert(final ViewHolder holder, final Track trackItem, final int position)
            {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
                holder.setText(R.id.playListName, trackItem.getName());
                holder.setText(R.id.time_setter, trackItem.getTime());
                holder.setOnClickListener(R.id.playListName, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        final EditText inputText = new EditText(TodoEditorActivity.this);

                        inputText.setText(trackItem.getName());
                        inputText.setMaxLines(1);
                        inputText.selectAll();
                        final AlertDialog.Builder inputDialog = new AlertDialog.Builder(TodoEditorActivity.this);
                        inputDialog.setTitle(R.string.rename)
                                .setView(inputText)
                                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        String newName = inputText.getText().toString();
                                        trackItem.setName(newName);
                                        dbHelper.setTrackName(trackItem.getId(), newName);
                                        dialogInterface.dismiss();
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        dialogInterface.dismiss();
                                    }
                                });
                        inputDialog.show();

                    }
                });


                holder.setOnClickListener(R.id.time_setter, new View.OnClickListener()
                {
                    String msg;
                    @Override
                    public void onClick(View view)
                    {
                        int[] timeDigits = TimeConverter.timeStringToInts(trackItem.getTime());
                        //TODO:dialog need to adjust.
                        tpview=new OptionsPickerView.Builder(TodoEditorActivity.this,new OptionsPickerView.OnOptionsSelectListener()
                        {
                            @Override
                            public void onOptionsSelect(int option1,int option2,int option3,View v)
                            {

                                msg = String.format("%02d", option1) + ":" + String.format("%02d", option2) + ":" + String.format("%02d", option3);
                                trackItem.setTime(msg);
                                dbHelper.setTrackTime(trackItem.getId(), msg);
                                if (trackItem.isLast && !msg.equals(getResources().getString(R.string.default_time)))
                                {
                                    if (maxTrackId < 0)
                                    {
                                        maxTrackId = dbHelper.findMaxTrackId() + 1;
                                    } else
                                    {
                                        maxTrackId++;

                                    }
                                    mDatas.add(dbHelper.insertNewTrack(maxTrackId));
                                    trackItem.isLast = false;
                                    setLast(mDatas);
                                }
                                notifyDataSetChanged();
                            }
                        })
                                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener()
                                {
                                    @Override
                                    public void customLayout(View v)
                                    {
                                        TextView tvSubmit = v.findViewById(R.id.tv_finish);
                                        TextView tvCancel = v.findViewById(R.id.iv_cancel);
                                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                tpview.returnData();
                                            }
                                        });
                                        tvCancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                tpview.dismiss();
                                            }
                                        });

                                    }
                                })
                                .setTitleText(getResources().getString(R.string.select_time))
                                .setLabels("Hour","Min","Sec")
                                .isCenterLabel(false)
                                .setSelectOptions(timeDigits[0], timeDigits[1], timeDigits[2])
                                .setContentTextSize(30)
                                .setDividerColor(Color.TRANSPARENT)
                                .setBgColor(Color.WHITE)
                                .setTitleBgColor(Color.DKGRAY)
                                .setTitleColor(Color.LTGRAY)
                                //.setCancelColor(Color.YELLOW)
                                //.setSubmitColor(Color.YELLOW)
                                //.setTextColorCenter(Color.BLACK)
                                .setCyclic(true, true, true)
                                .setOutSideCancelable(true)
                                .isDialog(true)
                                .build();
                        tpview.setNPicker(hourList,minAndSecList,minAndSecList);
                        tpview.show();
                    }


                });
                holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();

                        if (mDatas.size() > 1)
                        {
                            dbHelper.deleteTrack(trackItem.getId());
                            mDatas.remove(position);
                            setLast(mDatas);
                            notifyDataSetChanged();
                        } else
                        {
                            Toast.makeText(TodoEditorActivity.this, "Can't delete it. At least one item should be in the scene.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        
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
                Intent i=new Intent(TodoEditorActivity.this,TimerActivity.class);
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
            mDatas = dbHelper.queryTracksByPlayListId(playListId);
        } else
        {
            Toast.makeText(this, "Can't find the ID.", Toast.LENGTH_SHORT).show();
        }

        setLast(mDatas);
    }

    private void setLast(List<Track> tracks)
    {
        int dataNum = tracks.size();
        if (dataNum > 0)
        {
            tracks.get(dataNum - 1).isLast = true;
        }
    }

    private void initOptions()
    {
        hourList=new ArrayList<>();
        minAndSecList=new ArrayList<>();
        for(short i=0;i<=MAX_HOUR;i++)
        {
            hourList.add(i);
        }
        for(short i=0;i<60;i++)
        {
            minAndSecList.add(i);
        }

    }

}
