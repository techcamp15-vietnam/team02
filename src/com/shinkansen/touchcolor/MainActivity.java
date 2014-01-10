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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
import com.shinkansen.touchcolor.constant.Constant;
import com.shinkansen.touchcolor.datahelper.RelateObjectDataSource;
import com.shinkansen.touchcolor.soundmanager.SoundManager;

public class MainActivity extends Activity {

	private Preview mPreview;
	private Camera mCamera;
	public int x, y;
	public static Point size, scale;
	private TextView txtColorName;
	private ImageView ivShowColor;
	private ImageView ivTransparent;
	private ImageView ivHelp;
	private FrameLayout previewLayout;

	private String colorCatchedName = "red";
	private Gallery gallery;
	public static Handler mHandler;
	private List<RelateObject> listObject;

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


		// init camera
		mPreview = new Preview(this);
		mCamera = Camera.open();
		mPreview.setCamera(mCamera);
		previewLayout.addView(mPreview);

		// int sound manager

		ivTransparent.setOnTouchListener(ontch);

		// get color's used last time
		SharedPreferences sharedPref = getSharedPreferences("local_data",
				MODE_PRIVATE);
		int colorId = sharedPref.getInt("last_color_id", Constant.COLOR_ID[0]);
		String colorString = sharedPref.getString("last_color_name", "あかい");
		colorCatchedName = colorString;

		ivShowColor.setBackgroundColor(colorId);
		txtColorName.setText(colorString);
		txtColorName.setTextColor(colorId);



		relateObject = new RelateObjectDataSource(this);

		listObject = relateObject.getObjectsByColor(colorString);
		// RelateObjectDataSource.addSampleData(relateObject);
		if (listObject.size() == 0) {
			RelateObjectDataSource.addSampleData(relateObject);
		}
		// show ralate object
		listObject = relateObject.getObjectsByColor(colorString);

		if (!pics.isEmpty())
			pics.clear();

		for (RelateObject relateObject : listObject) {
			String imgName = relateObject.getRObjectImageName();
			int indexImg = getResources().getIdentifier(imgName, "drawable",
					getPackageName());
			pics.add(indexImg);
		}

		gallery.setAdapter(new ImageAdapter(getApplicationContext(), pics));
		gallery.setSelection(1);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showPopupImage(pics.get(arg2), arg2);
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("MainActivity", "call onPause");
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
		Log.d("MainActivity", "call onResume");

		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					Bitmap bmp = (Bitmap) msg.obj;
					msg.obj = null;

					float sx = (float) bmp.getWidth() / size.x;
					float sy = (float) bmp.getHeight() / size.y;

					// Log.d("size =", "witdth ="+bmp.getWidth() +"height="+
					// bmp.getHeight() + "x=" + size.x + "y"+ size.y );
					x = (int) (x * sx);
					y = (int) (y * sy);
					int tch = bmp.getPixel(x, y);
					bmp.recycle();
					bmp = null;
					ivShowColor.setBackgroundColor(tch);
					colorCatchedName = getBestMatchingColorName(tch);
					txtColorName.setText(colorCatchedName);

					txtColorName.setTextColor(tch);

					for (int i = 0; i < Constant.COLOR.length; i++) {
						Log.d("Compare", Constant.COLOR[i] + "//"
								+ colorCatchedName);
						if (Constant.COLOR[i].equals(colorCatchedName)) {
							SoundManager.getInstance().playSound(i + 2);
							break;
						}
					}

					// show ralate object
					listObject = relateObject
							.getObjectsByColor(colorCatchedName);

					Log.d("num of object", String.valueOf(listObject.size()));

					if (!pics.isEmpty())
						pics.clear();

					for (RelateObject relateObject : listObject) {
						String imgName = relateObject.getRObjectImageName();
						int indexImg = getResources().getIdentifier(imgName,
								"drawable", getPackageName());
						pics.add(indexImg);
					}

					gallery.setAdapter(new ImageAdapter(
							getApplicationContext(), pics));
					gallery.setSelection(1);
					gallery.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							showPopupImage(pics.get(arg2), arg2);
						}
					});
				}
			}

		};
		// Open the default i.e. the first rear facing camera.
		if (mCamera == null) {
			mCamera = Camera.open();
			mPreview.setCamera(mCamera);

		}

	}

	@Override
	protected void onStop() {
		super.onStop();

		for (int i = 0; i < Constant.COLOR.length; i++) {
			if (Constant.COLOR[i].equals(this.colorCatchedName)) {
				SharedPreferences sharedPref = getSharedPreferences(
						"local_data", MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putInt("last_color_id", Constant.COLOR_ID[i]);
				editor.putString("last_color_name", Constant.COLOR[i]);
				editor.commit();
				break;
			}
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

			try {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					Display display = getWindowManager().getDefaultDisplay();
					size = new Point();
					display.getSize(size);
					mPreview.takePicture();

					break;

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return true;
		}
	};

	/**
	 * Get name Color of pixel
	 * 
	 * @param color
	 *            : color of pixel(int) <<<<<<< HEAD
	 * @author 2A-duythanh
	 */

	private String getBestMatchingColorName(int pixelColor) {
		Map<String, Integer> mColors = new HashMap<String, Integer>();
		mColors.put("あおい", Color.rgb(0, 0, 255));
		mColors.put("あおい", Color.rgb(0, 0, 238));
		mColors.put("あおい", Color.rgb(0, 0, 205));
		mColors.put("あおい", Color.rgb(0, 0, 139));
		// mColors.put("cyan", Color.rgb(0, 255, 255));
		// mColors.put("みどり", Color.rgb(0, 255, 0));
		// mColors.put("みどり", Color.rgb(0, 238, 0));
		// mColors.put("みどり", Color.rgb(0, 205, 0));
		mColors.put("みどり", Color.rgb(0, 37, 0));
		mColors.put("みどり", Color.rgb(0, 64, 0));
		mColors.put("みどり", Color.rgb(0, 80, 0));
		mColors.put("みどり", Color.rgb(0, 117, 0));
		mColors.put("みどり", Color.rgb(0, 166, 0));
		mColors.put("みどり", Color.rgb(0, 187, 0));
		mColors.put("みどり", Color.rgb(0, 219, 0));
		mColors.put("みどり", Color.rgb(0, 236, 0));
		mColors.put("みどり", Color.rgb(40, 255, 40));

		mColors.put("きいろ", Color.rgb(255, 255, 0));
		mColors.put("きいろ", Color.rgb(238, 238, 0));
		mColors.put("きいろ", Color.rgb(238, 220, 130));
		mColors.put("きいろ", Color.rgb(238, 201, 0));
		mColors.put("きいろ", Color.rgb(255, 236, 139));
		mColors.put("きいろ", Color.rgb(205, 205, 0));

		// mColors.put("あかい", Color.rgb(206, 14, 12));
		// mColors.put("あかい", Color.rgb(238, 22, 2));
		// mColors.put("あかい", Color.rgb(255, 4, 4));
		// mColors.put("あかい", Color.rgb(139, 11, 11));

		mColors.put("あかい", Color.rgb(234, 0, 0));
		mColors.put("あかい", Color.rgb(255, 0, 0));
		mColors.put("あかい", Color.rgb(255, 45, 45));

		mColors.put("オレンジ", Color.rgb(255, 153, 18));
		mColors.put("オレンジ", Color.rgb(237, 145, 33));
		mColors.put("オレンジ", Color.rgb(255, 140, 0));
		mColors.put("オレンジ", Color.rgb(255, 127, 0));
		mColors.put("オレンジ", Color.rgb(255, 102, 0));
		mColors.put("オレンジ", Color.rgb(255, 128, 0));
		mColors.put("オレンジ", Color.rgb(234, 117, 0));

		//
		// mColors.put("パープル", Color.rgb(148, 0, 211));
		// mColors.put("パープル", Color.rgb(153, 50, 204));
		// mColors.put("パープル", Color.rgb(138, 43, 226));
		// mColors.put("パープル", Color.rgb(155, 48, 255));
		// mColors.put("パープル", Color.rgb(145, 44, 238));
		mColors.put("パープル", Color.rgb(91, 0, 174));
		mColors.put("パープル", Color.rgb(111, 0, 211));
		mColors.put("パープル", Color.rgb(134, 0, 255));
		mColors.put("パープル", Color.rgb(146, 26, 255));
		mColors.put("パープル", Color.rgb(159, 53, 255));

		mColors.put("ピンク", Color.rgb(147, 0, 147));
		mColors.put("ピンク", Color.rgb(174, 0, 174));
		mColors.put("ピンク", Color.rgb(210, 0, 210));
		mColors.put("ピンク", Color.rgb(232, 0, 232));
		mColors.put("ピンク", Color.rgb(255, 0, 255));
		mColors.put("ピンク", Color.rgb(255, 68, 255));
		mColors.put("ピンク", Color.rgb(255, 119, 255));

		// mColors.put("pink", Color.rgb(255, 192, 203));
		// mColors.put("ピンク", Color.rgb(238, 121, 159));
		// mColors.put("ピンク", Color.rgb(205, 104, 137));
		// mColors.put("ピンク", Color.rgb(238, 162, 173));
		// mColors.put("ピンク", Color.rgb(238, 18, 137));
		// mColors.put("ピンク", Color.rgb(238, 48, 167));
		// mColors.put("ピンク", Color.rgb(255, 20, 147));

		mColors.put("しろい", Color.rgb(255, 255, 255));
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
		// continue iterating if the map contains a next colour and the
		// difference is greater than zero.
		// a difference of zero means we've found an exact match, so there's no
		// point in iterating further.
		while (colorNameIterator.hasNext() && currentDifference > 0) {
			// this colour's name
			String currentColorName = colorNameIterator.next();
			// this colour's int value
			int color = mColors.get(currentColorName);
			// get int values for all three colour components of this colour
			int colorR = Color.red(color);
			int colorG = Color.green(color);
			int colorB = Color.blue(color);
			// calculate sum of absolute differences that indicates how good
			// this match is
			int difference = Math.abs(pixelColorR - colorR)
					+ Math.abs(pixelColorG - colorG)
					+ Math.abs(pixelColorB - colorB);
			// a smaller difference means a better match, so keep track of it
			if (currentDifference > difference) {
				currentDifference = difference;
				closestColorName = currentColorName;
			}
		}
		// if(currentDifference<50)
		return closestColorName;
		// else return "Khong mau";
	}

	/**
	 * @author duythanh
	 */
	public String getNameColor(int color) {
		if (Color.parseColor("#3e4095") <= color
				&& Color.parseColor("#3e4095") + 1000 >= color)
			;

		return "";
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	 * Event when button click
	 * 
	 * @param view
	 *            : view of button
	 * @author 2C-huuthang
	 */
	public void onButtonClick(View view) {
		switch (view.getId()) {
		case R.id.btnPlaySound: {
			for (int i = 0; i < Constant.COLOR.length; i++) {
				Log.d("Compare", Constant.COLOR[i] + "//" + colorCatchedName);
				if (Constant.COLOR[i].equals(colorCatchedName)) {
					SoundManager.getInstance().playSound(i + 2);
					break;
				}
			}
			break;
		}
		case R.id.btnNextRememberScreen: {

			if (mCamera != null) {
				mPreview.setCamera(null);
				mCamera.release();
				mCamera = null;
			}
			Intent intent = new Intent(this, PlayActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			break;
		}

		default:
			break;
		}
	}

	/**
	 * Show popup Image
	 * 
	 * @param imageResource
	 *            : id of image; position: index of image
	 * @author huuthang
	 */
	private void showPopupImage(Integer imageResource, int position) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_popup_image,
				(ViewGroup) findViewById(R.id.layout_root));

		ViewPager viewPager = (ViewPager) layout.findViewById(R.id.view_pager);
		viewPager.setAdapter(new ImageViewPagerAdapter(this, pics));
		viewPager.setCurrentItem(position);

		dialog.setView(layout);
		dialog.setCancelable(true);

		AlertDialog alertDialog = dialog.create();
		alertDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		alertDialog.show();

		RelateObject obj = (RelateObject) listObject.get(position);
		int resId = getResources().getIdentifier(
				"raw/" + obj.getRObjectSoundPath(), "raw", getPackageName());
		SoundManager.getInstance().playSoundOfRelateObject(this, resId);

	}

	/**
	 * Menu show dialog
	 * 
	 * @param
	 * @author 2B-congthang
	 */
	public void showDialogAbout() {
		AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);

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
		AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);

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
