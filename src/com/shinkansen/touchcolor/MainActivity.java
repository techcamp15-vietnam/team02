package com.shinkansen.touchcolor;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 Event when button click
	@param view : view of button 
	@author huuthang
	*/
	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnMenu:
			
			break;
		case R.id.btnNextColor:
			
			break;
		case R.id.btnPlay:
			
			break;

		default:
			break;
		}
	}

}
