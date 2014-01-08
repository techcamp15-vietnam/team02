package com.shinkansen.touchcolor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import android.util.Log;
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

import com.shinkansen.touchcolor.DataModel.RelateObject;
import com.shinkansen.touchcolor.adapter.ImageAdapter;
import com.shinkansen.touchcolor.adapter.ImageViewPagerAdapter;
import com.shinkansen.touchcolor.datahelper.RelateObjectDataSource;

public class MainActivity extends Activity {
	
	private Preview mPreview;
	private Camera mCamera;
	public int x, y;
	public static Point size,scale;
	private TextView txtColorName;
	private ImageView ivShowColor;
	private ImageView ivTransparent;
	private FrameLayout previewLayout;

	private String colorCatchedName;
	private Gallery gallery;

	
	private RelateObjectDataSource relateObject;
		ArrayList<Integer> pics = new ArrayList<Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ivTransparent = (ImageView) findViewById(R.id.ivTransparent);
		
		previewLayout = (FrameLayout) findViewById(R.id.preview);
		txtColorName = (TextView) findViewById(R.id.txtColorName);
		ivShowColor = (ImageView) findViewById(R.id.ivShowColor);
		gallery = (Gallery) findViewById(R.id.gallery);
		
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
		
		RelateObject testObj = new RelateObject();
		for (int i = 1; i < 5; i++){
			testObj.setObjectImageName("do"+ i);
			testObj.setObjectColor("red");
			
			relateObject.addRelateObject(testObj);
		}/*
		for (int i = 1; i < 3; i++){
			testObj.setObjectImageName("trang"+ i);
			testObj.setObjectColor("white");
			
			relateObject.addRelateObject(testObj);
		}
		for (int i = 1; i < 5; i++){
			testObj.setObjectImageName("vang"+ i);
			testObj.setObjectColor("yellow");
			
			relateObject.addRelateObject(testObj);
		}
		for (int i = 1; i < 5; i++){
			testObj.setObjectImageName("xanh"+ i);
			testObj.setObjectColor("green");
			
			relateObject.addRelateObject(testObj);
		}
		for (int i = 1; i < 5; i++){
			testObj.setObjectImageName("tim"+ i);
			testObj.setObjectColor("purple");
			
			relateObject.addRelateObject(testObj);
		}
		for (int i = 1; i < 5; i++){
			testObj.setObjectImageName("den"+ i);
			testObj.setObjectColor("black");
			
			relateObject.addRelateObject(testObj);
		}
		for (int i = 1; i < 3; i++){
			testObj.setObjectImageName("cam"+ i);
			testObj.setObjectColor("orange");
			
			relateObject.addRelateObject(testObj);
		}*/
		
		
	}
	
	/**
	 On touch screen, get color of the position
	@param:
	@author 2A-duythanh
	*/
	
	OnTouchListener ontch = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			
	
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
						ivShowColor.setBackgroundColor(tch);
						Log.d("color before parse", String.valueOf(tch));
						//tch = caculateColorNearest(tch);
						//Log.d("color after parse", String.valueOf(tch));
						colorCatchedName = getBestMatchingColorName(tch);
						txtColorName.setText(colorCatchedName);

						txtColorName.setTextColor(tch);
						
						// show ralate object
						
						List<RelateObject> listObject = relateObject.getObjectsByColor(colorCatchedName);
						
						Log.d("num of object", String.valueOf(listObject.size()));
						
						if (!pics.isEmpty()) pics.clear();
						
						for (RelateObject relateObject : listObject) {
							String imgName = relateObject.getRObjectImageName();
							int indexImg = getResources().getIdentifier(imgName, "drawable", getPackageName());
							//Log.d("Index", String.valueOf(indexImg));	
							pics.add(indexImg);
						}
								
						gallery.setAdapter(new ImageAdapter(getApplicationContext(), pics));
						gallery.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								showPopupImage(pics.get(arg2), arg2);
							}
				        });
		            }
		        }, 2000);
				break;
			
			}
			
			return true;
		}
	};
	/**
	Get name Color of pixel
	@param color: color of pixel(int)
<<<<<<< HEAD
	@author 2A-duythanh
	*/

	private String getBestMatchingColorName(int pixelColor) {
		Map<String, Integer> mColors = new HashMap<String, Integer>();
		mColors.put("blue", Color.rgb(0, 0, 255));
		mColors.put("blue", Color.rgb(0, 0, 238));
		mColors.put("blue", Color.rgb(0, 0, 205));
		mColors.put("blue", Color.rgb(0, 0, 139));
//		mColors.put("cyan", Color.rgb(0, 255, 255));
		mColors.put("green", Color.rgb(0, 255, 0));
		mColors.put("green", Color.rgb(0, 238, 0));
		mColors.put("green", Color.rgb(0, 205, 0));
		
		mColors.put("yellow", Color.rgb(255, 255, 0));
		mColors.put("yellow", Color.rgb(238, 238, 0));
		mColors.put("yellow", Color.rgb(238, 220, 130));
		mColors.put("yellow", Color.rgb(238, 201, 0));
		mColors.put("yellow", Color.rgb(255, 236, 139));
		mColors.put("yellow", Color.rgb(205, 205, 0));
		
		mColors.put("red", Color.rgb(206, 14, 12));
		mColors.put("red", Color.rgb(238, 22, 2));
		mColors.put("red", Color.rgb(255, 4, 4));
		mColors.put("red", Color.rgb(139, 11, 11));
		
		mColors.put("orange", Color.rgb(255, 153, 18));
		mColors.put("orange", Color.rgb(237, 145, 33));
		mColors.put("orange", Color.rgb(255, 140, 0));
		mColors.put("orange", Color.rgb(255, 127, 0));
		mColors.put("orange", Color.rgb(255, 102, 0));
		mColors.put("orange", Color.rgb(255, 128, 0));
		
		
		
		mColors.put("purple", Color.rgb(148, 0, 211));
		mColors.put("purple", Color.rgb(153, 50, 204));
		mColors.put("purple", Color.rgb(138, 43, 226));
		mColors.put("purple", Color.rgb(155, 48, 255));
		mColors.put("purple", Color.rgb(145, 44, 238));
		
		mColors.put("white", Color.rgb(255, 255, 255));
//		mColors.put("pink", Color.rgb(255, 192, 203));
		mColors.put("pink", Color.rgb(238, 121, 159));
		mColors.put("pink", Color.rgb(205, 104, 137));
		mColors.put("pink", Color.rgb(238, 162, 173));
		mColors.put("pink", Color.rgb(238, 18, 137));
		mColors.put("pink", Color.rgb(238, 48, 167));
		mColors.put("pink", Color.rgb(255, 20, 147));
		

		mColors.put("black", Color.rgb(0, 0, 0));
		
		
	    // largest difference is 255 for every colour component
	    int currentDifference = 3 * 255;
	    // name of the best matching colour
	    String closestColorName = null;
	    // get int values for all three colour components of the pixel
	    int pixelColorR = Color.red(pixelColor);
	    int pixelColorG = Color.green(pixelColor);
	    int pixelColorB = Color.blue(pixelColor);

	    Iterator<String> colorNameIterator = mColors.keySet().iterator();
	    // continue iterating if the map contains a next colour and the difference is greater than zero.
	    // a difference of zero means we've found an exact match, so there's no point in iterating further.
	    while (colorNameIterator.hasNext() && currentDifference > 0) {
	        // this colour's name
	        String currentColorName = colorNameIterator.next();
	        // this colour's int value
	        int color = mColors.get(currentColorName);
	        // get int values for all three colour components of this colour
	        int colorR = Color.red(color);
	        int colorG = Color.green(color);
	        int colorB = Color.blue(color); 
	        // calculate sum of absolute differences that indicates how good this match is 
	        int difference = Math.abs(pixelColorR - colorR) + Math.abs(pixelColorG - colorG) + Math.abs(pixelColorB - colorB);
	        // a smaller difference means a better match, so keep track of it
	        if (currentDifference > difference) {
	            currentDifference = difference;
	            closestColorName = currentColorName;
	        }
	    }
//	    if(currentDifference<50)
	    return closestColorName+currentDifference;
//	    else return "Khong mau";
	}
	/**
	@author duythanh
	*/
	public String getNameColor(int color){
		if(Color.parseColor("#3e4095")<=color&&Color.parseColor("#3e4095")+1000>=color);
		
		return "";
	}
	
	
	

	/**
	 Rotate Image bitmap
	@param src: path of image bitmap; degree: rotate degree
	@author 2A-duythanh
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
	@author 2C-huuthang
	*/
	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnMenu:
			
			break;
		case R.id.btnNextColor:
			
			break;
		case R.id.btnPlay:{
			if (mCamera != null) {
				mPreview.setCamera(null);
				mCamera.release();
				mCamera = null;
			}
			Intent intent = new Intent(this, PlayActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			break;
		}

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
	@author 2B-congthang
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
