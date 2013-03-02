package com.kanorimon.itsuitsu;

import java.util.Random;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

	//�ݒ�
	private View clickSetting;
    private ImageView imgv;

	@Override
    public void onCreate(Bundle savedInstanceState) {

		Log.v(getString(R.string.log),"MainActivity�@onCreate start");

    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	
    	//�ݒ�{�^���C�x���g
        clickSetting = findViewById(R.id.button_setting);
        clickSetting.setOnClickListener(oCLforShowButton);

		Log.v(getString(R.string.log),"MainActivity�@onCreate end");
	}
    
    @Override
    public void onStart(){
    	super.onStart();
    	setText();
   }
    
    @Override
    public void onRestart(){
    	super.onRestart();
    	setText();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
	    imgv.setBackgroundDrawable(null);
	    clickSetting.setOnClickListener(null);
	    System.gc();
    }
    
    //��ʕ\���̐ݒ�
    private void setText(){
		Log.v(getString(R.string.log),"MainActivity�@setText start");

		//�C���e���g�f�[�^�擾
		String timing = getIntent().getStringExtra(getString(R.string.timing));
		String character = getIntent().getStringExtra(getString(R.string.character));
		String wether = getIntent().getStringExtra(getString(R.string.weather));
		String minKion = getIntent().getStringExtra(getString(R.string.minKion));
		String maxKion = getIntent().getStringExtra(getString(R.string.maxKion));
		String cal = getIntent().getStringExtra(getString(R.string.calendar));
		String days24 = getIntent().getStringExtra(getString(R.string.days24));
		String days72 = getIntent().getStringExtra(getString(R.string.days72));
		String daysother = getIntent().getStringExtra(getString(R.string.daysother));
		
		//��擾
		String[] days24_name = getResources().getStringArray(getResources().getIdentifier("days24_list_name", "array", getPackageName()));
		String[] days72_name = getResources().getStringArray(getResources().getIdentifier("days72_list_name", "array", getPackageName()));
		String[] daysother_name = getResources().getStringArray(getResources().getIdentifier("daysother_list_name", "array", getPackageName()));

    	//��ʕ��i�擾
    	imgv = (ImageView)findViewById(R.id.imageView1);
    	TextView edit = (TextView)findViewById( R.id.textView1 );
    	
    	//�ݒ�f�[�^�擾
    	SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //�����_������
        Random r = new Random(System.currentTimeMillis());
        
    	//�L�����N�^�[�ݒ�
		if(character == null){
			character = p.getString(getString(R.string.character_pref), getString(R.string.character_default_value));
			if(character.equals(getString(R.string.character_random))){
				String[] random_char = getResources().getStringArray(getResources().getIdentifier(getString(R.string.character_list_value), "array", getPackageName()));
				character = random_char[r.nextInt(random_char.length-1)];
			}
		}

		//�^�C�g���擾
		String[] weather_title = getResources().getStringArray(getResources().getIdentifier("weather_title", "array", getPackageName()));
		String[] koyomi_title = getResources().getStringArray(getResources().getIdentifier("koyomi_title", "array", getPackageName()));
		String[] cal_title = getResources().getStringArray(getResources().getIdentifier("cal_title", "array", getPackageName()));

		//�g�[�N�擾
		String[] random_talk = getResources().getStringArray(getResources().getIdentifier(character + "_random", "array", getPackageName()));
		String[] morning_talk = getResources().getStringArray(getResources().getIdentifier(character + "_morning", "array", getPackageName()));
		String[] night_talk = getResources().getStringArray(getResources().getIdentifier(character + "_night", "array", getPackageName()));
		String[] rain_talk = getResources().getStringArray(getResources().getIdentifier(character + "_rain", "array", getPackageName()));
		//String[] cal_talk = getResources().getStringArray(getResources().getIdentifier(character + "_cal", "array", getPackageName()));
		String[] nintama_talk = getResources().getStringArray(getResources().getIdentifier(character + "_nintama", "array", getPackageName()));

		//�L�����N�^�[�摜�ݒ�
	    imgv.setBackgroundDrawable(getResources().getDrawable(getResources().getIdentifier(character, "drawable", getPackageName())));	

    	//�g�[�N������
	    String text = "";

	    //�����_���g�[�N
		if(timing == null){
			text = random_talk[r.nextInt(random_talk.length)];
        
   		//���͂悤�A���₷��
		}else if(timing.equals(getString(R.string.alarm_morning)) || timing.equals(getString(R.string.alarm_night))){
			
			//����or����
			String dateStr = "";
			
	   		//���͂悤
			if(timing.equals(getString(R.string.alarm_morning))){
				text = morning_talk[r.nextInt(morning_talk.length)];
				dateStr = getString(R.string.today_name);

			//���₷��
			}else{
				text = night_talk[r.nextInt(night_talk.length)];
				dateStr = getString(R.string.tomorrow_name);
			}
    		
			//�V�C
			
			if(!(wether.equals(""))){
    			text = text + String.format(weather_title[r.nextInt(weather_title.length)],dateStr);
    			text = text + "\n" + wether;

    			if(maxKion.equals("")){
    				if(minKion.equals("")){
    					//
    				}else{
            			text = text + "�i�Œ�" + minKion + "���j";
    				}
    			}else{
    				if(minKion.equals("")){
            			text = text + "�i�ō�" + maxKion + "���j";
    				}else{
            			text = text + "�i�ō�" + maxKion + "���@�Œ�" + minKion + "���j";
    				}
    			}

    			if((wether.indexOf("�J") != -1) || (wether.indexOf("��") != -1)){
        			text = text + rain_talk[r.nextInt(rain_talk.length)];
        		}
        	}

			//�X�P�W���[��
			if(!(cal.equals(""))){
				text = text + String.format(cal_title[r.nextInt(cal_title.length)],dateStr);
				text = text + cal;
				//text = text + String.format(cal_talk[r.nextInt(cal_talk.length)]);
			}

			//��
			if(!(days24.equals(""))){
				text = text + String.format(koyomi_title[r.nextInt(koyomi_title.length)],dateStr);
				text = text + "\n" + days24_name[Integer.parseInt(days24)];
			}
			if(!(days72.equals(""))){
				text = text + "-" + days72_name[Integer.parseInt(days72)];
			}
			if((!(daysother.equals(""))) && (!(daysother.equals("0")))){
				text = text + "-" + daysother_name[Integer.parseInt(daysother)];
			}

			//�ɂ񂽂�
        }else if(timing.equals(getString(R.string.alarm_nintama))){
			text = nintama_talk[r.nextInt(nintama_talk.length)];
        } 

    	edit.setText(text);

    	Log.v(getString(R.string.log),"MainActivity�@setText end");
    }
    
    //�{�^���N���b�N���̃��X�i
    private final OnClickListener oCLforShowButton = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){

            case R.id.button_setting:
            	Intent intent = new Intent( getApplicationContext(), MenuActivity.class );
                startActivity( intent );
                break;
            }
        }
    };
    

    

}
