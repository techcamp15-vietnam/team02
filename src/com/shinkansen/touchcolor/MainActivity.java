package com.shinkansen.touchcolor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_about:
			showDialogAbout();
        return true;
		case R.id.action_manual:
			showDialogManual();
        return true;
		}
		return false;
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
	/**
	 Menu show dialog
	@param
	@author congthang
	*/
	public void showDialogAbout(){
		AlertDialog.Builder b=new AlertDialog.Builder(MainActivity.this);
		 
		b.setTitle("About");
		b.setMessage("Sinkansen team");
		b.setPositiveButton("OK", new DialogInterface. OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			dialog.cancel();
		}});

		b.create().show();
	}
	public void showDialogManual(){
	AlertDialog.Builder b=new AlertDialog.Builder(MainActivity.this);
	 
	b.setTitle("Manual");
	b.setMessage("I don't know!");
	b.setPositiveButton("OK", new DialogInterface. OnClickListener() {
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		dialog.cancel();
	}});

	b.create().show();
}
	
	
	
}
