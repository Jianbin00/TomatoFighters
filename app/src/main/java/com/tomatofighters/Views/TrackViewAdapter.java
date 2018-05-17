package com.tomatofighters.Views;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.tomatofighters.DB.PlayListDBHelper;
import com.tomatofighters.Models.Track;
import com.tomatofighters.R;
import com.tomatofighters.TimeConverter;
import com.tomatofighters.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Jianbin Li
 * Assignment
 * ${FILE_NAME}.
 */

public class TrackViewAdapter extends RecyclerViewAdapter
{
    private static final short MAX_HOUR = 9;
    private final static String DEFAULT_TIME = "00:00:00";
    private final static String SELECT_TIME = "Select the time";
    private List<Track> dataList = new ArrayList<>();
    private PlayListDBHelper dbHelper;
    private OptionsPickerView tpview;

    private List<Short> hourList;
    private List<Short> minAndSecList;

    @Override
    public List<Track> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<Track> dataList)
    {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe_track, parent, false);
        return new TrackViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Track data = dataList.get(position);
        ((TrackViewAdapter.MyViewHolder) holder).nameView.setText(data.getName());

        ((TrackViewAdapter.MyViewHolder) holder).nameView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final EditText inputText = new EditText(v.getContext());

                inputText.setText(data.getName());
                inputText.setMaxLines(1);
                inputText.selectAll();
                final AlertDialog.Builder inputDialog = new AlertDialog.Builder(v.getContext());
                inputDialog.setTitle(R.string.rename)
                        .setView(inputText)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                String newName = inputText.getText().toString();
                                data.setName(newName);
                                dbHelper = new PlayListDBHelper();
                                dbHelper.setTrackName(data.getId(), data.getPlayListId(), newName);
                                dbHelper.close();
                                dialogInterface.dismiss();
                                notifyItemChanged(position);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.dismiss();
                            }
                        })
                ;
                inputText.setImeOptions(EditorInfo.IME_ACTION_SEND);
                inputDialog.show();
                Util.showSoftKeyboard(inputText);
            }
        });
        ((TrackViewAdapter.MyViewHolder) holder).timeView.setText(data.getTime());
        ((TrackViewAdapter.MyViewHolder) holder).timeView.setOnClickListener(new View.OnClickListener()
        {
            String msg;

            @Override
            public void onClick(View v)
            {
                int[] timeDigits = TimeConverter.timeStringToInts(data.getTime());

                tpview = new OptionsPickerView.Builder(v.getContext(), new OptionsPickerView.OnOptionsSelectListener()
                {
                    @Override
                    public void onOptionsSelect(int option1, int option2, int option3, View v)
                    {

                        msg = TimeConverter.timeToString(option1, option2, option3);
                        data.setTime(msg);
                        dbHelper = new PlayListDBHelper();
                        dbHelper.setTrackTime(data.getId(), data.getPlayListId(), msg);
                        if (data.isLast && !msg.equals(DEFAULT_TIME))
                        {
                            dataList.add(dbHelper.insertNewTrack(data.getPlayListId()));
                            data.isLast = false;
                            setLast(dataList);
                        }
                        dbHelper.close();
                        notifyItemChanged(position);
                    }
                })
                        .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener()
                        {
                            @Override
                            public void customLayout(View v)
                            {
                                TextView tvSubmit = v.findViewById(R.id.tv_finish);
                                TextView tvCancel = v.findViewById(R.id.iv_cancel);
                                tvSubmit.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        tpview.returnData();
                                    }
                                });
                                tvCancel.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        tpview.dismiss();
                                    }
                                });

                            }
                        })
                        .setTitleText(SELECT_TIME)
                        .setLabels("Hour", "Min", "Sec")
                        .isCenterLabel(true)
                        .setSelectOptions(timeDigits[0], timeDigits[1], timeDigits[2])
                        .setContentTextSize(20)
                        .setDividerColor(Color.TRANSPARENT)
                        .setBgColor(Color.WHITE)
                        .setTitleBgColor(Color.DKGRAY)
                        .setTitleColor(Color.LTGRAY)
                        .setCyclic(true, true, true)
                        .setOutSideCancelable(true)
                        .isDialog(true)
                        .build();
                initOptions();
                tpview.setNPicker(hourList, minAndSecList, minAndSecList);
                tpview.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    @Override
    public void removeItem(int position)
    {
        PlayListDBHelper dbHelper = new PlayListDBHelper();
        dbHelper.deleteTrack(dataList.get(position).getId(), dataList.get(position).getPlayListId());
        dbHelper.close();
    }


    private void setLast(List<Track> tracks)
    {
        int dataNum = tracks.size();
        if (dataNum > 0)
        {
            tracks.get(dataNum - 1).isLast = true;
        }
    }

    public void setLast()
    {
        setLast(dataList);
    }

    private void initOptions()
    {
        hourList = new ArrayList<>();
        minAndSecList = new ArrayList<>();
        for (short i = 0; i <= MAX_HOUR; i++)
        {
            hourList.add(i);
        }
        for (short i = 0; i < 60; i++)
        {
            minAndSecList.add(i);
        }

    }

    private static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView nameView, timeView;
        RelativeLayout mainView, allView;

        MyViewHolder(View itemView)
        {
            super(itemView);
            nameView = itemView.findViewById(R.id.trackName);


            timeView = itemView.findViewById(R.id.trackTime);

            mainView = itemView.findViewById(R.id.trackMain);
            allView = itemView.findViewById(R.id.trackItem);

        }


    }
}
