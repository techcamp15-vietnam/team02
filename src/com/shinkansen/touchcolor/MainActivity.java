package com.shinkansen.touchcolor;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import com.shinkansen.touchcolor.adapter.ImageAdapter;
import com.shinkansen.touchcolor.adapter.ImageViewPagerAdapter;
import com.shinkansen.touchcolor.datahelper.RelateObjectDataSource;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private RelateObjectDataSource relateObject;
	Integer[] pics = {
    		R.drawable.active1,
    		R.drawable.active2,
    		R.drawable.frame_white
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		relateObject = new RelateObjectDataSource(this);
		
		/**
		 * test add data
		 * 
		RelateObject testObj = new RelateObject();
		testObj.setRObjId(2);
		testObj.setObjectColor("Blue");
		testObj.setObjectName("Ball");
		testObj.setObjectImageName("ball");
		
		relateObject.addRelateObject(testObj);*/
		
		
		Gallery gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this, pics));
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showPopupImage(pics[arg2], arg2);
			}
        	
        });
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
	 Show popup Image
	@param imageResource: id of image; position: index of image
	@author huuthang
	*/
	private void showPopupImage(Integer imageResource, int position){
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
    	View layout = inflater.inflate(R.layout.custom_popup_image, (ViewGroup) findViewById(R.id.layout_root));
   
    	
    	ViewPager viewPager = (ViewPager) layout.findViewById(R.id.view_pager);
    	viewPager.setAdapter(new ImageViewPagerAdapter(this, pics));
    	viewPager.setCurrentItem(position);
    	dialog.setView(layout);
    	dialog.setCancelable(true);
    	
    	dialog.create();
    	dialog.show();
    	
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
