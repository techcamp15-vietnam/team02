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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

public class MainActivity extends Activity {
	
	private Preview mPreview;
	private Camera mCamera;
	public int x, y;
	public static Point size,scale;
	private TextView txtColorName;
	private ImageView ivShowColor;
	private ImageView ivTransparent;
	private FrameLayout previewLayout;
	
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
	

	OnTouchListener ontch = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			// TODO Auto-generated method stub
			x = (int)event.getX();
			y = (int)event.getY();
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//Display display = getWindowManager().getDefaultDisplay();
				//size = new Point();
				//display.getSize(size);
				mPreview.takePicture();
				
				break;
			/*case MotionEvent.ACTION_UP:
				Log.d("aaaaaaaaa", Preview.bitmap.getWidth()+"/"+Preview.bitmap.getHeight());
				Bitmap bmp = rotateImage(Preview.bitmap,90);

				
				float sx =(float) bmp.getWidth()/size.x;
				float sy =(float) bmp.getHeight()/size.y;
				x=(int)(x*sx);
				y=(int)(y*sy);
				int tch = bmp.getPixel(x, y);
				txtColorName.setTextColor(tch);
				break;
			case MotionEvent.ACTION_MOVE:
				txtColorName.setText("Toa do:" + x + "/" + y);
				break;
				*/
			}

			return true;
		}
	};
	
	private Bitmap resizeImage(final Bitmap image) {
		Bitmap resizedImage = null;		
		resizedImage = Bitmap.createScaledBitmap(image, MainActivity.size.x,MainActivity.size.y, true);
		return resizedImage;
	}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
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
