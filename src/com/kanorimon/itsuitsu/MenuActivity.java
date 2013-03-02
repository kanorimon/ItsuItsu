package com.kanorimon.itsuitsu;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.widget.TimePicker;

//@SuppressWarnings("deprecation")
public class MenuActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	//設定
	private SharedPreferences p;
	
	private Preference prefMorningCheck;
	private Preference prefMorningTime;
	private Preference prefNightCheck;
	private Preference prefNightTime;
	private Preference prefNintamaCheck;
	private Preference prefNintamaTime;
	private Preference prefvibrationCheck;
	private Preference prefLedCheck;
	private ListPreference prefChar;
	private ListPreference prefWeather;
	private RingtonePreference prefRingtone;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.v(getString(R.string.log),"MenuActivity　setText start");

    	super.onCreate(savedInstanceState);
    	addPreferencesFromResource(R.xml.pref);

        p = PreferenceManager.getDefaultSharedPreferences( getApplicationContext());
        p.edit().commit();
        
    	//初回起動設定
    	firstListener();
    	
    	//初期表示の設定
    	presetListener();
    	
    	//おはようアラームクリック待ちリスナー
    	prefMorningCheck = this.findPreference("morning_checkbox");
    	prefMorningCheck.setOnPreferenceClickListener(onPreferenceClickListener);

    	prefMorningTime = this.findPreference("morning_time");
    	prefMorningTime.setOnPreferenceClickListener(onPreferenceClickListener);
    	
    	//おやすみアラームクリック待ちリスナー
    	prefNightCheck = this.findPreference("night_checkbox");
    	prefNightCheck.setOnPreferenceClickListener(onPreferenceClickListener);

    	prefNightTime = this.findPreference("night_time");
    	prefNightTime.setOnPreferenceClickListener(onPreferenceClickListener);

    	//にんたまアラームクリック待ちリスナー
    	prefNintamaCheck = this.findPreference("nintama_checkbox");
    	prefNintamaCheck.setOnPreferenceClickListener(onPreferenceClickListener);

    	prefNintamaTime = this.findPreference("nintama_time");
    	prefNintamaTime.setOnPreferenceClickListener(onPreferenceClickListener);

    	//キャラクター設定クリック待ちリスナー
        prefChar = (ListPreference)findPreference(getString(R.string.character_pref));   
        prefChar.setOnPreferenceChangeListener(listPreference_OnPreferenceChangeListener);  

    	//天気予報地点設定クリック待ちリスナー
        prefWeather = (ListPreference)findPreference(getString(R.string.weather_pref));   
        prefWeather.setOnPreferenceChangeListener(listPreference_OnPreferenceChangeListener);  
    	
    	//バイブレーション設定クリック待ちリスナー
        prefvibrationCheck = this.findPreference("vibration_checkbox");
        prefvibrationCheck.setOnPreferenceClickListener(onPreferenceClickListener);
    	
    	//LED設定クリック待ちリスナー
       	prefLedCheck = this.findPreference("led_checkbox");
       	prefLedCheck.setOnPreferenceClickListener(onPreferenceClickListener);

    	//通知音設定クリック待ちリスナー
        prefRingtone = (RingtonePreference)findPreference("ringtone_pref");   
        prefRingtone.setOnPreferenceChangeListener(ringtonePreference_OnPreferenceChangeListener);  

		Log.v(getString(R.string.log),"MenuActivity　setText end");
    }
    
    //初回起動設定
    private void firstListener(){

    	if(p.getInt("morning_h", 99) == 99){
    		p.edit().putInt("morning_h", 7).commit();
   		}
    	if(p.getInt("morning_m", 99) == 99){
    		p.edit().putInt("morning_m", 0).commit();
   		}
    	if(p.getInt("night_h", 99) == 99){
    		p.edit().putInt("night_h", 22).commit();
   		}
    	if(p.getInt("night_m", 99) == 99){
    		p.edit().putInt("night_m", 0).commit();
   		}
    	if(p.getInt("nintama_h", 99) == 99){
    		p.edit().putInt("nintama_h", 18).commit();
   		}
    	if(p.getInt("nintama_m", 99) == 99){
    		p.edit().putInt("nintama_m", 10).commit();
   		}
    }

    //画面表示時のプリセット
    public void presetListener() {

    	//おはようアラーム
    	onAlarmPreferenceChanged("morning");
 
    	//おやすみアラーム
    	onAlarmPreferenceChanged("night");

    	//にんたまアラーム
    	onAlarmPreferenceChanged("nintama");

        //キャラクター
        listPreference_OnPreferenceChange_char();
        
        //天気予報地点
        listPreference_OnPreferenceChange_wether();

        //バイブレーション
        settingCheckGet("vibration");
        
        //LED
        settingCheckGet("led");
        
        //通知音
        ringtonePreference_OnPreferenceChange();

    }
    
    /*
     * サマリー表示設定
     */

    //アラームのサマリー設定呼び出し
    public void onAlarmPreferenceChanged(String alarmTiming) {
    	//おはようアラーム通知設定読み込み
        alermCheckGet(alarmTiming);
    	//おはようアラーム通知時間読み込み
        alermTimeGet(alarmTiming);
    }

    //アラームのサマリー表示設定
    private void alermCheckGet(String alermTiming){
    	CheckBoxPreference checkbox_preference = (CheckBoxPreference)getPreferenceScreen().findPreference(alermTiming + "_checkbox");
    	if (checkbox_preference.isChecked()) {
    		checkbox_preference.setSummary("お知らせする");
    	} else {
    		checkbox_preference.setSummary("お知らせしない");
    	}
    }

    //バイブレーション・LEDのサマリー表示設定
    private void settingCheckGet(String key){
    	CheckBoxPreference checkbox_preference = (CheckBoxPreference)getPreferenceScreen().findPreference(key + "_checkbox");
    	if (checkbox_preference.isChecked()) {
    		checkbox_preference.setSummary("有効");
    	} else {
    		checkbox_preference.setSummary("無効");
    	}
    }

    //通知時間サマリー表示設定
    private void alermTimeGet(String alermTiming){
    	String text = String.format("%1$02d時%2$02d分", p.getInt(alermTiming + "_h", 0),p.getInt(alermTiming + "_m", 0));
    	Preference time_preference = (Preference)getPreferenceScreen().findPreference(alermTiming + "_time");
    	time_preference.setSummary(text);
    }
            
    //通知音のサマリー表示設定（プリセット）
    private void ringtonePreference_OnPreferenceChange(){     
    	Preference ring_preference = (Preference)getPreferenceScreen().findPreference("ringtone_pref");
    	String url = p.getString("ringtone_pref",""); 
    	
    	ringtonePreference_OnPreferenceChange(ring_preference,url);
    	/*
    	Uri uri;  
        Ringtone ringtone;  
        if ("".equals(url)) {
        	ring_preference.setSummary("サイレント");
        } else {
            uri = Uri.parse(url);  
            ringtone = RingtoneManager.getRingtone(this, uri);  
            ring_preference.setSummary(ringtone.getTitle(this));  
        }
        */
    }  

    //通知音のサマリー表示設定
    private boolean ringtonePreference_OnPreferenceChange(Preference preference, Object newValue){     
        String url = (String)newValue; 
        Uri uri;  
        Ringtone ringtone;  
        
        if ("".equals(url)) {
            preference.setSummary("サイレント");
        } else {
            uri = Uri.parse(url);  
            ringtone = RingtoneManager.getRingtone(this, uri);  
            preference.setSummary(ringtone.getTitle(this));  
        }  
        return true;  
    }
    
    //キャラクターのサマリー表示設定（プリセット）
    private void listPreference_OnPreferenceChange_char(){     
    	ListPreference list_preference = (ListPreference)getPreferenceScreen().findPreference(getString(R.string.character_pref));
    	list_preference.setSummary(list_preference.getEntry());
    }

    //天気予報地点のサマリー表示設定（プリセット）
    private void listPreference_OnPreferenceChange_wether(){     
    	ListPreference list_preference = (ListPreference)getPreferenceScreen().findPreference(getString(R.string.weather_pref));
    	list_preference.setSummary(list_preference.getEntry());
    }
            
    //キャラクター・天気予報地点のサマリー表示設定
    private boolean listPreference_OnPreferenceChange(Preference preference, Object newValue){
    	String nin = (String)newValue; 
        String ninX = "";
        String[] charName = getResources().getStringArray(R.array.character_list_name);
        String[] charValue = getResources().getStringArray(R.array.character_list_value);

        String[] weatherArray = getResources().getStringArray(R.array.weather_list_name_asc); 
        
        try{
        	ninX = weatherArray[Integer.parseInt(nin)];
        }catch(NumberFormatException e){
        	for (int j=0 ; j<charValue.length ; j++) {
        		if (charValue[j].equals(nin)){
        			ninX = charName[j];
        			break;
        		}
        	}
        }

        preference.setSummary(ninX);  
        return true;
    }

    /*
     * リスナー
     */

	//true falseボタン、時刻設定のクリックリスナー
    private OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {

        	//クリックされたキーを取得
        	String prekey = preference.getKey();

    	    if(prekey.equals("morning_checkbox")){
    	    	checkOnOff("morning");
    	    }
    	    
    	    if(prekey.equals("morning_time")){
    	    	timeGet("morning");
    	    } 	    

    	    if(prekey.equals("night_checkbox")){
    	    	checkOnOff("night");
    	    }
    	    
    	    if(prekey.equals("night_time")){
    	    	timeGet("night");
    	    } 	    

    	    if(prekey.equals("nintama_checkbox")){
    	    	checkOnOff("nintama");
    	    }
    	    
    	    if(prekey.equals("nintama_time")){
    	    	timeGet("nintama");
    	    } 	    

    	    if(prekey.equals("vibration_checkbox")){
    	    	settingCheckGet("vibration");
    	    }
    	    
    	    if(prekey.equals("led_checkbox")){
    	    	settingCheckGet("led");
    	    }

            return true;
        }
    };
    
    // 通知音設定変更リスナー  
    private OnPreferenceChangeListener ringtonePreference_OnPreferenceChangeListener = new OnPreferenceChangeListener(){  
        @Override  
        public boolean onPreferenceChange(Preference preference, Object newValue) {  
            return ringtonePreference_OnPreferenceChange(preference,newValue);  
        }
    };  
    
    //キャラクター・天気予報地点の設定変更リスナー  
    private OnPreferenceChangeListener listPreference_OnPreferenceChangeListener = new OnPreferenceChangeListener(){  
        @Override  
        public boolean onPreferenceChange(Preference preference, Object newValue) {  
            return listPreference_OnPreferenceChange(preference,newValue);  
        }
    };  

    /*
     * アラーム関連サブルーチン
     */

    //アラームのオンオフ設定
    private void checkOnOff(String alarmTiming){
	    onAlarmPreferenceChanged(alarmTiming);
	    alermStart(alarmTiming);
    }

    //アラームの時刻設定
    private void timeGet(final String alarmTiming){
    	final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    p.edit().putInt(alarmTiming + "_h", hourOfDay).commit();
                    p.edit().putInt(alarmTiming + "_m", minute).commit();
                    onAlarmPreferenceChanged(alarmTiming);
                    alermStart(alarmTiming);
                }
            }, p.getInt(alarmTiming + "_h", 0), p.getInt(alarmTiming + "_m", 0), true);
        timePickerDialog.show();
    }
    
    //アラームスタート・ストップ
    private void alermStart(String alermTiming){
    	CheckBoxPreference checkbox_preference = (CheckBoxPreference)getPreferenceScreen().findPreference(alermTiming + "_checkbox");
        MyAlarmManager mam = new MyAlarmManager( getApplicationContext());

    	if (checkbox_preference.isChecked()) {
            mam.addAlarm(p.getInt(alermTiming + "_h", 0),p.getInt(alermTiming + "_m", 0),alermTiming); 
    	} else {
            mam.stopAlarm(alermTiming); 
    	}
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}
	
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	prefMorningCheck.setOnPreferenceClickListener(null);
    	prefMorningTime.setOnPreferenceClickListener(null);
    	prefNightCheck.setOnPreferenceClickListener(null);
    	prefNightTime.setOnPreferenceClickListener(null);
    	prefNintamaCheck.setOnPreferenceClickListener(null);
    	prefNintamaTime.setOnPreferenceClickListener(null);
    	prefvibrationCheck.setOnPreferenceClickListener(null);
    	prefLedCheck.setOnPreferenceClickListener(null);
    	prefChar.setOnPreferenceChangeListener(null);
    	prefWeather.setOnPreferenceChangeListener(null);
    	prefRingtone.setOnPreferenceChangeListener(null);
	    System.gc();
    }

}
