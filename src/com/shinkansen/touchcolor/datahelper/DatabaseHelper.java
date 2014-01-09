package com.shinkansen.touchcolor.datahelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "database2.db";
	public static final String TABLE_RELATE_OBJECT = "RelateObject";
	public static final String TABLE_COLOR = "Color";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_COLOR_NAME = "color_name";
	public static final String COLUMN_IMG_NAME = "image_name";
	public static final String COLUMN_SOUND_NAME = "sound_name";
	
	public static final String COLUMN_COLOR_HEX_CODE = "hex_code";
	
	private static final String DATABASE_CREATE = "create table "
		      + TABLE_RELATE_OBJECT 
		      + "(" + COLUMN_ID + " integer primary key autoincrement, "
		      + COLUMN_NAME + " text, "
		      + COLUMN_COLOR_NAME + " text," 
		      + COLUMN_IMG_NAME + " text,"
		      + COLUMN_SOUND_NAME + " text "+ " )";
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		Log.d("Create DB", DATABASE_CREATE);
		arg0.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, 1);
	}


}
