package com.shinkansen.touchcolor;

import com.shinkansen.touchcolor.soundmanager.SoundManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		SoundManager.getInstance().initSoundBackground(this);
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
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.btnRememberColor: {
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
		dialog.setTitle("Help");
		
		TextView text = (TextView) dialog.findViewById(R.id.help_text);
		text.setText("Android custom dialog example!");
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
		dialog.setContentView(R.layout.setting_dialog);
		dialog.setTitle("Setting");
		
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
				}else{
					SoundManager.getInstance().setTurnOffSound(false);
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
