package com.tomatofighter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.List;

public class TodoEditorActivity extends AppCompatActivity
{
    private final short MAX_HOUR = 99;
    private ListView mLv;
    private TodoList todoList;
    private List<TaskItem> mDatas;
    private Toolbar toolbar;
    private OptionsPickerView tpview;
    private ArrayList<Short> hourList;
    private ArrayList<Short> minAndSecList;

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
        mLv.setAdapter(new CommonAdapter<TaskItem>(this, mDatas, R.layout.item_swipe_todo_editor)
        {
            @Override
            public void convert(final ViewHolder holder, TaskItem taskItem, final int position)
            {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
                holder.setText(R.id.activity, taskItem.name);
                holder.setText(R.id.time_setter, taskItem.time);
                //TODO:Set the listener.
                holder.setOnClickListener(R.id.time_setter, new View.OnClickListener()
                {
                    String msg;
                    @Override
                    public void onClick(View view)
                    {
                        tpview=new OptionsPickerView.Builder(TodoEditorActivity.this,new OptionsPickerView.OnOptionsSelectListener()
                        {
                            @Override
                            public void onOptionsSelect(int option1,int option2,int option3,View v)
                            {

                                msg=option1+":"+option2+":"+option3;
                                Toast.makeText(TodoEditorActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                .setSelectOptions(1,1,1)
                                .setContentTextSize(50)
                                .setDividerColor(Color.TRANSPARENT)
                                .setBgColor(Color.BLACK)
                                .setTitleBgColor(Color.DKGRAY)
                                .setTitleColor(Color.LTGRAY)
                                .setCancelColor(Color.YELLOW)
                                .setSubmitColor(Color.YELLOW)
                                .setTextColorCenter(Color.LTGRAY)
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
                        mDatas.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo_editor, menu);
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
            case R.id.action_play:
                Intent i=new Intent(TodoEditorActivity.this,TimerActivity.class);
                startActivity(i);
                return true;
            case android.R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    //TODO:Implement the loading method.
    private void initDatas() {
        /*mDatas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mDatas.add(new TaskItem("" + i));
        }*/

        todoList = getIntent().getParcelableExtra("todolist");
        setTitle(todoList.getName());
        mDatas = todoList.getTasks();
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
