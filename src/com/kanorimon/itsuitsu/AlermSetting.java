package com.kanorimon.itsuitsu;

import java.util.Random;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlermSetting {

	public void setNortification(Context context,Intent i,String timing,String character,SharedPreferences p){

		Log.v(context.getString(R.string.log),"setNortification start");
		
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

		Random r = new Random(System.currentTimeMillis());

        // LargeIcon �� Bitmap �𐶐�
        //Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_samon);
		//Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),context.getResources().getIdentifier("small-" + character, "drawable", context.getPackageName()));
		Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),context.getResources().getIdentifier("ic_launcher_" + character, "drawable", context.getPackageName()));

		 // NotificationBuilder���쐬
	    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
	    builder.setContentIntent(pi);

    	//�e�L�X�g�擾
	    String[] ticker_talk =  context.getResources().getStringArray(context.getResources().getIdentifier(character + "_ticker_" + timing, "array", context.getPackageName()));
	    String[] title_talk =  context.getResources().getStringArray(context.getResources().getIdentifier(character + "_title_" + timing, "array", context.getPackageName()));
	    String[] text_talk =  context.getResources().getStringArray(context.getResources().getIdentifier(character + "_text_" + timing, "array", context.getPackageName()));

    	// �X�e�[�^�X�o�[�ɕ\�������e�L�X�g
	    builder.setTicker(ticker_talk[r.nextInt(ticker_talk.length)]);

	    // �A�C�R��
	    builder.setSmallIcon(context.getResources().getIdentifier("small_" + character, "drawable", context.getPackageName()));
	    
	    // Notification���J�����Ƃ��ɕ\�������^�C�g��
	    builder.setContentTitle(title_talk[r.nextInt(title_talk.length)]);
	    
	    // Notification���J�����Ƃ��ɕ\�������T�u�^�C�g��
	    builder.setContentText(text_talk[r.nextInt(text_talk.length)]);

	    // Notification���J�����Ƃ��ɕ\�������A�C�R��
	    builder.setLargeIcon(largeIcon);
	    
	    // �ʒm����^�C�~���O
	    builder.setWhen(System.currentTimeMillis());
	    
        //�ʒm��
        String url = p.getString("ringtone_pref","");
        if(url == null){
        }else{
        	builder.setSound(Uri.parse(url));
        }
        
        //�o�C�u���[�V����
        if(p.getBoolean("vibration_checkbox",false)){
        	long[] vibrate = {0,100,500,100,500,100};
        	builder.setVibrate(vibrate);
        }
        
        //LED
        if(p.getBoolean("led_checkbox",false)){
        	builder.setLights(0xff00ff00,300,3000);
        }
        
	    // �^�b�v����ƃL�����Z��(������)
	    builder.setAutoCancel(true);
	    
        // NotificationManager�̃C���X�^���X�擾
        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, builder.build()); // �ݒ肵��Notification��ʒm����

        Log.v(context.getString(R.string.log),"setNortification end");
	}
	


}
