package com.kanorimon.itsuitsu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootAlarmReceiver extends BroadcastReceiver {

	private SharedPreferences p;
	private Context c;

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.v(context.getString(R.string.log),"BootAlarmReceiver　onReceive start");

		c = context;
		p =  PreferenceManager.getDefaultSharedPreferences(c);

		alermStart(context.getString(R.string.alarm_morning));
    	alermStart(context.getString(R.string.alarm_night));
    	alermStart(context.getString(R.string.alarm_nintama));

		Log.v(context.getString(R.string.log),"BootAlarmReceiver　onReceive end");
	}

    //アラームスタート・ストップ
    private void alermStart(String alermTiming){
    	MyAlarmManager mam = new MyAlarmManager(c);

    	if (p.getBoolean(alermTiming+"_checkbox",false)) {
            mam.addAlarm(p.getInt(alermTiming + "_h", 0),p.getInt(alermTiming + "_m", 0),alermTiming); 
    	} else {
            mam.stopAlarm(alermTiming); 
    	}
    }
}
