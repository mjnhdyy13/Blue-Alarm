package com.hfad.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {



    private List<Alarm> mlistalarm;
    private OnCallBack mlistener;
    private OnCallBack2 mlistener2;
    public AlarmAdapter(OnCallBack mlistener,OnCallBack2 mlistener2,List<Alarm> mlistalarm) {
        this.mlistalarm = mlistalarm;
        this.mlistener = mlistener;
        this.mlistener2 = mlistener2;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View iteamview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm,parent,false);
        return new ViewHolder(iteamview);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Alarm alarm = mlistalarm.get(position);
        holder.tv_hour.setText(alarm.getTime());
        holder.tv_level.setText(alarm.getLevel());
        if(alarm.getCheckswitch()){
            holder.mswitch.setChecked(true);
        }else {
            holder.mswitch.setChecked(false);
        }
        holder.mswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((Switch) v).isChecked();
                if (checked){
                    mlistener2.onIteamClicked2(position);
//                    //
                    alarm.setCheckswitch(true);
                    Log.i("in adapter on"+String.valueOf(position),"zzzz");
                }
                else{
                    mlistener.onIteamClicked(position);
                    //holder.mswitch.setChecked(false);
                    alarm.setCheckswitch(false);
                    Log.i("in adapter off"+String.valueOf(position),"zzzz");
                }

            }
        });
//        holder.mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    mlistener2.onIteamClicked2(position);
//                    //alarm.setCheckswitch(true);
//                    Log.i("in adapter on"+String.valueOf(position),"zzzz");
//
//                } else {
//                    mlistener.onIteamClicked(position);
//                    //alarm.setCheckswitch(false);
//                    Log.i("in adapter off"+String.valueOf(position),"zzzz");
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mlistalarm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Switch mswitch ;
        TextView tv_hour,tv_level;

        public ViewHolder( View itemView) {
            super(itemView);
            mswitch = (Switch) itemView.findViewById(R.id.switch1);
            tv_hour = (TextView)  itemView.findViewById(R.id.tv_hour);
            tv_level = (TextView)  itemView.findViewById(R.id.tv_level);

        }
    }
    public interface OnCallBack{
        void onIteamClicked(int position);
    }
    public interface OnCallBack2{
        void onIteamClicked2(int position);
    }


}
