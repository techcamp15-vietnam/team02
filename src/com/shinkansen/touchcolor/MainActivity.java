package com.shinkansen.touchcolor;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinkansen.touchcolor.adapter.ImageAdapter;
import com.shinkansen.touchcolor.adapter.ImageViewPagerAdapter;
import com.shinkansen.touchcolor.datahelper.RelateObjectDataSource;
import com.shinkansen.touchcolor.soundmanager.SoundManager;

public class MainActivity extends Activity {
	
	private Preview mPreview;
	private Camera mCamera;
	public int x, y;
	public static Point size,scale;
	private TextView txtColorName;
	private ImageView ivShowColor;
	private ImageView ivTransparent;
	private FrameLayout previewLayout;
	private SoundManager mSoundManager;
	
	private RelateObjectDataSource relateObject;
	Integer[] pics = {
    		R.drawable.active1,
    		R.drawable.active2,
    		R.drawable.ic_launcher
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ivTransparent = (ImageView) findViewById(R.id.ivTransparent);
		
		previewLayout = (FrameLayout) findViewById(R.id.preview);
		txtColorName = (TextView) findViewById(R.id.txtColorName);
		ivShowColor = (ImageView) findViewById(R.id.ivShowColor);
		
		//init camera
		mPreview = new Preview(this);
		mCamera = Camera.open();
		mPreview.setCamera(mCamera);
		previewLayout.addView(mPreview);
		
		ivTransparent.setOnTouchListener(ontch);
		
		
		String colorString = dummydata();
		
		ivShowColor.setBackgroundColor(Color.parseColor(colorString));
		txtColorName.setText(colorString);
		txtColorName.setTextColor(Color.parseColor(colorString));
		
		relateObject = new RelateObjectDataSource(this);
				
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
	
	/**
	 On touch screen, get color of the position
	@param:
	@author duythanh
	*/
	
	OnTouchListener ontch = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			// TODO Auto-generated method stub
			x = (int)event.getX();
			y = (int)event.getY();
			
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				
				Display display = getWindowManager().getDefaultDisplay();
				size = new Point();
				display.getSize(size);
				mPreview.takePicture();
				final Handler handler = new Handler();
		        handler.postDelayed(new Runnable() {
		            @Override
		            public void run() {		         
		            	//Bitmap bmp = rotateImage(Preview.bitmap,90);
						Bitmap bmp = Preview.bitmap;
						float sx =(float) bmp.getWidth()/size.x;
						float sy =(float) bmp.getHeight()/size.y;
						x=(int)(x*sx);
						y=(int)(y*sy);
						int tch = bmp.getPixel(x, y);
						String strColor = String.format("#%06X", 0xFFFFFF & tch);
//						txtColorName.setText(""+Integer.toHexString(str));
						//tch = Color.parseColor("#000000");
						tch +=1000;
						txtColorName.setText(""+tch);
						txtColorName.setTextColor(tch);
						
		            }
		        }, 500);
				break;
			
			}

			return true;
		}
	};
	/**
	Get name Color of pixel
	@param color: color of pixel(int)
	@author duythanh
	*/
	public String getNameColor(int color){
		if(Color.parseColor("#3e4095")<=color&&Color.parseColor("#3e4095")+1000>=color);
		
		return "";
	}
	
	
	
	/**
	 Rotate Image bitmap
	@param src: path of image bitmap; degree: rotate degree
	@author duythanh
	*/
	
	public Bitmap rotateImage(Bitmap src, float degree) {
	     // create new matrix object
	     Matrix matrix = new Matrix();
	     // setup rotation degree
	     matrix.postRotate(degree);
	     // return new bitmap rotated using matrix
	     return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	protected void onPause() {
		super.onPause();
		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		if (mCamera != null) {
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Open the default i.e. the first rear facing camera.
		

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
	
	public String dummydata(){
		String[] color = {"Red", "Blue", "Green", "White", "Black"};
		int rand = (int)( Math.random() * 5);
		return color[rand];
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
			Intent intent = new Intent(this, PlayActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
    	
    	AlertDialog alertDialog = dialog.create();
    	alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	alertDialog.show();

    	
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
			public void onClick(DialogInterface dialog, int which){
				dialog.cancel();
			}
		});
	
		b.create().show();
}
	
	
	
}
