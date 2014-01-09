package com.shinkansen.touchcolor.constant;


import com.shinkansen.touchcolor.R;

import android.graphics.Color;

/**
Class define constant are used in App
@param:
@author 2C-huuthang
*/

public class Constant {
	public static final String[] COLOR =
		{"あかい","あおい","みどり","きいろ","オレンジ","パープル","ピンク","しろい","くろい"};
	public static final Integer[] COLOR_ID = {
		Color.parseColor("#FF0000"),
		Color.parseColor("#0000FF"),
		Color.parseColor("#008000"),
		Color.parseColor("#FFFF00"),
		Color.parseColor("#FFA500"),
		Color.parseColor("#800080"),
		Color.parseColor("#FFC0CB"),
		Color.parseColor("#FFFFFF"),
		Color.parseColor("#000000")};
	public static final Integer[] SOUND_ID = {
		R.raw.background,
		R.raw.red,
		R.raw.blue, 
		R.raw.green, 
		R.raw.yellow, 
		R.raw.orange,
		R.raw.purple,
		R.raw.pink,
		R.raw.white,
		R.raw.black};
	
	public static final int[] SAD_IMG = {
		R.drawable.sad1,
		R.drawable.sad2,
		R.drawable.sad3,
		R.drawable.sad4,
		R.drawable.sad5,
		R.drawable.sad6
	
	};
	
	public static final int[] SMILE_IMG = {
		R.drawable.smile1,
		R.drawable.smile2,
		R.drawable.smile3,
		R.drawable.smile4,
		R.drawable.smile5,
		R.drawable.smile6,
		R.drawable.smile7
	};
	
}
