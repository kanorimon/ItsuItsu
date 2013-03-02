package com.kanorimon.itsuitsu;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class MyAlarmManager {

    Context c;
    AlarmManager am;
    private PendingIntent mAlarmSender;
    
    public MyAlarmManager(Context c){
    	this.c = c;
    	am = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);

    	Log.v(c.getString(R.string.log),"MyAlarmManager 初期化完了");
    }
    
    public void addAlarm(int hour,int min,String alermTiming){
 
    	Log.v(c.getString(R.string.log),"MyAlarmManager addAlarm　start");

    	Intent i = new Intent();
    	i.setClass(c, MyAlarmService.class);  
    	i.setData(Uri.parse("http://" + alermTiming));
    	i.putExtra(c.getString(R.string.timing),alermTiming);
    	
    	//mAlarmSender = PendingIntent.getBroadcast(c, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
    	mAlarmSender = PendingIntent.getService(c, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

    	// アラーム時間設定
    	Calendar calnow = Calendar.getInstance();
    	calnow.setTimeInMillis(System.currentTimeMillis());
 
    	Calendar calset = Calendar.getInstance();
    	calset.setTimeInMillis(System.currentTimeMillis());
    	calset.set(Calendar.HOUR_OF_DAY, hour);
    	calset.set(Calendar.MINUTE, min);
    	calset.set(Calendar.SECOND, 0);
    	calset.set(Calendar.MILLISECOND, 0);

    	//今日が過ぎていたら明日
    	if(calset.compareTo(calnow) <= 0){
    		calset.add(Calendar.DATE, 1);
    	}

    	//にんたまアラームは平日のみ
    	if(alermTiming.equals(c.getString(R.string.alarm_nintama))){
    		if(calset.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
        		calset.add(Calendar.DATE, 2);
    		}
    		if(calset.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
        		calset.add(Calendar.DATE, 1);
    		}
    	}

    	Log.v(c.getString(R.string.log),"MyAlarmManager アラームセット" + calset.getTime() + " : " + calnow.getTime());

    	am.set(AlarmManager.RTC_WAKEUP, calset.getTimeInMillis(), mAlarmSender);

    	Log.v(c.getString(R.string.log),"MyAlarmManager アラームセット完了");
    }
    
    public void stopAlarm(String alermTiming) {
    	
    	Intent i = new Intent();
    	i.setClass(c, MyAlarmService.class);  
    	i.setData(Uri.parse("http://" + alermTiming));
    	i.putExtra(c.getString(R.string.timing),alermTiming);

      	//mAlarmSender = PendingIntent.getBroadcast(c, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
      	mAlarmSender = PendingIntent.getService(c, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

      	am.cancel(mAlarmSender);
    	Log.v(c.getString(R.string.log),"MyAlarmManager アラームキャンセル");
    }
    
}

