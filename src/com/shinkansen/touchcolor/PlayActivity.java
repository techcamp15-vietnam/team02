package com.shinkansen.touchcolor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shinkansen.touchcolor.constant.Constant;
import com.shinkansen.touchcolor.soundmanager.SoundManager;

public class PlayActivity extends Activity {

	private Preview mPreview;
	private Camera mCamera;
	public int x, y;
	public static Point size, scale;
	private TextView txtColorName;
	private ImageView ivShowColor;
	private ImageView ivTransparent;
	private FrameLayout previewLayout;
	private String colorCatchedName;
	private String colorCurrentName;
	private ImageView imageCheck;
	private int randIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		ivTransparent = (ImageView) findViewById(R.id.iv_pTransparent);

		previewLayout = (FrameLayout) findViewById(R.id.play_preview);
		txtColorName = (TextView) findViewById(R.id.txt_pColorName);
		ivShowColor = (ImageView) findViewById(R.id.iv_pShowColor);
		imageCheck = (ImageView) findViewById(R.id.imageCheck);

		// init camera
		mPreview = new Preview(this);
		mCamera = Camera.open();
		mPreview.setCamera(mCamera);
		previewLayout.addView(mPreview);

		ivTransparent.setOnTouchListener(ontch);
		randIndex = randomColorIndex();
		colorCurrentName = Constant.COLOR[randIndex];
		ivShowColor.setBackgroundColor(Constant.COLOR_ID[randIndex]);
		txtColorName.setText(colorCurrentName);
		txtColorName.setTextColor(Constant.COLOR_ID[randIndex]);

		MainActivity.mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					Bitmap bmp = (Bitmap) msg.obj;

					float sx = (float) bmp.getWidth() / size.x;
					float sy = (float) bmp.getHeight() / size.y;

					int xt = (int) (x * sx);
					int yt = (int) (y * sy);
					// Log.d("sss",x+"/"+y);
					int tch = bmp.getPixel(xt, yt);
					ivShowColor.setBackgroundColor(tch);

					colorCatchedName = getBestMatchingColorName(tch);
					TextView tempimg = (TextView) findViewById(R.id.textView1);
					tempimg.setText(colorCatchedName);
					
					if (colorCatchedName == colorCurrentName) {
						SoundManager.getInstance().playSound(Constant.SOUND_ID.length-1);
						new CountDownTimer(1500, 1000) {
						
							public void onTick(long millisUntilFinished) {
								imageCheck.setVisibility(View.VISIBLE);
								int randIndex = randomImageCorrect();
								imageCheck
										.setBackgroundResource(Constant.SMILE_IMG[randIndex]);
								RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) imageCheck
										.getLayoutParams();
								if (x - 120 >= 0)
									mParams.leftMargin = x - 120;
								else
									mParams.leftMargin = 0;
								if (y - 120 >= 0)
									mParams.topMargin = y - 120;
								else
									mParams.topMargin = y - 0;
								imageCheck.setLayoutParams(mParams);
							}

							public void onFinish() {
								imageCheck.setVisibility(View.GONE);
								int randIndex = randomColorIndex();
								colorCurrentName = Constant.COLOR[randIndex];
								ivShowColor
										.setBackgroundColor(Constant.COLOR_ID[randIndex]);
								txtColorName.setText(colorCurrentName);
								txtColorName
										.setTextColor(Constant.COLOR_ID[randIndex]);
							}
						}.start();

					} else {
						SoundManager.getInstance().playSound(Constant.SOUND_ID.length);
						new CountDownTimer(1500, 1000) {
							
							public void onTick(long millisUntilFinished) {
								
								imageCheck.setVisibility(View.VISIBLE);
								int randIndex = randomImageFail();
								imageCheck
										.setBackgroundResource(Constant.SAD_IMG[randIndex]);
								RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) imageCheck
										.getLayoutParams();
								if (x - 120 >= 0)
									mParams.leftMargin = x - 120;
								else
									mParams.leftMargin = 0;
								if (y - 120 >= 0)
									mParams.topMargin = y - 120;
								else
									mParams.topMargin = y - 0;
								imageCheck.setLayoutParams(mParams);
							}

							public void onFinish() {
								imageCheck.setVisibility(View.GONE);
							}
						}.start();
					}
				}
			}

		};

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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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
	 * get random Color
	 * 
	 * @param
	 * @author 2C-huuthang
	 */
	public int randomColorIndex() {
		int rand = (int) (Math.random() * Constant.COLOR.length);
		return rand;
	}

	/**
	 * get random image on correct
	 * 
	 * @param
	 * @author 2A-duythanh
	 */
	public int randomImageCorrect() {
		int rand = (int) (Math.random() * Constant.SMILE_IMG.length);
		return rand;
	}

	/**
	 * get random image on fail
	 * 
	 * @param
	 * @author 2A-duythanh
	 */
	public int randomImageFail() {
		int rand = (int) (Math.random() * Constant.SAD_IMG.length);
		return rand;
	}

	/**
	 * Event when button click
	 * 
	 * @param view
	 *            : view of button
	 * @author 2C-huuthang
	 */
	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btn_p_play_sound: {
			SoundManager.getInstance().playSound(randIndex + 2);
			break;
		}
		case R.id.btn_next: {
			randIndex = randomColorIndex();
			String colorString = Constant.COLOR[randIndex];
			Log.d("color rand", colorString);
			ivShowColor.setBackgroundColor(Constant.COLOR_ID[randIndex]);
			txtColorName.setText(colorString);
			txtColorName.setTextColor(Constant.COLOR_ID[randIndex]);
			SoundManager.getInstance().playSound(randIndex + 2);

			break;
		}

		default:
			break;
		}
	}

	/**
	 * On touch screen, get color of the position
	 * 
	 * @param:
	 * @author 2A-duythanh
	 */

	OnTouchListener ontch = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			
			x = (int) event.getX();
			y = (int) event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				Display display = getWindowManager().getDefaultDisplay();
				size = new Point();
				display.getSize(size);
				mPreview.takePicture();

				break;

			}

			return true;
		}
	};

	/**
	 * Get name Color of pixel
	 * 
	 * @param color
	 *            : color of pixel(int)
	 * @author 2A-duythanh
	 */

	private String getBestMatchingColorName(int pixelColor) {
		Map<String, Integer> mColors = new HashMap<String, Integer>();
		mColors.put("あおい", Color.rgb(0, 0, 255));
		mColors.put("あおい", Color.rgb(0, 0, 238));
		mColors.put("あおい", Color.rgb(0, 0, 205));
		mColors.put("あおい", Color.rgb(0, 0, 139));
//		mColors.put("cyan", Color.rgb(0, 255, 255));
		mColors.put("みどり", Color.rgb(0, 255, 0));
		mColors.put("みどり", Color.rgb(0, 238, 0));
		mColors.put("みどり", Color.rgb(0, 205, 0));
		
		mColors.put("きいろ", Color.rgb(255, 255, 0));
		mColors.put("きいろ", Color.rgb(238, 238, 0));
		mColors.put("きいろ", Color.rgb(238, 220, 130));
		mColors.put("きいろ", Color.rgb(238, 201, 0));
		mColors.put("きいろ", Color.rgb(255, 236, 139));
		mColors.put("きいろ", Color.rgb(205, 205, 0));
		
		mColors.put("あかい", Color.rgb(206, 14, 12));
		mColors.put("あかい", Color.rgb(238, 22, 2));
		mColors.put("あかい", Color.rgb(255, 4, 4));
		mColors.put("あかい", Color.rgb(139, 11, 11));
		
		mColors.put("オレンジ", Color.rgb(255, 153, 18));
		mColors.put("オレンジ", Color.rgb(237, 145, 33));
		mColors.put("オレンジ", Color.rgb(255, 140, 0));
		mColors.put("オレンジ", Color.rgb(255, 127, 0));
		mColors.put("オレンジ", Color.rgb(255, 102, 0));
		mColors.put("オレンジ", Color.rgb(255, 128, 0));
		
		
		
		mColors.put("パープル", Color.rgb(148, 0, 211));
		mColors.put("パープル", Color.rgb(153, 50, 204));
		mColors.put("パープル", Color.rgb(138, 43, 226));
		mColors.put("パープル", Color.rgb(155, 48, 255));
		mColors.put("パープル", Color.rgb(145, 44, 238));
		
		mColors.put("しろい", Color.rgb(255, 255, 255));
//		mColors.put("pink", Color.rgb(255, 192, 203));
		mColors.put("ピンク", Color.rgb(238, 121, 159));
		mColors.put("ピンク", Color.rgb(205, 104, 137));
		mColors.put("ピンク", Color.rgb(238, 162, 173));
		mColors.put("ピンク", Color.rgb(238, 18, 137));
		mColors.put("ピンク", Color.rgb(238, 48, 167));
		mColors.put("ピンク", Color.rgb(255, 20, 147));
		

		mColors.put("くろい", Color.rgb(0, 0, 0));
		
		
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
	 * Rotate Image bitmap
	 * 
	 * @param src
	 *            : path of image bitmap; degree: rotate degree
	 * @author 2A-duythanh
	 */

	public Bitmap rotateImage(Bitmap src, float degree) {
		// create new matrix object
		Matrix matrix = new Matrix();
		// setup rotation degree
		matrix.postRotate(degree);
		// return new bitmap rotated using matrix
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(),
				matrix, true);
	}

	/**
	 * Menu show dialog
	 * 
	 * @param
	 * @author 2B-congthang
	 */
	public void showDialogAbout() {
		AlertDialog.Builder b = new AlertDialog.Builder(this);

		b.setTitle("About");
		b.setMessage("Sinkansen team");
		b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		b.create().show();
	}

	public void showDialogManual() {
		AlertDialog.Builder b = new AlertDialog.Builder(this);

		b.setTitle("Manual");
		b.setMessage("I don't know!");
		b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		b.create().show();
	}

}
