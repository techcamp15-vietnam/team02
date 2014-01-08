package com.shinkansen.touchcolor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.shinkansen.touchcolor.constant.Constant;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayActivity extends Activity {

	private Preview mPreview;
	private Camera mCamera;
	public int x, y;
	public static Point size,scale;
	private TextView txtColorName;
	private ImageView ivShowColor;
	private ImageView ivTransparent;
	private FrameLayout previewLayout;
	private String colorCatchedName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		ivTransparent = (ImageView) findViewById(R.id.iv_pTransparent);
		
		previewLayout = (FrameLayout) findViewById(R.id.play_preview);
		txtColorName = (TextView) findViewById(R.id.txt_pColorName);
		ivShowColor = (ImageView) findViewById(R.id.iv_pShowColor);
		
		//init camera
		mPreview = new Preview(this);
		mCamera = Camera.open();
		mPreview.setCamera(mCamera);
		previewLayout.addView(mPreview);
		
		ivTransparent.setOnTouchListener(ontch);
		int randIndex = randomColorIndex();
		String colorString = Constant.COLOR[randIndex];
		ivShowColor.setBackgroundColor(Constant.COLOR_ID[randIndex]);
		txtColorName.setText(colorString);
		txtColorName.setTextColor(Constant.COLOR_ID[randIndex]);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
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
	 get random Color
	@param 
	@author 2C-huuthang
	*/
	public int randomColorIndex() {
		int rand = (int)(Math.random()* Constant.COLOR.length);
		return rand;
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
		case R.id.btn_next:
		{
			int randIndex = randomColorIndex();
			String colorString = Constant.COLOR[randIndex];
			Log.d("color rand", colorString);
			ivShowColor.setBackgroundColor(Constant.COLOR_ID[randIndex]);
			txtColorName.setText(colorString);
			txtColorName.setTextColor(Constant.COLOR_ID[randIndex]);
			
			break;
		}

		default:
			break;
		}
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
						Bitmap bmp = Preview.bitmap;
						Log.d("sss",bmp.getWidth()+"/"+bmp.getHeight());
						float sx =(float) bmp.getWidth()/size.x;
						float sy =(float) bmp.getHeight()/size.y;
						x=(int)(x*sx);
						y=(int)(y*sy);
						int tch = bmp.getPixel(x, y);
						ivShowColor.setBackgroundColor(tch);
						colorCatchedName = getBestMatchingColorName(tch);
						txtColorName.setText(colorCatchedName);
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
	@author 2A-duythanh
	*/

	private String getBestMatchingColorName(int pixelColor) {
		Map<String, Integer> mColors = new HashMap<String, Integer>();
		mColors.put("blue", Color.rgb(0, 0, 255));
		mColors.put("green", Color.rgb(0, 255, 0));
		mColors.put("yellow", Color.rgb(255, 255, 0));
		mColors.put("red", Color.rgb(255, 0, 0));
		mColors.put("purple", Color.rgb(160, 32, 240));
		mColors.put("orange", Color.rgb(255, 165, 0));
		mColors.put("black", Color.rgb(0, 0, 0));
		mColors.put("white", Color.rgb(255, 255, 255));
		
		
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
	    return closestColorName;
//	    else return "Khong mau";
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

	/**
	 Menu show dialog
	@param
	@author 2B-congthang
	*/
	public void showDialogAbout(){
		AlertDialog.Builder b=new AlertDialog.Builder(this);
		 
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
		AlertDialog.Builder b=new AlertDialog.Builder(this);
		 
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
