package com.shinkansen.touchcolor.datahelper;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shinkansen.touchcolor.DataModel.RelateObject;

/**
 * Data Source of Relate Object
 * @author huuthang
 */
public class RelateObjectDataSource {
	private DatabaseHelper databaseHelper;
	
	private String[] allColumns = { DatabaseHelper.COLUMN_ID,
		      DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_COLOR_NAME,
		      DatabaseHelper.COLUMN_IMG_NAME, DatabaseHelper.COLUMN_SOUND_NAME};
	
	public RelateObjectDataSource(Context context) {
		databaseHelper = new DatabaseHelper(context); 
	}
	/**
	 Add Object to database
	@param obj : relate Object
	@author huuthang
	*/
	public void addRelateObject(RelateObject obj){
		Log.d("Add Object", obj.toString());
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_NAME, obj.getRObjectName());
		values.put(DatabaseHelper.COLUMN_COLOR_NAME, obj.getRObjectColor());
		values.put(DatabaseHelper.COLUMN_IMG_NAME, obj.getRObjectImageName());
		values.put(DatabaseHelper.COLUMN_SOUND_NAME, obj.getRObjectSoundPath());
		
		long num = db.insert(DatabaseHelper.TABLE_RELATE_OBJECT, null, values);
		Log.d("insert: ", String.valueOf(num));
		db.close();
	}
	
	/**
	 Get RelateObjects in database by Color name
	@param colorName: name of color
	@author huuthang
	*/
	public List<RelateObject> getObjectsByColor(String colorName){
		List<RelateObject> objects = new LinkedList<RelateObject>();
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		
		Cursor cursor = db.query(DatabaseHelper.TABLE_RELATE_OBJECT,
				allColumns, "color_name = ?",
				new String[] {String.valueOf(colorName)}, //args
				null, null, null, null);
		RelateObject object = null;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			object = new RelateObject();
			object.setRObjId(Integer.parseInt(cursor.getString(0)));
			object.setObjectName(cursor.getString(1));
			object.setObjectColor(cursor.getString(2));
			object.setObjectImageName(cursor.getString(3));
			object.setObjectSoundPath(cursor.getString(4));
			objects.add(object);
			cursor.moveToNext();
		}
		cursor.close();
		
		Log.d("Get all RelateObject", objects.toString());
		
		return objects;
	}
	public static void addSampleData(RelateObjectDataSource relateObject){
		RelateObject testObj = new RelateObject();
		for (int i = 1; i < 5; i++){
			testObj.setObjectImageName("do"+ i);
			testObj.setObjectColor("red");
			
			relateObject.addRelateObject(testObj);
		}
		
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
		}
	}
}
