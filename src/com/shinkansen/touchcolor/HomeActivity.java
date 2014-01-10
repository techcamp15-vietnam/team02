package com.shinkansen.touchcolor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.shinkansen.touchcolor.soundmanager.SoundManager;

public class HomeActivity extends Activity {
	private AnimationDrawable animation1;
	private ImageView imgAni1;
	private AnimationDrawable animation2;
	private ImageView imgAni2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		imgAni1 = (ImageView) findViewById(R.id.animation1);
		animation1 = (AnimationDrawable) imgAni1.getDrawable();
		 
        animation1.setCallback(imgAni1);

        animation1.setVisible(true, true);
        animation1.start();
        
        imgAni2 = (ImageView) findViewById(R.id.logoView);
		animation2 = (AnimationDrawable) imgAni2.getDrawable();
		 
        animation2.setCallback(imgAni2);

        animation2.setVisible(true, true);
        animation2.start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SoundManager.getInstance().playSound(1);
	}

	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event)  { 
	    if (keyCode == KeyEvent.KEYCODE_BACK) { 
	    	SoundManager.getInstance().setTurnOffSoundBackground(1);
	    	HomeActivity.this.finish();
	        return true; 
	    } 

	    return super.onKeyDown(keyCode, event); 
	}
	/**
	 * Event when button click
	 * 
	 * @param view
	 *            : view of button
	 * @author 2C-huuthang
	 */
	public void buttonClick(View view) {
		switch (view.getId()) {
		case R.id.btnStudyColor: {
			SoundManager.getInstance().setStopSound(1);
			SoundManager.getInstance().playSound(13);

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.btnRememberColor: {
			SoundManager.getInstance().setStopSound(1);
			SoundManager.getInstance().playSound(14);
			Intent intent = new Intent(this, PlayActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.btnHelp: {
			showHelpDialog();
			break;
		}
		case R.id.btnSetting: {
			showSettingDialog();
			break;
		}

		default:
			break;
		}
	}

	public void showHelpDialog() {
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.help_dialog);
		dialog.setTitle("マニュアル");
		
		Button dialogButton = (Button) dialog.findViewById(R.id.s_btnOk);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	public void showSettingDialog() {
		// custom dialog
		final Dialog dialog = new Dialog(this);
		boolean turnOffSound;
		dialog.setContentView(R.layout.setting_dialog);
		dialog.setTitle("設定");
		
		// get setting's used last time
		SharedPreferences sharedPref = getSharedPreferences("sound_data", MODE_PRIVATE);
		boolean isTurnOff = sharedPref.getBoolean("turnOffSound", false);
		CheckBox chkSound = (CheckBox) dialog.findViewById(R.id.chkSound);
		chkSound.setChecked(isTurnOff);
		chkSound.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (((CheckBox)v).isChecked()){
					// turn off sound
					SoundManager.getInstance().setTurnOffSound(true);
					SharedPreferences sharedPref = getSharedPreferences("sound_data", MODE_PRIVATE);
			    	SharedPreferences.Editor editor = sharedPref.edit();
			    	editor.putBoolean("turnOffSound", true);
			    	editor.commit();
			    	SoundManager.getInstance().setPauseSound(1);
				}else{
					SoundManager.getInstance().setTurnOffSound(false);
					SoundManager.getInstance().setResumSound(1);
					SharedPreferences sharedPref = getSharedPreferences("sound_data", MODE_PRIVATE);
			    	SharedPreferences.Editor editor = sharedPref.edit();
			    	editor.putBoolean("turnOffSound", false);
			    	editor.commit();
				}
			}
		});
		Button dialogButton = (Button) dialog.findViewById(R.id.s_btnOk);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

}
